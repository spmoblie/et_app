package com.elite.selfhelp;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;

import com.elite.selfhelp.activity.MainActivity;
import com.elite.selfhelp.config.AppConfig;
import com.elite.selfhelp.config.SharedConfig;
import com.elite.selfhelp.utils.DeviceUtils;
import com.elite.selfhelp.utils.LogUtils;
import com.elite.selfhelp.utils.TimeUtils;
import com.printer.sdk.PrinterInstance;

public class AppApplication extends Application {

	private static AppApplication spApp = null;

	public static int screenWidth; //屏幕宽
	public static int screenHeight; //屏幕高

	public static PrinterInstance mPrinter;

	/**
	 * 必须注册，Android框架调用Application
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		spApp = this;

		//获取屏幕宽高
		screenWidth = DeviceUtils.getDeviceWidth(spApp);
		screenHeight = DeviceUtils.getDeviceHeight(spApp);

		//打印手机信息
		LogUtils.i("device", "设备型号："+ DeviceUtils.getModel() + " 宽："+screenWidth + " / 高："+screenHeight);

		//程序崩溃时触发线程
		Thread.setDefaultUncaughtExceptionHandler(restartHandler);
	}

	public static synchronized AppApplication getInstance() {
		return spApp;
	}

	public static SharedPreferences getSharedPreferences() {
		return SharedConfig.getConfig(spApp).getSharedPreferences();
	}

	/**
	 * 全局唯一设备ID
	 */
	public static String getOnlyID() {
		String idStr = DeviceUtils.getAndroidId() + DeviceUtils.getSerialNumber();
		String s1 = idStr.substring(0, 2);
		String s2 = idStr.substring(2, idStr.length());
		idStr = s2 + s1;
		return idStr;
	}

	/**
	 * 小票单据号
	 */
	public static String getPrintCode() {
		return "C" + TimeUtils.getNowStr("yyyy-MM-dd") + getCode();
	}

	private static String getCode() {
		SharedPreferences sp = getSharedPreferences();
		int num = sp.getInt(AppConfig.KEY_PRINT_CODE, 1);
		String code = "";
		if (num <= 1) {
			code = "0001";
		} else if (num < 10) {
			code = "000" + String.valueOf(num);
		} else if (num < 100) {
			code = "00" + String.valueOf(num);
		} else if (num < 1000) {
			code = "0" + String.valueOf(num);
		} else if (num < 10000) {
			code = String.valueOf(num);
		} else {
			code = "E0001";
		}
		num++;
		sp.edit().putInt(AppConfig.KEY_PRINT_CODE, num).apply();
		return code;
	}

	/**
	 * 创建服务用于捕获崩溃异常
	 */
	private Thread.UncaughtExceptionHandler restartHandler = new Thread.UncaughtExceptionHandler() {
		public void uncaughtException(Thread thread, Throwable ex) {
			restartApp(); //发生崩溃异常时,重启应用
		}
	};

	/**
	 * 重启应用
	 */
	public void restartApp() {
		Intent intent = new Intent(spApp, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		spApp.startActivity(intent);
		//AppManager.getInstance().AppExit(spApp);
		//结束进程之前可以把你程序的注销或者退出代码放在这段代码之前
		android.os.Process.killProcess(android.os.Process.myPid());
	}

}