package com.elite.display01;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;

import com.elite.display01.activity.MainActivity;
import com.elite.display01.config.SharedConfig;
import com.elite.display01.utils.DeviceUtil;
import com.elite.display01.utils.LogUtil;

public class AppApplication extends Application {

	private static AppApplication spApp = null;

	public static int screenWidth; //屏幕宽
	public static int screenHeight; //屏幕高

	/**
	 * 必须注册，Android框架调用Application
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		spApp = this;

		//获取屏幕宽高
		screenWidth = DeviceUtil.getDeviceWidth(spApp);
		screenHeight = DeviceUtil.getDeviceHeight(spApp);

		//打印手机信息
		LogUtil.i("device", "设备型号："+ DeviceUtil.getModel() + " 宽："+screenWidth + " / 高："+screenHeight);

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
		return DeviceUtil.getSerialNumber() + DeviceUtil.getAndroidId();
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