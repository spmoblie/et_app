package com.elite.inventory.config;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 全局配置文件(偏好设置)
 */
public class SharedConfig {

	private static SharedConfig SC;
	private SharedPreferences sp;
	
	private SharedConfig(Context context){
		sp = context.getSharedPreferences("elite_inventory", Context.MODE_PRIVATE);
	}

	public static synchronized SharedConfig getConfig(Context context){
		if (SC == null) {
			SC = new SharedConfig(context);
		}
		return SC;
	}

	public SharedPreferences getSharedPreferences(){
		return sp;
	}

	public void clearConfig(){
		if (sp != null) {
			sp.edit().clear().apply();
		}
		SC = null;
	}
}
