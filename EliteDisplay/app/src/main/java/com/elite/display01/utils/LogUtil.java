package com.elite.display01.utils;

import android.util.Log;

import com.elite.display01.config.AppConfig;

public class LogUtil {
	
  public static void i(String tag, String msg)
  {
	  if (!AppConfig.IS_PUBLISH)
	  {
		  Log.i(tag, msg);
	  }
  }
  
  public static void i(String tag, Object msg)
  {
	  i(tag, String.valueOf(msg));
  }

}
