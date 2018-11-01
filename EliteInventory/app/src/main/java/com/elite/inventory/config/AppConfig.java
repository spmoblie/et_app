package com.elite.inventory.config;

import android.Manifest;
import android.os.Environment;


public class AppConfig {

	/**
	 ******************************************* Url设置开始 ******************************************
	 */

	// 是否正式发布
	public static final boolean IS_PUBLISH = false;
	// 网络传输协议http
	public final static String APP_HTTP = "http://";
	// 网络传输协议https
	public final static String APP_HTTPS = "https://";
	// 域名1
	public final static String ENVIRONMENT_TEST_APP_1 = APP_HTTP + "180.76.239.119:9002/elitejxc/mall/api/";
	// 域名2
	public final static String ENVIRONMENT_TEST_APP_2 = "";

	// Base域名
	public final static String ENVIRONMENT_BASE_URL = ENVIRONMENT_TEST_APP_1;
	// 图片域名
	public final static String ENVIRONMENT_PRESENT_IMG_APP = "";

	/**
	 ******************************************* Url设置结束 ******************************************
	 */

	/**
	 ******************************************* Path设置结束 ******************************************
	 */

	// 内置SD卡路径
	public static final String SDPATH = Environment.getExternalStorageDirectory().toString();
	// 缓存的根目录
	public static final String SAVE_PATH_ROOT = SDPATH + "/EliteSelfHelp/";
	// 图片缓存路径
	public static final String SAVE_PATH_IMAGE = SAVE_PATH_ROOT + "image/";
	// 视频缓存路径
	public static final String SAVE_PATH_VIDEO = SAVE_PATH_ROOT + "video/";

	/**
	 ******************************************* Path设置结束 ******************************************
	 */

	/**
	 ******************************************* Key设置结束 ******************************************
	 */

	// 缓存Key_班次
	public static final String KEY_WORK = "key_work";
	// 缓存Key_账号
	public static final String KEY_USER = "key_user";
	// 缓存Key_密码
	public static final String KEY_PASSWORD = "key_password";
	// 缓存Key_数据同步时间
	public static final String KEY_UPDATE_TIME = "key_update_time";

	/**
	 ******************************************* Key设置结束 ******************************************
	 */


	/**
	 ******************************************* other设置结束 ******************************************
	 */

	// 对话框事件标记
	public static final int DIALOG_0001 = 90001; //确定
	public static final int DIALOG_0002 = 90002; //取消

	// 默认串口号
	public static final String DEFAULT_USER = "0001";
	// 默认串口号
	public static final String DEFAULT_PASSWORD = "123456";

	// 默认串口号
	public static final String DEFAULT_PORT = "/dev/ttyS0";
	// 默认波特率
	public static final int DEFAULT_BAUD = 9600;
	// 打印纸张宽度
	public static final int PRINT_WIDTH_58 = 384;
	public static final int PRINT_WIDTH_80 = 576;
	public static final int PRINT_WIDTH_100 = 724;
	// 打印类型标记
	public static final int PRINTER_TYPE_USB = 901;
	public static final int PRINTER_TYPE_SERIAL = 902;

	// 数据类型标记
	public static final int DATA_TYPE_GOODS_LISTS = 601;
	public static final int DATA_TYPE_CATEGORY_LISTS = 602;

	// 需动态申请的权限
	public static final String PERMISSION_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
	public static final String PERMISSION_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;

	/**
	 ******************************************* other设置结束 ******************************************
	 */

}