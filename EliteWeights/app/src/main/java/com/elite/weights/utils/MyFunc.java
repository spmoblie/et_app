package com.elite.weights.utils;

/**
 * @author benjaminwan
 *数据转换工具
 */
public class MyFunc {

	// 判断奇数或偶数，位运算，最后一位是1则为奇数，为0是偶数
    public static int isOdd(int num)
	{
		return num & 0x1;
	}

	//Hex字符串转int
	public static int HexToInt(String inHex) {
    	return Integer.parseInt(inHex, 16);
    }

	//Hex字符串转byte
	public static byte HexToByte(String inHex) {
    	return (byte) Integer.parseInt(inHex,16);
    }

	//1字节转2个Hex字符
    public static String Byte2Hex(Byte inByte) {
    	return String.format("%02x", inByte).toUpperCase();
    }

	//字节数组转hex字符串
	public static String ByteArrToHex(byte[] inBytArr) {
		StringBuilder strBuilder = new StringBuilder();
		int j = inBytArr.length;
		for (int i = 0; i < j; i++)
		{
			strBuilder.append(Byte2Hex(inBytArr[i]));
			strBuilder.append(" ");
		}
		return strBuilder.toString(); 
	}

	//字节数组转hex字符串，可选长度
	public static String ByteArrToHex(byte[] inBytArr, int offset, int byteCount) {
    	StringBuilder strBuilder=new StringBuilder();
		int j = byteCount;
		for (int i = offset; i < j; i++)
		{
			strBuilder.append(Byte2Hex(inBytArr[i]));
		}
		return strBuilder.toString();
	}

	//hex字符串转字节数组
	public static byte[] HexToByteArr(String inHex) {
		int hexlen = inHex.length();
		byte[] result;
		if (isOdd(hexlen)==1) {//奇数
			hexlen++;
			result = new byte[(hexlen/2)];
			inHex="0"+inHex;
		}else {//偶数
			result = new byte[(hexlen/2)];
		}
	    int j=0;
		for (int i = 0; i < hexlen; i+=2)
		{
			result[j]=HexToByte(inHex.substring(i,i+2));
			j++;
		}
	    return result; 
	}

	//字符串转字节数组
	public static byte[] strToByteArray(String str) {
		if (str == null) {
			return null;
		}
		return str.getBytes();
	}

	// 字节数组转字符串
	public static String byteArrayToStr(byte[] byteArray) {
		if (byteArray == null) {
			return null;
		}
		return new String(byteArray);
	}

	//Hex字符串转ASCII码
	public static String HexToString(String hex){
		StringBuilder sb = new StringBuilder();
		for( int i=0; i<hex.length()-1; i+=2 ){
			//grab the hex in pairs
			String output = hex.substring(i, (i + 2));
			//convert hex to decimal
			int decimal = Integer.parseInt(output, 16);
			//convert the decimal to character
			sb.append((char)decimal);
		}
		return sb.toString();
	}

}