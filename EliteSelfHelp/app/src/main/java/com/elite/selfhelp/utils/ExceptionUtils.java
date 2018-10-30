package com.elite.selfhelp.utils;


import com.elite.selfhelp.config.AppConfig;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtils {
	
	public static void handle(Exception e)
	{
		if (AppConfig.IS_PUBLISH) {
			// App正式发布之后将异常详细信息上传至错误统计平台
			//MobclickAgent.reportError(AppApplication.getInstance().getApplicationContext(), e);
		}

		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		e.printStackTrace(printWriter);
		LogUtils.i("ExceptionUtils", stringWriter.toString());
	}

}
