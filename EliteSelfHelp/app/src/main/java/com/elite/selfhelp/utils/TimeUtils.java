package com.elite.selfhelp.utils;

import android.annotation.SuppressLint;
import android.content.Context;

import com.elite.selfhelp.AppApplication;
import com.elite.selfhelp.R;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

@SuppressLint({ "SimpleDateFormat", "DefaultLocale" })
public class TimeUtils {

	long start;

	public TimeUtils()
	{
		start = System.currentTimeMillis();
	}

	public void log(String tag, String msg) {
		LogUtils.i(tag, msg + (System.currentTimeMillis() - start)/1000 + "." + ((System.currentTimeMillis() - start)/100%10) + "秒");
	}

	/**
	 * 获取当前时间
	 * @param pattern yyyy-MM-dd HH:mm:ss  yyyy-MM-dd  HH:mm:ss ...
	 */
	public static Date getTimeDate(String pattern) {
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		String dateString = formatter.format(new Date());
		return formatter.parse(dateString, new ParsePosition(8));
	}

	/**
	 * 获取当前时间
	 * @param pattern yyyy-MM-dd HH:mm:ss  yyyy-MM-dd  HH:mm:ss ...
	 * @return 2018-05-15 16:17:18
	 */
	public static String getTimeStr(String pattern) {
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		return formatter.format(new Date());
	}

	/**
	 * 得到时间-小时
	 */
	public static String getHour() {
		String dateString = getTimeStr("yyyy-MM-dd HH:mm:ss");
		return dateString.substring(11, 13);
	}

	/**
	 * 得到时间-分钟
	 *
	 * @return
	 */
	public static String getTime() {
		String dateString = getTimeStr("yyyy-MM-dd HH:mm:ss");
		return dateString.substring(14, 16);
	}

	/**
	 * 获取当前时间
	 * @param pattern yyyy-MM-dd HH:mm:ss  yyyy-MM-dd  HH:mm:ss ...
	 * @return 20180515161718
	 */
	public static String getNowStr(String pattern) {
		String dateString = getTimeStr(pattern);
		return getReplaceStr(dateString);
	}

	/**
	 * 解析时间格式字符串
	 */
	public static String getReplaceStr(String dateStr) {
		if (dateStr.contains("-")) {
			dateStr = dateStr.replace("-", "");
		}
		if (dateStr.contains(" ")) {
			dateStr = dateStr.replace(" ", "");
		}
		if (dateStr.contains(":")) {
			dateStr = dateStr.replace(":", "");
		}
		return dateStr;
	}

	/**
	 * 字符串转换为时间
	 * @param strDate
	 * @param pattern yyyy-MM-dd HH:mm:ss  yyyy-MM-dd  HH:mm:ss ...
	 */
	public static Date strToDate(String strDate, String pattern) {
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		return formatter.parse(strDate, new ParsePosition(0));
	}

	/**
	 * 时间转换为字符串
	 * @param dateDate
	 * @param pattern yyyy-MM-dd HH:mm:ss  yyyy-MM-dd  HH:mm:ss ...
	 */
	public static String dateToStr(Date dateDate, String pattern) {
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		return formatter.format(dateDate);
	}

	/**
	 *  毫秒格式转换成时间格式
	 * @param dateTime 毫秒时间
	 * @param pattern 支持的时间格式：
	 *        yyyy/MM/dd HH:mm:ss 如 '2002/1/1 17:55:00'
	 *        yyyy/MM/dd HH:mm:ss pm 如 '2002/1/1 17:55:00 pm'
	 *        yyyy-MM-dd HH:mm:ss 如 '2002-1-1 17:55:00'
	 *        yyyy-MM-dd HH:mm:ss am 如 '2002-1-1 17:55:00 am'
	 */
	public static String dateTimeToStr(long dateTime, String pattern) {
		SimpleDateFormat sDateFormat = new SimpleDateFormat(pattern);
		return sDateFormat.format(new Date(dateTime + 0));
	}

	/**
	 * 将毫秒格式的时间换成自定义的字串格式：1秒
	 */
	public static String getTextTimeSecond(long time){
		Context ctx = AppApplication.getInstance().getApplicationContext();

		int day = (int) (time / 86400);
		long dayTime = time % 86400;
		int hour = (int) (dayTime / 3600);
		long hourTime = dayTime % 3600;
		int minute = (int) (hourTime / 60);
		long minuteTime = hourTime % 60;

		return minuteTime + ctx.getString(R.string.second);
	}

	/**
	 * 将毫秒格式的时间换成自定义的字串格式：1分1秒
	 */
	public static String getTextTimeMinuteSecond(long time){
		Context ctx = AppApplication.getInstance().getApplicationContext();

		int day = (int) (time / 86400);
		long dayTime = time % 86400;
		int hour = (int) (dayTime / 3600);
		long hourTime = dayTime % 3600;
		int minute = (int) (hourTime / 60);
		long minuteTime = hourTime % 60;

		// 30分钟有效期
		/*if (time <= 0 || day > 0 || hour > 0 || minute > 30) {
			return "";
		}*/
		return minute + ctx.getString(R.string.minute) + minuteTime + ctx.getString(R.string.second);
	}

