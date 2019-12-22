package com.elite.display01.config;

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
	public final static String ENVIRONMENT_TEST_APP_1 = "";
	// 域名2
	public final static String ENVIRONMENT_TEST_APP_2 = "";

	// Base域名
	public final static String ENVIRONMENT_BASE_URL = APP_HTTPS;
	// 图片域名
	public final static String ENVIRONMENT_PRESENT_IMG_APP = "";
	// 联惠支付
	public final static String ENVIRONMENT_LOHONET_PAY = APP_HTTPS + "pay.lohonet.com/";

	/**
	 ******************************************* Url设置结束 ******************************************
	 */

	/**
	 ******************************************* Path设置结束 ******************************************
	 */

	// 内置SD卡路径
	public static final String SDPATH = Environment.getExternalStorageDirectory().toString();
	// 缓存的根目录
	public static final String SAVE_PATH_ROOT = SDPATH + "/EliteDisplay/";
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
	// 缓存Key_轮播时间
	public static final String KEY_TIME = "key_time";
	// 缓存Key_视频标记
	public static final String KEY_VIDEO = "key_video";
	// 缓存Key_管理密码
	public static final String KEY_PASSWORD = "key_password";

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
	// 默认轮播时间(秒)
	public static final int DEFAULT_TIME = 5;
	// 默认管理员密码
	public static final String DEFAULT_PASSWORD = "123456";
	// 拉卡拉支付激活码
	public static final String ACTIVE_CODE = "373798345674"; //373798375653

	// 联惠支付的公钥
	public static final  String PUB_KEY = "test_publickey.pem";
	// 客户生成的私钥
	public static final  String PRI_KEY = "pkcs8_rsa_private_key_2048.pem";

	// 需动态申请的权限
	public static final String PERMISSION_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
	public static final String PERMISSION_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;

	/**
	 ******************************************* other设置结束 ******************************************
	 */

}