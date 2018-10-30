package com.elite.selfhelp.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.hardware.usb.UsbDevice;
import android.os.Build;

@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
@SuppressLint("NewApi")
public class PrinterUtils {

	public static int PrinterType = 0;
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	@SuppressLint("NewApi")
	public static boolean isUsbPrinter(UsbDevice device) {

		LogUtils.i("Printer", "DeviceInfo: " + device.getVendorId() + " , " + device.getProductId());
		// 保存设备VID和PID
		int VendorID = device.getVendorId();
		int ProductID = device.getProductId();
		
		// 保存匹配到的设备
		// 17224 亿利达2058，亿利达4080
		// 1157 POS58III
		// 6790 POS58III
		// 4070 33054 容大RP58A citizen CT-P236
		// 1155 斯普瑞特打印机
		// 1363 润法打印模组 
		// VID 0x0416,1046,PID 0x5011,20497
		if (VendorID == 17224
				|| VendorID == 1157
				|| VendorID == 6790
				|| (VendorID == 1155 && ProductID == 22592)
				|| (VendorID == 1363 && ProductID == 22304)
				|| (VendorID == 1137 && ProductID == 80)
				|| (VendorID == 34918 && ProductID == 256)
				|| (VendorID == 4070 && ProductID == 33054)
				|| (VendorID == 1046 && ProductID == 20497)) {
			LogUtils.i("Printer", "Is Printer:" + device.getVendorId() + " , " + device.getProductId());
			return true;
		} else {
			return false;
		}

	}

}
