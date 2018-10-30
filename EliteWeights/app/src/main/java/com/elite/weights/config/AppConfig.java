package com.elite.weights.config;

import android.os.Environment;

public class AppConfig {

	/**
	 ******************************************* Path设置开始 ******************************************
	 */

	// 内置SD卡路径
	public static final String SDPATH = Environment.getExternalStorageDirectory().toString();
	// 缓存的根目录
	public static final String SAVE_PATH_ROOT = SDPATH + "/EliteWeight/";
	// 图片缓存路径
	public static final String SAVE_PATH_IMAGE = SAVE_PATH_ROOT + "image/";
	// 视频缓存路径
	public static final String SAVE_PATH_VIDEO = SAVE_PATH_ROOT + "video/";
	// 文本保存路径，应用关闭时不清除
	public static final String SAVE_PATH_TXT_SAVE = SAVE_PATH_ROOT + "txt/ew_ts/";
	// 文本缓存路径，应用关闭时须清除
	public static final String SAVE_PATH_TXT_DICE = SAVE_PATH_ROOT + "txt/ew_td/";

	/**
	 ******************************************* Path设置结束 ******************************************
	 */

	/**
	 ******************************************* Key设置开始 ******************************************
	 */

	// 缓存Key_串口号
	public static final String KEY_PORT = "key_prot";
	// 缓存Key_波特率
	public static final String KEY_BAUD = "key_baud";
	// 缓存Key_时间标记
	public static final String KEY_DATA_TIME = "key_data_time";

	/**
	 ******************************************* Key设置结束 ******************************************
	 */


	/**
	 ******************************************* other设置开始 ******************************************
	 */

	// 对话框事件标记
	public static final int DIALOG_0001 = 90001; //确定
	public static final int DIALOG_0002 = 90002; //取消

	// 默认串口号
	public static final String DEFAULT_PORT = "/dev/ttyS0";
	// 默认波特率
	public static final int DEFAULT_BAUD = 9600;

	/**
	 ******************************************* other设置结束 ******************************************
	 */

}