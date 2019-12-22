package com.elite.display01.utils;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore.Images.ImageColumns;

import com.elite.display01.AppApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class FileManager {
	
	/**
	 * 写入数据（String）
	 * 
	 * @param filePath 文本路径
	 * @param writeStr 文本对象
	 */
	public static void writeFileSaveString(String filePath, String writeStr) {
		if (StringUtil.isNull(filePath) || StringUtil.isNull(writeStr)) return;
		FileOutputStream fos = null;
		try {
			checkFilePath(filePath);
			fos = new FileOutputStream(filePath);
			byte[] bytes = writeStr.getBytes();
			fos.write(bytes);
		} catch (Exception e) {
			ExceptionUtil.handle(e);
		} finally {
        	try {
        		if (fos != null) {
					fos.close();
				}
			} catch (Exception e) {
				ExceptionUtil.handle(e);
			}
        }
	}

	/**
	 * 读取数据（String）
	 * 
	 * @param filePath 文本路径
	 */
	public static String readFileSaveString(String filePath) {
		FileInputStream fin = null;
		String resu = "";
		try {
			File file = new File(filePath);
			if (file.exists()) {
				fin = new FileInputStream(file);
				int length = fin.available();
				byte[] buffer = new byte[length];
				fin.read(buffer);
				resu = new String(buffer, "UTF-8");
			}
		} catch (Exception e) {
			ExceptionUtil.handle(e);
		} finally {
        	try {
        		if (fin != null) {
        			fin.close();
				}
			} catch (Exception e) {
				ExceptionUtil.handle(e);
			}
        }
		return resu;
	}
	
	/**
	 * 写入数据（Object）
	 * 
	 * @param filePath 文件路径
	 * @param obj 文件对象
	 */
    public static void writeFileSaveObject(String filePath, Object obj) {
		if (StringUtil.isNull(filePath) || obj == null) return;
		ObjectOutputStream oos = null;
    	FileOutputStream fos = null;
        try {
			checkFilePath(filePath);
			fos = new FileOutputStream(new File(filePath));
			oos = new ObjectOutputStream(fos);
			oos.writeObject(obj);
			oos.flush();
        } catch (IOException e) {
			ExceptionUtil.handle(e);
        }finally{
        	try {
        		if (fos != null) {
        			fos.close();
				}
        		if (oos != null) {
					oos.close();
				}
			} catch (IOException e) {
				ExceptionUtil.handle(e);
			}
        }
    }
    
    /**
	 * 读取数据（Object）
	 * 
	 * @param filePath 文件路径
	 */
    public static Object readFileSaveObject(String filePath) {
        Object temp = null;
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
        	File file = new  File(filePath);
			if (file.exists()) {
				fis = new FileInputStream(file);
				ois = new ObjectInputStream(fis);
				temp = ois.readObject();
			}
        } catch (Exception e) {
			ExceptionUtil.handle(e);
        }finally{
        	try {
        		if (fis != null) {
					fis.close();
				}
        		if (ois != null) {
					ois.close();
				}
			} catch (Exception e) {
				ExceptionUtil.handle(e);
			}
        }
        return temp;
    }
	
	/**
	 * 校验文件路径，不存在则创建
	 */
	public static void checkFilePath(String path) throws IOException {
		File file = new File(path);
		//判定文件所在的目录是否存在，不存在则创建
		File parentFile = file.getParentFile();
		if(parentFile != null && !parentFile.exists()){
			parentFile.mkdirs();
		}
		//判断文件是否存在,不存在则创建
		if(!file.exists()){
			file.createNewFile();
		}
	}

	/**
	 * 校验文件是否存在
	 */
	public static boolean checkFileExists(String path) {
		if (StringUtil.isNull(path)) return false;
		File file = new File(path);
		//判定文件所在的目录是否存在
		File parentFile = file.getParentFile();
		if(parentFile == null || !parentFile.exists()){
			return false;
		}
		//判断文件是否存在
		if(!file.exists()){
			return false;
		}
		return true;
	}

	/**
	 * 获取文件夹大小
	 * 
	 * @param file File实例
	 * @return long 单位为b
	 * @throws Exception
	 */
	public static long getFolderSize(File file) throws Exception {
		long size = 0;
		if (file == null) {
			return size;
		}
		File[] fileList = file.listFiles();
		if (fileList == null) {
			return size;
		}
		for (int i = 0; i < fileList.length; i++) {
			if (fileList[i].isDirectory()) {
				size = size + getFolderSize(fileList[i]);
			} else {
				size = size + fileList[i].length();
			}
		}
		return size;
	}

	/**
	 * 获取文件夹文件数目
	 */
	public static int getFolderNum(File file) throws Exception {
		if (file == null) {
			return 0;
		}
		File[] fileList = file.listFiles();
		if (fileList == null) {
			return 0;
		}
		return fileList.length;
	}

	/**
	 * 删除指定目录下文件及目录
	 */
	public static void deleteFolderFile(File file) throws IOException {
		if (file == null) {
			return;
		}
		if(file.isFile()){ //文件
			file.delete();
			return;
		}
		if(file.isDirectory()){ //文件夹
			File[] childFile = file.listFiles();
			if(childFile == null || childFile.length == 0){
				file.delete();
				return;
			}
			for(File f : childFile){
				deleteFolderFile(f); //递归
			}
			file.delete();
		}
	}
	
	/**
	 * 从给定的Uri返回文件的绝对路径
	 *
	 * @param uri
	 * @return the file path or null
	 */
	public static String getRealFilePath(final Uri uri ) {
	    if ( null == uri ) return null;
	    final String scheme = uri.getScheme();
	    String data = null;
	    if ( scheme == null )
	        data = uri.getPath();
	    else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
	        data = uri.getPath();
	    } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
	        Cursor cursor = AppApplication.getInstance().getApplicationContext()
					.getContentResolver().query( uri, new String[] { ImageColumns.DATA }, null, null, null );
	        if ( null != cursor ) {
	            if ( cursor.moveToFirst() ) {
	                int index = cursor.getColumnIndex( ImageColumns.DATA );
	                if ( index > -1 ) {
	                    data = cursor.getString( index );
	                }
	            }
	            cursor.close();
	        }
	    }
	    return data;
	}

	/**
	 * 使用当前时间生成文件名
	 * @param fileType 文件类型(.jpg / .txt / ...)
	 */
	public static String getFileName(String fileType){
	    SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
        String dateTime = s.format(new Date());
        String imgFileName = dateTime + fileType;
        return imgFileName;
	}

	/**
	 * 读取指定文件中的内容
	 */
	public static String getStringFromFile(File file) throws Exception {
	    FileInputStream fis = new FileInputStream(file);
	    BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
	    StringBuilder sb = new StringBuilder();
	    String line;
	    while ((line = reader.readLine()) != null) {
	      sb.append(line).append("\n");
	    }
	    reader.close();
	    fis.close();        
	    return sb.toString();
	}

	/**
	 * 读取Asset中的文件内容
	 */
	public static String loadJSONFromAsset(String filename) {
		String json = null;
		InputStream is = null;
		try {
			is = AppApplication.getInstance().getApplicationContext().getAssets().open(filename);
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			json = new String(buffer, "UTF-8");
		} catch (Exception e) {
			ExceptionUtil.handle(e);
			return null;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (Exception e) {
				ExceptionUtil.handle(e);
			}
		}
		return json;
	}
    
	/**
	 * 读取Asset中的图片
	 */
	public static Bitmap getBitmapFromAssets(String filename){
		InputStream is = null;
		try {
			is = AppApplication.getInstance().getApplicationContext().getAssets().open(filename + ".png");
			Bitmap bitmap = BitmapFactory.decodeStream(is);   
			return bitmap;
		} catch (IOException e) {
			ExceptionUtil.handle(e);
			return null;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (Exception e) {
				ExceptionUtil.handle(e);
			}
		}
	}

	/**
	 * 获取文件类型
	 *
	 * @param filePath
	 * @return
	 */
	public static String getFileType(String filePath) {
		HashMap<String, String> mFileTypes = new HashMap<String, String>();
		mFileTypes.put("FFD8FF", "jpg");
		mFileTypes.put("89504E47", "png");
		mFileTypes.put("47494638", "gif");
		mFileTypes.put("49492A00", "tif");
		mFileTypes.put("424D", "bmp");

//		mFileTypes.put("41433130", "dwg"); //CAD
//		mFileTypes.put("38425053", "psd");
//		mFileTypes.put("7B5C727466", "rtf"); //日记本
//		mFileTypes.put("3C3F786D6C", "xml");
//		mFileTypes.put("68746D6C3E", "html");
//		mFileTypes.put("44656C69766572792D646174653A", "eml"); //邮件
//		mFileTypes.put("D0CF11E0", "doc");
//		mFileTypes.put("5374616E64617264204A", "mdb");
//		mFileTypes.put("252150532D41646F6265", "ps");
//		mFileTypes.put("255044462D312E", "pdf");
//		mFileTypes.put("504B0304", "zip");
//		mFileTypes.put("52617221", "rar");
//		mFileTypes.put("57415645", "wav");
//		mFileTypes.put("41564920", "avi");
//		mFileTypes.put("2E524D46", "rm");
//		mFileTypes.put("000001BA", "mpg");
//		mFileTypes.put("000001B3", "mpg");
//		mFileTypes.put("6D6F6F76", "mov");
//		mFileTypes.put("3026B2758E66CF11", "asf");
//		mFileTypes.put("4D546864", "mid");
//		mFileTypes.put("1F8B08", "gz");
		return mFileTypes.get(getFileHeader(filePath));
	}

	/**
	 * 判定是否为jpg或png格式的图片
	 *
	 * @param filePath
	 * @return
	 */
	public static boolean isImage(String filePath) {
		HashMap<String, String> mFileTypes = new HashMap<String, String>();
		mFileTypes.put("FFD8FF", "jpg");
		mFileTypes.put("89504E47", "png");
		return mFileTypes.containsKey(getFileHeader(filePath));
	}

	/**
	 * 获取文件头信息
	 *
	 * @param filePath
	 * @return
	 */
	public static String getFileHeader(String filePath) {
		FileInputStream is = null;
		String value = null;
		try {
			is = new FileInputStream(filePath);
			byte[] b = new byte[3];
			is.read(b, 0, b.length);
			value = bytesToHexString(b);
		} catch (Exception e) {
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
		}
		return value;
	}

	/**
	 * 将byte字节转换为十六进制字符串
	 *
	 * @param src
	 * @return
	 */
	private static String bytesToHexString(byte[] src) {
		StringBuilder builder = new StringBuilder();
		if (src == null || src.length <= 0) {
			return null;
		}
		String hv;
		for (int i = 0; i < src.length; i++) {
			hv = Integer.toHexString(src[i] & 0xFF).toUpperCase();
			if (hv.length() < 2) {
				builder.append(0);
			}
			builder.append(hv);
		}
		return builder.toString();
	}

}
