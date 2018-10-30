package com.elite.selfhelp.config;

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
	public final static String ENVIRONMENT_TEST_APP_1 = APP_HTTP + "p.szelite.net:9002/elitejxc/mall/api/";
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

	// 缓存Key_串口号
	public static final String KEY_PORT = "key_prot";
	// 缓存Key_波特率
	public static final String KEY_BAUD = "key_baud";
	// 缓存Key_打印机连接状态
	public static final String KEY_CONNECT = "key_connect";
	// 缓存Key_打印机类型
	public static final String KEY_PRINTER = "key_printer";
	// 缓存Key_打印机VID
	public static final String KEY_PRINTER_VID = "key_printer_vid";
	// 缓存Key_打印纸张宽度
	public static final String KEY_PRINT_SIZE = "key_print_size";
	// 缓存Key_打印小票序号
	public static final String KEY_PRINT_CODE = "key_print_code";
	// 缓存Key_数据同步时间
	public static final String KEY_UPDATE_TIME = "key_update_time";
	// 缓存Key_管理密码
	public static final String KEY_PASSWORD = "key_password";
	// 缓存Key_签到时间
	public static final String KEY_LOGIN_TIME = "key_login_time";

	// 缓存Key_模板编辑选项-选中
	public static final String KEY_EDIT_SHOW_1 = "key_edit_show_1";
	public static final String KEY_EDIT_SHOW_2 = "key_edit_show_2";
	public static final String KEY_EDIT_SHOW_3 = "key_edit_show_3";
	public static final String KEY_EDIT_SHOW_4 = "key_edit_show_4";
	public static final String KEY_EDIT_SHOW_5 = "key_edit_show_5";
	public static final String KEY_EDIT_SHOW_6 = "key_edit_show_6";
	public static final String KEY_EDIT_SHOW_7 = "key_edit_show_7";
	public static final String KEY_EDIT_SHOW_8 = "key_edit_show_8";
	// 缓存Key_模板编辑选项-内容
	public static final String KEY_EDIT_TEXT_1 = "key_edit_text_1";
	public static final String KEY_EDIT_TEXT_2 = "key_edit_text_2";
	public static final String KEY_EDIT_TEXT_3 = "key_edit_text_3";
	public static final String KEY_EDIT_TEXT_4 = "key_edit_text_4";
	public static final String KEY_EDIT_TEXT_5 = "key_edit_text_5";
	public static final String KEY_EDIT_TEXT_6 = "key_edit_text_6";
	public static final String KEY_EDIT_TEXT_7 = "key_edit_text_7";

	/**
	 ******************************************* Key设置结束 ******************************************
	 */


	/**
	 ******************************************* other设置结束 ******************************************
	 */

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

	// 默认管理员密码
	public static final String DEFAULT_PASSWORD = "123456";

	// 需动态申请的权限
	public static final String PERMISSION_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
	public static final String PERMISSION_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;

	/**
	 ******************************************* other设置结束 ******************************************
	 */

}