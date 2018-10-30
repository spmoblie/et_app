package com.elite.selfhelp.utils;

import android.content.SharedPreferences;
import android.content.res.Resources;

import com.elite.selfhelp.AppApplication;
import com.elite.selfhelp.R;
import com.elite.selfhelp.config.AppConfig;
import com.elite.selfhelp.entity.InvoiceEntity;
import com.elite.selfhelp.entity.ShopEntity;
import com.printer.sdk.PrinterConstants;
import com.printer.sdk.Table;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class XTUtils {

	private static final String TAG = "XUtils";
	private static boolean is58mm = false;
	private static int printerType = AppConfig.PRINTER_TYPE_USB;

	private static final byte ES = (byte)0x1b;
	private static final byte FS = (byte)0x1c;
	private static final byte GS = (byte)0x1d;

	private static final byte[] ES_INIT = new byte[]{ES, '@'}; //初始化打印机
	private static final byte[] FS_NORM = new byte[]{FS, '.'}; //设置标准模式
	private static final byte[] FS_CHIN = new byte[]{FS, '&'}; //设置汉字模式
	private static final byte[] GS_MODE_CU = new byte[]{GS, 'V', 1}; //切纸指令
	private static final byte[] ES_FONT_SM = new byte[]{ES, '!', 0}; //标准字体
	private static final byte[] FS_FONT_SM = new byte[]{FS, '!', 0}; //标准字体
	private static final byte[] ES_FONT_BG = new byte[]{ES, '!',12}; //字体放大
	private static final byte[] FS_FONT_BG = new byte[]{FS, '!',12}; //字体放大
	private static final byte[] ES_BOLD_NO = new byte[]{ES, 'E', 0}; //取消加粗
	private static final byte[] ES_BOLD_OK = new byte[]{ES, 'E', 1}; //字体加粗
	private static final byte[] ES_SITE_LF = new byte[]{ES, 'a', 0}; //靠左打印
	private static final byte[] ES_SITE_CT = new byte[]{ES, 'a', 1}; //居中打印
	private static final byte[] ES_SITE_RG = new byte[]{ES, 'a', 2}; //靠右打印

	/**
	 * USB打印文本内容
	 */
	public static void printText(Resources res, InvoiceEntity data) {
		if (res == null || data == null) return;
		if(PrinterConstants.paperWidth == 384) {
			is58mm = true;
		}else {
			is58mm = false;
		}
		SharedPreferences sp = AppApplication.getSharedPreferences();
		printerType = sp.getInt(AppConfig.KEY_PRINTER, -1);

		mySendBytes(ES_INIT); //初始化打印机
		mySendBytes(FS_CHIN); //设置汉字模式

		if (sp.getBoolean(AppConfig.KEY_EDIT_SHOW_1, true)) {
			mySendBytes(ES_SITE_CT); //字体居中
			mySendBytes(ES_FONT_BG); //字体放大
			mySendBytes(FS_FONT_BG); //字体放大
			mySendBytes(ES_BOLD_OK); //字体加粗
			String txt_1 = sp.getString(AppConfig.KEY_EDIT_TEXT_1, "");
			if (StringUtils.isNull(txt_1)) {
				txt_1 = res.getString(R.string.print_title);
			}
			mySendBytes(getBytes(txt_1 + "\n"));
		}

		mySendBytes(ES_SITE_LF); //字体居左
		mySendBytes(ES_FONT_SM); //标准字体
		mySendBytes(FS_FONT_SM); //标准字体
		mySendBytes(ES_BOLD_NO); //取消加粗

		if (sp.getBoolean(AppConfig.KEY_EDIT_SHOW_1, true)) {
			mySendBytes(getBytes(" \n"));
		}

		String printSn = data.getPrintSn();
		StringBuffer sb = new StringBuffer();
		if (sp.getBoolean(AppConfig.KEY_EDIT_SHOW_2, true)) {
			String txt_2 = sp.getString(AppConfig.KEY_EDIT_TEXT_2, "");
			if (StringUtils.isNull(txt_2)) {
				txt_2 = res.getString(R.string.print_number);
			}
			sb.append(txt_2 + "\n");
		}
		sb.append(res.getString(R.string.print_code) + printSn + "\n");
		sb.append(res.getString(R.string.print_time) + data.getCreateTime() + "\n\n");
		mySendBytes(getBytes(sb.toString()));

		printTable(res, data); //打印表格信息
		mySendBytes(getBytes(" \n"));

		sb = new StringBuffer();
		sb.append(res.getString(R.string.print_pay_type) + "\n");

		if (sp.getBoolean(AppConfig.KEY_EDIT_SHOW_3, true)) {
			String txt_3 = sp.getString(AppConfig.KEY_EDIT_TEXT_3, "");
			if (StringUtils.isNull(txt_3)) {
				txt_3 = res.getString(R.string.print_company_name);
			}
			sb.append(txt_3 + "\n");
		}
		if (sp.getBoolean(AppConfig.KEY_EDIT_SHOW_4, true)) {
			String txt_4 = sp.getString(AppConfig.KEY_EDIT_TEXT_4, "");
			if (StringUtils.isNull(txt_4)) {
				txt_4 = res.getString(R.string.print_company_urls);
			}
			sb.append(txt_4 + "\n");
		}
		if (sp.getBoolean(AppConfig.KEY_EDIT_SHOW_5, true)) {
			String txt_5 = sp.getString(AppConfig.KEY_EDIT_TEXT_5, "");
			if (StringUtils.isNull(txt_5)) {
				txt_5 = res.getString(R.string.print_company_adds);
			}
			sb.append(txt_5 + "\n");
		}
		if (sp.getBoolean(AppConfig.KEY_EDIT_SHOW_6, true)) {
			String txt_6 = sp.getString(AppConfig.KEY_EDIT_TEXT_6, "");
			if (StringUtils.isNull(txt_6)) {
				txt_6 = res.getString(R.string.print_company_call);
			}
			sb.append(txt_6 + "\n");
		}
		if (sp.getBoolean(AppConfig.KEY_EDIT_SHOW_7, true)) {
			String txt_7 = sp.getString(AppConfig.KEY_EDIT_TEXT_7, "");
			if (StringUtils.isNull(txt_7)) {
				txt_7 = res.getString(R.string.print_company_line);
			}
			sb.append(txt_7 + "\n");
		}
		if (is58mm) {
			sb.append("================================\n");
		} else {
			sb.append("================================================\n");
		}
		mySendBytes(getBytes(sb.toString()));

		mySendBytes(ES_SITE_CT); //字体居中

		mySendBytes(getBytes(res.getString(R.string.print_thanks) + "\n"));

		// 打印条形码
		if (sp.getBoolean(AppConfig.KEY_EDIT_SHOW_8, false)) {
			mySendBytes(getBytes(" \n"));
			printBarcode128(printSn);
		}

		mySendBytes(ES_SITE_LF); //字体居左

		mySendBytes(getBytes(" \n\n\n"));

		mySendBytes(GS_MODE_CU); //切纸指令
		mySendBytes(FS_NORM); //取消汉字模式
	}

	/**
	 * 打印表格内容
	 */
	public static void printTable(Resources res, InvoiceEntity data) {
		if (res == null || data == null || data.getShopLists() == null) return;
		String column = res.getString(R.string.print_note_title);
		Table table;
		if (is58mm) {
			table = new Table(column, ";", new int[] { 14, 6, 6, 6 });
		} else {
			table = new Table(column, ";", new int[] { 18, 10, 10, 12 });
		}
		List<ShopEntity> lists = data.getShopLists();
		ShopEntity shopEn;
		for (int i = 0; i < lists.size(); i++) {
			shopEn = lists.get(i);
			if (shopEn != null) {
				table.addRow("" + shopEn.getName()+ ";" + shopEn.getPrice()+ ";" + shopEn.getNum()+ ";" + shopEn.getSubtotal());
			}
		}
		table.addRow(res.getString(R.string.print_total_price) + "; ; ;" + data.getTotalPrice());
		mySendBytes(getBytes(table.getTableText()));
	}

	/**
	 * 打印条形码
	 */
	public static void printBarcode128(String strbarcode) {
		byte[] tmpByte = getBytes(strbarcode);
		byte[] command = new byte[tmpByte.length + 12];
		int pos = 0;
		// 设置条码宽度 GS w n (2<=n<=3)
		command[pos++] = GS;
		command[pos++] = 'w';
		command[pos++] = 2;
		// 设置条码高度 GS h n (1<=n<=255)
		command[pos++] = GS;
		command[pos++] = 'h';
		command[pos++] = 100;
		// 设置条码注释 GS H n (1<=n<=2)
		//command[pos++] = GS;
		//command[pos++] = 'H';
		//command[pos++] = 2;
		// 打印条码内容
		command[pos++] = GS;
		command[pos++] = 'k';
		command[pos++] = 72; //72 条形码系统CODE128
		command[pos++] = (byte)strbarcode.length(); //条码字符个数
		for(int j = 0; j < tmpByte.length; ++j) {
			command[pos++] = tmpByte[j]; //条码内容
		}
		mySendBytes(command);
	}

	public static byte[] getBytes(String content) {
		byte[] data;
		try {
			data = content.getBytes("gbk");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			data = content.getBytes();
		}
		return data;
	}

	private static int mySendBytes(byte[] srcData) {
		switch (printerType) {
			case AppConfig.PRINTER_TYPE_USB:
				if (AppApplication.mPrinter != null) {
					return AppApplication.mPrinter.sendBytesData(srcData);
				} else {
					return -1;
				}
			case AppConfig.PRINTER_TYPE_SERIAL:
				return serialSendBytes(srcData);
			default:
				return -1;
		}
	}

	private static int serialSendBytes(byte[] srcData) {
		int packetSize = 512;
		int num = srcData.length / packetSize;
		byte[] pack = new byte[packetSize];
		byte[] temp = new byte[srcData.length - packetSize * num];
		if(num >= 1) {
			for(int i = 0; i <= num - 1; ++i) {
				System.arraycopy(srcData, i * packetSize, pack, 0, packetSize);
				SerialPortUtils.write(pack);
				try {
					Thread.sleep(20L);
				} catch (InterruptedException var8) {
					var8.printStackTrace();
				}
			}
			System.arraycopy(srcData, num * packetSize, temp, 0, srcData.length - packetSize * num);
			if(SerialPortUtils.write(temp) < 0) {
				return -3;
			}
		} else if(SerialPortUtils.write(srcData) < 0) {
			return -3;
		}
		return srcData.length;
	}

}
