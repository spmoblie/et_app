package com.elite.weights.activity;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.elite.weights.AppApplication;
import com.elite.weights.R;
import com.elite.weights.utils.BitmapUtil;
import com.elite.weights.utils.DialogManager;
import com.elite.weights.utils.PrintLabelFruit;
import com.elite.weights.utils.PrinterUtils;
import com.elite.weights.utils.SerialPortUtil;
import com.elite.weights.view.Preview;
import com.printer.sdk.PrinterConstants;
import com.printer.sdk.PrinterInstance;

import java.util.HashMap;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements View.OnClickListener{

	private static final String TAG = "MainActivity";

	@BindView(R.id.rl_main)
	RelativeLayout rl_main;

	@BindView(R.id.sv_camera)
	SurfaceView surfaceView;

	@BindView(R.id.camera_iv_show)
	ImageView iv_show;

	@BindView(R.id.main_bt_test_rfid)
	Button bt_rfid;

	@BindView(R.id.main_bt_test_print)
	Button bt_print;

	@BindView(R.id.main_bt_setting)
	Button bt_setting;

	private Context mContext;
	private Preview mPreview;
	private Camera mCamera;
	private UsbDevice mUsbDevice;

	private static final int DEFAULT_WIDTH = 320;
	private static final int DEFAULT_HEIGHT = 180;
	private static final String ACTION_USB_PERMISSION = "com.android.usb.USB_PERMISSION";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		init();
		//初始化打印机
		initPrinter();
		//初始化电子秤
		SerialPortUtil.openSerialPort();
	}

	private void init() {
		mContext = this;
		bt_rfid.setOnClickListener(this);
		bt_print.setOnClickListener(this);
		bt_setting.setOnClickListener(this);

		/*mPreview = new Preview(this, surfaceView);
		mPreview.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		rl_main.addView(mPreview);
		startCamera();*/
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.main_bt_test_rfid:
				startActivity(new Intent(mContext, RFIDActivity.class));
				break;
			case R.id.main_bt_test_print:
				printText();
				break;
			case R.id.main_bt_setting:
				startActivity(new Intent(mContext, SettingActivity.class));
				break;
		}
	}

	private void printText() {
		PrintLabelFruit.doPrintTSPL(AppApplication.mPrinter, mContext,"测试", "单价：￥5.89/kg", "净重：10.00kg", "￥58.9", "010000810000081");
	}

	private void startCamera() {
		try{
			mCamera = Camera.open(0);
			mPreview.setCamera(mCamera);
		} catch (RuntimeException ex) {
			Toast.makeText(mContext, getString(R.string.camera_not_found), Toast.LENGTH_LONG).show();
		}
	}

	private void stopCamera(){
		if (mCamera != null) {
			mCamera.stopPreview();
			mPreview.setCamera(null);
			mCamera.release();
			mCamera = null;
		}
	}

	private void takePicture() {
		mCamera.takePicture(null, null, new JpegCallBack());
	}

	/**
	 * 初始化打印机
	 */
	protected void initPrinter() {
		UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
		HashMap<String, UsbDevice> devices = manager.getDeviceList();
		for (UsbDevice device : devices.values()) {
			if (PrinterUtils.isUsbPrinter(device)) { //匹配打印机型号
				openUsbDevice(device);
			}
		}
	}

	/**
	 * 连接USB设备
	 */
	protected void openUsbDevice(UsbDevice usbDevice) {
		if (usbDevice == null) return;
		mUsbDevice = usbDevice;
		Context ctx = AppApplication.getAppContext();
		AppApplication.mPrinter = PrinterInstance.getPrinterInstance(ctx, mUsbDevice, mHandler);
		UsbManager mUsbManager = (UsbManager) ctx.getSystemService(Context.USB_SERVICE);
		PrinterUtils.PrinterType = mUsbDevice.getVendorId();
		if (mUsbManager.hasPermission(mUsbDevice)) {
			AppApplication.mPrinter.openConnection();
		} else { // 没有权限询问用户是否授予权限
			PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, 0, new Intent(ACTION_USB_PERMISSION), 0);
			IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
			filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
			filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
			ctx.registerReceiver(mUsbReceiver, filter);
			mUsbManager.requestPermission(mUsbDevice, pendingIntent); // 该代码执行后，系统弹出一个对话框
		}
	}

	/**
	 * 断开USB设备
	 */
	protected void closeUsbDevice() {
		if (AppApplication.mPrinter != null) {
			AppApplication.mPrinter.closeConnection();
			AppApplication.mPrinter = null;
		}
	}

	/**
	 * 打印机连接监听器
	 */
	private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
		@SuppressLint("NewApi")
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			Log.w("Printer", "receiver action: " + action);

			if (ACTION_USB_PERMISSION.equals(action)) {
				synchronized (this) {
					AppApplication.getAppContext().unregisterReceiver(mUsbReceiver);
					UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
					if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)
							&& AppApplication.mPrinter != null && mUsbDevice != null && mUsbDevice.equals(device)) {
						AppApplication.mPrinter.openConnection();
					} else {
						mHandler.obtainMessage(PrinterConstants.Connect.FAILED).sendToTarget();
						Log.e("Printer", "permission denied for device " + device);
					}
				}
			}
		}
	};

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case PrinterConstants.Connect.SUCCESS:

					break;
				case PrinterConstants.Connect.FAILED:

					break;
				case PrinterConstants.Connect.CLOSED:

					break;
				case PrinterConstants.Connect.NODEVICE:

					break;
			}
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		//startCamera();
		/*if (mCamera != null) {
			mCamera.startPreview();
		}*/
	}

	@Override
	protected void onPause() {
		//stopCamera();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private class JpegCallBack implements Camera.PictureCallback {

		public JpegCallBack() {

		}


		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			Bitmap bm = BitmapUtil.getBitmap(data, DEFAULT_WIDTH, DEFAULT_HEIGHT);
			if (bm != null) {
				bm = BitmapUtil.reverseBitmap(bm, 0); //水平反转
				showImg(bm);
			}
			camera.startPreview();
		}

	}

	public void showImg(Bitmap bm) {
		if (iv_show != null && bm != null) {
			iv_show.setImageBitmap(bm);
		}
	}

}