	/**
	 * 将毫秒格式的时间换成自定义的字串格式：1天1时1分1秒
	 */
	public static String getTextTime(long time){
		Context ctx = AppApplication.getInstance().getApplicationContext();

		int day = (int) (time / 86400);
		long dayTime = time % 86400;
		int hour = (int) (dayTime / 3600);
		long hourTime = dayTime % 3600;
		int minute = (int) (hourTime / 60);
		long minuteTime = hourTime % 60;

		return day + ctx.getString(R.string.day) + hour + ctx.getString(R.string.hour) + minute + ctx.getString(R.string.minute) + minuteTime + ctx.getString(R.string.second);
	}

	/**
	 * 将毫秒格式的时间换成自定义的格式：[天,时,分,秒]
	 */
	public static Integer[] getArrayIntegerTime(long time){
		Integer[] times = new Integer[4];
		int day = (int) (time / 86400);
		long dayTime = time % 86400;
		int hour = (int) (dayTime / 3600);
		long hourTime = dayTime % 3600;
		int minute = (int) (hourTime / 60);
		long minuteTime = hourTime % 60;
		int second = (int) minuteTime;
		times[0] = day;
		times[1] = hour;
		times[2] = minute;
		times[3] = second;
		return times;
	}

	/**
	 * 二个小时的时间差,必须保证二个时间都是"HH:MM"的格式，返回字符型的分钟
	 */
	public static String getTwoHour(String st1, String st2) {
		String[] kk = null;
		String[] jj = null;
		kk = st1.split(":");
		jj = st2.split(":");
		if (Integer.parseInt(kk[0]) < Integer.parseInt(jj[0]))
			return "0";
		else {
			double y = Double.parseDouble(kk[0]) + Double.parseDouble(kk[1]) / 60;
			double u = Double.parseDouble(jj[0]) + Double.parseDouble(jj[1]) / 60;
			if ((y - u) > 0)
				return y - u + "";
			else
				return "0";
		}
	}

	/**
	 * 得到二个日期间的间隔天数
	 */
	public static long getTwoDay(String sj1, String sj2) {
		SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
		long day = 0;
		try {
			Date date = myFormatter.parse(sj1);
			Date mydate = myFormatter.parse(sj2);
			day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
		} catch (Exception e) {
			return -1;
		}
		return day;
	}

	/**
	 * 时间前推或后推分钟,其中JJ表示分钟.
	 */
	public static String getPreTime(String sj1, String jj) {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date1 = format.parse(sj1);
			long Time = (date1.getTime() / 1000) + Integer.parseInt(jj) * 60;
			date1.setTime(Time * 1000);
			return format.format(date1);
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 得到一个时间延后或前移几天的时间,nowdate为时间,delay为前移或后延的天数
	 */
	public static String getNextDay(String nowdate, String delay) {
		try{
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date d = strToDate(nowdate, "yyyy-MM-dd");
			long myTime = (d.getTime() / 1000) + Integer.parseInt(delay) * 24 * 60 * 60;
			d.setTime(myTime * 1000);
			return format.format(d);
		}catch(Exception e){
			return "";
		}
	}

	/**
	 * 获取一个月的最后一天
	 * @param date yyyy-MM-dd
	 */
	public static String getEndDateOfMonth(String date) {
		String str = date.substring(0, 8);
		String month = date.substring(5, 7);
		int mon = Integer.parseInt(month);
		if (mon == 1 || mon == 3 || mon == 5 || mon == 7 || mon == 8 || mon == 10 || mon == 12) {
			str += "31";
		} else if (mon == 4 || mon == 6 || mon == 9 || mon == 11) {
			str += "30";
		} else {
			if (isLeapYear(date)) {
				str += "29";
			} else {
				str += "28";
			}
		}
		return str;
	}

	/**
	 * 判断是否润年
	 */
	public static boolean isLeapYear(String ddate) {
		Date d = strToDate(ddate, "yyyy-MM-dd");
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(d);
		int year = gc.get(Calendar.YEAR);
		if ((year % 400) == 0) {
			return true; //被400整除是闰年
		} else {
			if ((year % 4) == 0 && !((year % 100) == 0)) {
				return true; //能被4整除同时不能被100整除则是闰年
			} else {
				return false; //不能被4整除或能被100整除则不是闰年
			}
		}
	}

	/**
	 * 判断二个时间是否在同一个周
	 * @param date1
	 * @param date2
	 */
	public static boolean isSameWeekDates(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(date1);
		cal2.setTime(date2);
		int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
		if (0 == subYear) {
			if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
				return true;
		} else if (1 == subYear && 11 == cal2.get(Calendar.MONTH)) {
			// 如果12月的最后一周横跨来年第一周的话则最后一周即算做来年的第一周
			if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
				return true;
		} else if (-1 == subYear && 11 == cal1.get(Calendar.MONTH)) {
			if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
				return true;
		}
		return false;
	}

	/**
	 * 产生周序列,即得到当前时间所在的年度是第几周
	 */
	public static String getSeqWeek() {
		Calendar c = Calendar.getInstance(Locale.CHINA);
		String week = Integer.toString(c.get(Calendar.WEEK_OF_YEAR));
		if (week.length() == 1)
			week = "0" + week;
		String year = Integer.toString(c.get(Calendar.YEAR));
		return year + week;
	}

}
