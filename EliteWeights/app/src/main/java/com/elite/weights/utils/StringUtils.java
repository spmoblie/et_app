package com.elite.weights.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

	public static boolean isNull(String str) {
		return str == null || str.length() == 0 || "".equals(str) || "null".equals(str);
	}

	public static boolean priceIsNull(String price) {
		return isNull(price) || price.equals("0") || price.equals("0.0") || price.equals("0.00");
	}

	public static boolean notNull(JSONObject jsonObj, String key) throws JSONException {
		return jsonObj.has(key) && !jsonObj.isNull(key) && !jsonObj.get(key).equals("");
	}

	public static int getInteger(String str) {
		if (isNull(str) || !isNumeric(str)) {
			return 0;
		}
		return Integer.parseInt(str);
	}

	public static double getDouble(String str) {
		if (isNull(str) || str.startsWith(".") ||
				str.contains(".") && str.substring(0, str.lastIndexOf(".")).contains(".")) {
			return 0.00;
		}
		return Double.parseDouble(str);
	}

	public static long getLong(String str) {
		if (isNull(str)) {
			return 0;
		}
		return Long.parseLong(str);
	}

	/**
	 * 判断手机格式是否正确
	 */
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * 判断email格式是否正确
	 */
	public static boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);

		return m.matches();
	}

	/**
	 * 判断是否全是数字
	 */
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (isNull(str) || !isNum.matches()) {
			return false;
		}
		return true;
	}
}
