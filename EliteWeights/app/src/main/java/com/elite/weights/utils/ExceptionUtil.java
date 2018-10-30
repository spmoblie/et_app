package com.elite.weights.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtil {
	
	public static void handle(Exception e) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		e.printStackTrace(printWriter);
		LogUtils.i("ExceptionUtil", stringWriter.toString());
	}

}
