package com.elite.display01.entity;

import java.text.SimpleDateFormat;

/**
 *串口数据
 */
public class ComBean {

	public byte[] bRec = null;
	public String sRecTime = "";
	public String sComPort = "";
	public int iRecSize = 0;

	public ComBean(byte[] buffer, int size) {
		//sComPort = sPort;
		iRecSize = size;
		bRec = new byte[size];
		for (int i = 0; i < size; i++)
		{
			bRec[i]=buffer[i];
		}
		SimpleDateFormat sDateFormat = new SimpleDateFormat("hh:mm:ss");
		sRecTime = sDateFormat.format(new java.util.Date());
	}

}