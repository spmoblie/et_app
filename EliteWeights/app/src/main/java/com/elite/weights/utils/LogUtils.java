package com.elite.weights.utils;

import android.util.Log;

public class LogUtils {
	
  public static void i(String tag, String msg) {
      Log.i(tag, msg);
  }
  
  public static void i(String tag, Object msg) {
	  i(tag, String.valueOf(msg));
  }

}
