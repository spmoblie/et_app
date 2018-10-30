package com.elite.selfhelp.activity;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import com.elite.selfhelp.AppApplication;
import com.elite.selfhelp.R;
import com.elite.selfhelp.config.AppConfig;
import com.elite.selfhelp.db.CategoryDBService;
import com.elite.selfhelp.db.GoodsDBService;
import com.elite.selfhelp.db.OrderDBService;
import com.elite.selfhelp.entity.InvoiceEntity;
import com.elite.selfhelp.retrofit.Fault;
import com.elite.selfhelp.retrofit.HttpRequests;
import com.elite.selfhelp.utils.CommonTools;
import com.elite.selfhelp.utils.DialogManager;
import com.elite.selfhelp.utils.ExceptionUtils;
import com.elite.selfhelp.utils.LoadDialog;
import com.elite.selfhelp.utils.LogUtils;
import com.elite.selfhelp.utils.PrinterUtils;
import com.elite.selfhelp.utils.SerialPortUtils;
import com.elite.selfhelp.utils.StringUtils;
import com.elite.selfhelp.utils.TimeUtils;
import com.elite.selfhelp.utils.XTUtils;
import com.lkl.sample.channelpay.nativeapi.CppSdkInterf;
import com.lkl.sample.channelpay.nativeapi.ResponseCode;
import com.printer.sdk.PrinterConstants;
import com.printer.sdk.PrinterInstance;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;

/**
 * Created by Beck on 2018/1/26.
 */
public abstract class BaseActivity extends AppCompatActivity {

    public static final String TAG = "BaseActivity";

    public static final int DIALOG_CONFIRM = 456; //全局对话框“确定”
    public static final int DIALOG_CANCEL = 887; //全局对话框“取消”

