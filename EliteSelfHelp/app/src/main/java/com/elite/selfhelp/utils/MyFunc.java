package com.elite.selfhelp.utils;

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
    	return (byte)Integer.parseInt(inHex,16);
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
	public static String ByteArrToHex(byte[] inBytArr,int offset,int byteCount) {
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

	//hex字符串转ASCII码
	public static String hexToString(String hex){
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

	/**
	 * 字符串转十六进制
	 * @param str 待转换的ASCII字符串
	 * @return String 每个Byte之间空格分隔，如: [61 6C 6B]
	 */
	public static String str2HexStr(String str) {

		char[] chars = "0123456789ABCDEF".toCharArray();
		StringBuilder sb = new StringBuilder("");
		byte[] bs = str.getBytes();
		int bit;

		for (int i = 0; i < bs.length; i++)
		{
			bit = (bs[i] & 0x0f0) >> 4;
			sb.append(chars[bit]);
			bit = bs[i] & 0x0f;
			sb.append(chars[bit]);
			sb.append(' ');
		}
		return sb.toString().trim();
	}

	/**
	 * 十六进制转字符串
	 * @param hexStr Byte字符串(Byte之间无分隔符 如:[616C6B])
	 * @return String 对应的字符串
	 */
	public static String hexStr2Str(String hexStr) {
		String str = "0123456789ABCDEF";
		char[] hexs = hexStr.toCharArray();
		byte[] bytes = new byte[hexStr.length() / 2];
		int n;

		for (int i = 0; i < bytes.length; i++)
		{
			n = str.indexOf(hexs[2 * i]) * 16;
			n += str.indexOf(hexs[2 * i + 1]);
			bytes[i] = (byte) (n & 0xff);
		}
		return new String(bytes);
	}

	/**
	 * bytes转十六进制
	 * @param b byte数组
	 * @return String 每个Byte值之间空格分隔
	 */
	public static String byte2HexStr(byte[] b) {
		String stmp="";
		StringBuilder sb = new StringBuilder("");
		for (int n=0;n<b.length;n++)
		{
			stmp = Integer.toHexString(b[n] & 0xFF);
			sb.append((stmp.length()==1)? "0"+stmp : stmp);
			sb.append(" ");
		}
		return sb.toString().toUpperCase().trim();
	}

	/**
	 * 十六进制转bytes
	 * @param src Byte字符串，每个Byte之间没有分隔符
	 * @return byte[]
	 */
	public static byte[] hexStr2Bytes(String src) {
		int m=0,n=0;
		int l=src.length()/2;
		System.out.println(l);
		byte[] ret = new byte[l];
		for (int i = 0; i < l; i++)
		{
			m=i*2+1;
			n=m+1;
			ret[i] = Byte.decode("0x" + src.substring(i*2, m) + src.substring(m,n));
		}
		return ret;
	}

	/**
	 * String的字符串转换成unicode的String
	 * @param strText 全角字符串
	 * @return String 每个unicode之间无分隔符
	 * @throws Exception
	 */
	public static String strToUnicode(String strText) throws Exception {
		char c;
		StringBuilder str = new StringBuilder();
		int intAsc;
		String strHex;
		for (int i = 0; i < strText.length(); i++)
		{
			c = strText.charAt(i);
			intAsc = (int) c;
			strHex = Integer.toHexString(intAsc);
			if (intAsc > 128)
				str.append("\\u" + strHex);
			else // 低位在前面补00
				str.append("\\u00" + strHex);
		}
		return str.toString();
	}

	/**
	 * unicode的String转换成String的字符串
	 * @param hex 16进制值字符串 （一个unicode为2byte）
	 * @return String 全角字符串
	 */
	public static String unicodeToString(String hex) {
		int t = hex.length() / 6;
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < t; i++)
		{
			String s = hex.substring(i * 6, (i + 1) * 6);
			// 高位需要补上00再转
			String s1 = s.substring(2, 4) + "00";
			// 低位直接转
			String s2 = s.substring(4);
			// 将16进制的string转为int
			int n = Integer.valueOf(s1, 16) + Integer.valueOf(s2, 16);
			// 将int转换为字符
			char[] chars = Character.toChars(n);
			str.append(new String(chars));
		}
		return str.toString();
	}

}