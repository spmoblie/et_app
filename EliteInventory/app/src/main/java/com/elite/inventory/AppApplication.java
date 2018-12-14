package com.elite.inventory;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.elite.inventory.activity.MainActivity;
import com.elite.inventory.config.AppConfig;
import com.elite.inventory.config.SharedConfig;
import com.elite.inventory.utils.DeviceUtils;
import com.elite.inventory.utils.LogUtils;

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
		screenWidth = DeviceUtils.getDeviceWidth(spApp);
		screenHeight = DeviceUtils.getDeviceHeight(spApp);

		//打印设备信息
		LogUtils.i("device", "设备型号："+ DeviceUtils.getModel() + " 宽："+screenWidth + " / 高："+screenHeight);

		//程序崩溃时触发线程
		Thread.setDefaultUncaughtExceptionHandler(restartHandler);
	}

	public static synchronized Context getAppContext() {
		return spApp.getApplicationContext();
	}

	public static SharedPreferences getSharedPreferences() {
		return SharedConfig.getConfig(spApp).getSharedPreferences();
	}

	/**
	 * 获取全局货币符号
	 */
	public static String getCurrency() {
		return getSharedPreferences().getString(AppConfig.KEY_CURR, spApp.getString(R.string.goods_curr_rmb));
	}

	/**
	 * 设置全局货币符号
	 */
	public static void setCurrency(String currStr) {
		getSharedPreferences().edit().putString(AppConfig.KEY_CURR, currStr).apply();
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