    protected SharedPreferences sp;
    protected GoodsDBService db_goods;
    protected OrderDBService db_order;
    protected CategoryDBService db_category;
    private Context mContext;
    private DialogManager dm = null;
    private int dmWidth;
    private boolean isToast = false;
    private UsbDevice mUsbDevice;
    private static final String ACTION_USB_PERMISSION = "com.android.usb.USB_PERMISSION";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);
        // 创建动画
        overridePendingTransition(R.anim.in_from_right, R.anim.anim_no_anim);
        LogUtils.i(TAG, "onCreate");

        mContext = this;
        sp = AppApplication.getSharedPreferences();
        db_goods = GoodsDBService.getInstance(this);
        db_order = OrderDBService.getInstance(this);
        db_category = CategoryDBService.getInstance(this);
        dm = DialogManager.getInstance(this);
        dmWidth = AppApplication.screenWidth * 1/4;
    }

    /**
     * 获取打印机连接状态
     */
    protected boolean isConnected() {
        return sp.getBoolean(AppConfig.KEY_CONNECT, false)
                && (getType() == AppConfig.PRINTER_TYPE_USB || getType() == AppConfig.PRINTER_TYPE_SERIAL);
    }

    /**
     * 缓存打印机连接状态
     */
    protected void setConnected(boolean connected) {
        sp.edit().putBoolean(AppConfig.KEY_CONNECT, connected).apply();
    }
    /**
     * 获取已连接的打印机类型
     */
    protected int getType() {
        return sp.getInt(AppConfig.KEY_PRINTER, -1);
    }

    /**
     * 缓存已连接的打印机类型
     */
    protected void setType(int type) {
        sp.edit().putInt(AppConfig.KEY_PRINTER, type).apply();
    }

    /**
     * 获取已连接的打印机VID
     */
    protected int getVID() {
        return sp.getInt(AppConfig.KEY_PRINTER_VID, -1);
    }

    /**
     * 缓存已连接的打印机VID
     */
    protected void setVID(int vid) {
        sp.edit().putInt(AppConfig.KEY_PRINTER_VID, vid).apply();
    }

    /**
     * 获取打印纸张宽度
     */
    protected int getSize() {
        PrinterConstants.paperWidth = sp.getInt(AppConfig.KEY_PRINT_SIZE, AppConfig.PRINT_WIDTH_58);
        return PrinterConstants.paperWidth;
    }

    /**
     * 缓存打印纸张宽度
     */
    protected void setSize(int size) {
        PrinterConstants.paperWidth = size;
        sp.edit().putInt(AppConfig.KEY_PRINT_SIZE, size).apply();
    }

    /**
     * 初始化打印机
     */
    protected void initPrinter() {
        if (!isConnected()) {
            connectStatus(false, "打印机未连接");
        } else {
            getSize(); //获取打印纸张宽度
            switch (getType()) {
                case AppConfig.PRINTER_TYPE_USB: //USB打印机
                    UsbDevice ud = null;
                    UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
                    HashMap<String, UsbDevice> devices = manager.getDeviceList();
                    for (UsbDevice device : devices.values()) {
                        if (PrinterUtils.isUsbPrinter(device)) { //匹配打印机型号
                            if (getVID() == device.getVendorId()) {
                                ud = device;
                            }
                        }
                    }
                    if (ud != null) { //识别到与缓存匹配的打印机
                        openUsbDevice(ud, false);
                    } else {
                        connectStatus(false, "打印机已更换，请重新配置");
                    }
                    break;
                case AppConfig.PRINTER_TYPE_SERIAL: //串口打印机
                    openSerialPort(false);
                    break;
                default:
                    connectStatus(false, "打印配置失效，请重新配置");
                    break;
            }
        }
    }

    /**
     * 连接USB设备
     */
    protected void openUsbDevice(UsbDevice usbDevice, boolean isShow) {
        if (usbDevice == null) return;
        isToast = isShow;
        mUsbDevice = usbDevice;
        Context ctx = AppApplication.getInstance().getApplicationContext();
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
                    AppApplication.getInstance().getApplicationContext().unregisterReceiver(mUsbReceiver);
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

    /**
     * 打印机连接状态消息处理机制
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PrinterConstants.Connect.SUCCESS:
                    if (isToast) {
                        connectStatus(true, "USB打印机连接成功");
                    } else {
                        connectStatus(true, "");
                    }
                    break;
                case PrinterConstants.Connect.FAILED:
                    connectStatus(false, "USB打印机连接失败");
                    break;
                case PrinterConstants.Connect.CLOSED:
                    connectStatus(false, "USB打印机连接关闭");
                    break;
                case PrinterConstants.Connect.NODEVICE:
                    connectStatus(false, "未发现USB打印设备");
                    break;
            }
        }
    };

    /**
     * 打印机连接状态处理
     */
    private void connectStatus(boolean isConnected, String showStr) {
        setConnected(isConnected);
        if (isConnected) {
            if (mUsbDevice != null) {
                setVID(mUsbDevice.getVendorId());
            }
        } else {
            if (SettingActivity.instance == null) {
                setVID(-1);
                setType(-1);
            }
        }
        if (SettingActivity.instance != null) {
            SettingActivity.instance.updateConnectStatus();
        }
        if (!StringUtils.isNull(showStr)) {
            showToast(showStr);
            LogUtils.i("Printer", showStr);
        }
    }

    /**
     * 打印小票
     */
    protected int stratPrint(InvoiceEntity ie) {
        int status = 0;
        if (AppApplication.mPrinter != null) {
            status = AppApplication.mPrinter.getCurrentStatus();
        }
        if (isConnected()) {
            if (status == 0) {
                XTUtils.printText(getResources(), ie);
            } else if (status == -2) {
                showToast(getString(R.string.goods_print_paper));
            } else {
                showToast(getString(R.string.goods_print_error));
            }
        } else {
            showToast(getString(R.string.goods_print_connect_no));
        }
        if (AppApplication.mPrinter != null) {
            status = AppApplication.mPrinter.getCurrentStatus();
        }
        return status;
    }

    /**
     * 打开串口
     */
    protected void openSerialPort(boolean isShow){
        SerialPortUtils.openSerialPort();
        if (isShow) {
            connectStatus(true, "串口打印机连接成功");
        } else {
            connectStatus(true, "");
        }
    }

    /**
     * 关闭串口
     */
    protected void closeSerialPort(){
        SerialPortUtils.closeSerialPort();
        connectStatus(false, "串口打印机连接关闭");
    }

    /**
     * 重启串口
     */
    protected void restartSerialPort(){
        SerialPortUtils.closeSerialPort();
        openSerialPort(true);
    }

    /**
     * 加载网络数据
     */
    protected void loadDatas(String path, HashMap<String, String> map, int httpType, final int dataType) {
        HttpRequests.getInstance()
                .loadDatas(path, map, httpType)
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onNext(ResponseBody body) {
                        try {
                            callbackDatas(new JSONObject(body.string()), dataType);
                        } catch (Exception e) {
                            ExceptionUtils.handle(e);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        if (throwable instanceof Fault) {
                            Fault fault = (Fault) throwable;
                            if (fault.getErrorCode() == 404) {
                                //错误处理
                            } else
                            if (fault.getErrorCode() == 500) {
                                //错误处理
                            } else
                            if (fault.getErrorCode() == 501) {
                                //错误处理
                            }
                        } else {
                            //错误处理
                        }
                        LogUtils.i("Retrofit","error message : " + throwable.getMessage());
                    }

                    @Override
                    public void onCompleted() {
                        // 结束处理
                    }
                });
    }

    /**
     * 回调网络数据
     */
    protected void callbackDatas(JSONObject jsonObject, int dataType) {};

    /**
     * 显示加载对话框
     */
    public void showLoadingDialog(){
        LoadDialog.show(mContext);
    }

    /**
     * 销毁加载对话框
     */
    public void hideLoadingDialog(){
        LoadDialog.hidden();
    }

    /**
     * 弹出两个按钮的对话框
     * @param titleStr 对话框标题
     * @param contentStr 对话框内容
     * @param leftStr 对话框左边按钮文本
     * @param rightStr 对话框右边按钮文本
     * @param isCenter 提示内容是否居中
     * @param isVanish 点击框以外是否消失
     */
    protected void showTwoBtnDialog(String titleStr, String contentStr, String leftStr, String rightStr,
                                    boolean isCenter, boolean isVanish, final Handler handler) {
        String lStr = leftStr.isEmpty() ? getString(R.string.cancel) : leftStr;
        String rStr = rightStr.isEmpty() ? getString(R.string.confirm) : rightStr;
        if (dm != null) {
            dm.showTwoBtnDialog(titleStr, contentStr, lStr, rStr, dmWidth, isCenter, isVanish, handler);
        }
    }

    /**
     * 弹出带输入框的对话框
     * @param title 标题
     * @param inputType 输入类型
     * @param isVanish 可否销毁
     * @param handler 回调
     */
    protected void showEditDialog(String title, int inputType, boolean isVanish, Handler handler) {
        if (dm != null) {
            dm.showEditDialog(title, dmWidth, inputType, isVanish, handler);
        }
    }

    /**
     * 销毁对话框
     */
    protected void dismissDialog() {
        if (dm != null) {
            dm.dismiss();
        }
    }

    /**
     * 弹出消息
     */
    protected void showToast(String content) {
        showToast(content, 3000);
    }

    /**
     * 弹出消息
     */
    protected void showToast(String content, long time) {
        CommonTools.showToast(content, time);
    }

    /**
     * 隐藏软键盘
     */
    protected void hideSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏
    }

    /**
     * 隐藏物理键盘
     */
    protected void toggleSoftInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (dm != null) {
            dm.clearInstance();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.i(TAG, "onDestroy");
    }

    @Override
    public void finish() {
        super.finish();
        // 销毁动画
        overridePendingTransition(R.anim.anim_no_anim, R.anim.out_to_left);
    }

    @Override
    public void setContentView(int layoutResID) {
        addSystemUIVisibleListener(); //添加监听
        setSystemUIVisible(false); //全屏显示
        super.setContentView(layoutResID);
        //Butter Knife初始化
        ButterKnife.bind(this);
    }

    @Override
    public void setContentView(View view) {
        addSystemUIVisibleListener(); //添加监听
        setSystemUIVisible(false); //全屏显示
        super.setContentView(view);
        //Butter Knife初始化
        ButterKnife.bind(this);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        addSystemUIVisibleListener(); //添加监听
        setSystemUIVisible(false); //全屏显示
        super.setContentView(view, params);
        //Butter Knife初始化
        ButterKnife.bind(this);
    }

    /**
     * 添加沉浸式模式变化的监听
     */
    public void addSystemUIVisibleListener() {
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (!getSystemUIVisible()) {
                            //非全屏时保持全屏
                            setSystemUIVisible(false);
                        }
                    }
                });
    }

    /**
     * 设置状态栏和导航栏状态
     * @param show
     */
    public void setSystemUIVisible(boolean show) {
        int visibility = getWindow().getDecorView().getSystemUiVisibility();
        int flags = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        if (show) {
            visibility &= ~flags;
        } else {
            visibility |= flags;
        }
        getWindow().getDecorView().setSystemUiVisibility(visibility);
    }

    /**
     * 判定是否全屏
     */
    public boolean getSystemUIVisible() {
        int visibility = getWindow().getDecorView().getSystemUiVisibility();
        int flags = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        return (visibility & flags) == flags;
    }

    /**
     * 支付激活
     */
    protected void payActive(final String activeCode) {
        showLoadingDialog();
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    CppSdkInterf interf = new CppSdkInterf(getApplicationContext());
                    //定义
                    interf.getDBPath();
                    //设备
                    interf.setTid(AppApplication.getOnlyID());
                    //激活  373798375653  373798345674
                    int activeResult = interf.doActive(activeCode);
                    LogUtils.i("LKLPay", "id = " + AppApplication.getOnlyID() + "  active = " + activeCode + "  active result = " + activeResult);

                    subscriber.onNext((activeResult == 0) ? "激活成功" : ResponseCode.POSP_ERR.get(activeResult));
                } catch (Exception e) {
                    hideLoadingDialog();
                    ExceptionUtils.handle(e);
                }
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onNext(String str) {
                hideLoadingDialog();
                showToast(str);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onCompleted() {

            }
        });
    }

    /**
     * 支付反激活
     */
    protected void payDeActive() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    CppSdkInterf interf = new CppSdkInterf(getApplicationContext());
                    //设备
                    interf.setTid(AppApplication.getOnlyID());
                    //反激活
                    int da = interf.doDeactive();
                    LogUtils.i("LKLPay", "deactive result = " + da);
                } catch (Exception e) {
                    ExceptionUtils.handle(e);
                }
            }
        }).start();
    }

    /**
     * 定期签到
     */
    protected void regularLogin() {
        String saveTime = sp.getString(AppConfig.KEY_LOGIN_TIME, "");
        String nowTime = TimeUtils.getNowStr("yyyy-MM-dd");
        if (!StringUtils.isNull(saveTime)) {
            long interval = TimeUtils.getTwoDay(nowTime, saveTime);
            if (interval < 1) return; //一天内不重复签到
            sp.edit().putString(AppConfig.KEY_LOGIN_TIME, "").apply();
        }
        payLogin();
    }

    /**
     * 支付签到
     */
    protected void payLogin() {
        try {
            CppSdkInterf interf = new CppSdkInterf(getApplicationContext());
            //设备
            interf.setTid(AppApplication.getOnlyID());
            //签到
            int lg = interf.doLogin();
            LogUtils.i("LKLPay", "id = " + AppApplication.getOnlyID() + "  login result = " + lg);
            if (lg == 0) { //签到成功
                sp.edit().putString(AppConfig.KEY_LOGIN_TIME, TimeUtils.getNowStr("yyyy-MM-dd")).apply();
            }
        } catch (Exception e) {
            ExceptionUtils.handle(e);
        }
    }
}
