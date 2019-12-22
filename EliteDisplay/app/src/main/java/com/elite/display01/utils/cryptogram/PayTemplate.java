package com.elite.display01.utils.cryptogram;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.elite.display01.config.AppConfig;
import com.elite.display01.entity.SmReq;
import com.elite.display01.utils.LogUtil;
import com.elite.display01.utils.cryptogram.CryptogramUtils;

import org.apache.commons.codec1.binary.Base64;
import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;


public abstract class PayTemplate {

	public static HashMap<String, String> getPayParameters(SmReq payForm, HashMap<String, String> hashMap) throws Exception {
		return Request(payForm, hashMap);
	}

	/**
	 * 加密
	 * @param payForm
	 * @param hashMap
	 * @return
	 * @throws Exception
	 */
	private static HashMap<String, String> Request(SmReq payForm,HashMap<String, String> hashMap) throws Exception {
		// 客户生成的私钥
		PrivateKey PriKey = CryptogramUtils.getRSAPrivateKeyByFileSuffix(
				AppConfig.PRI_KEY, "pem", null, "RSA");
		// 平台提供的公钥
		PublicKey PubKey = CryptogramUtils.getRSAPublicKeyByFileSuffix(
				AppConfig.PUB_KEY, "pem", "RSA");

		byte[] plainBytes = JSON.toJSONString(payForm).getBytes();

		String keyStr = CryptogramUtils.generateLenString(16);

		byte[] keyBytes = keyStr.getBytes("utf-8");
		// 主要key1
		String encrtptKey = new String(Base64.encodeBase64(CryptogramUtils.RSAEncrypt(keyBytes, PubKey, 2048, 11, "RSA/ECB/PKCS1Padding")), "utf-8");

		LogUtil.i("PayTemplate","ASDkey:" + keyStr + "加密后" + encrtptKey);

		// 加密报文2
		String encryptData = new String(Base64.encodeBase64((CryptogramUtils.AESEncrypt(plainBytes, keyBytes, "AES", "AES/ECB/PKCS5Padding", null))), "utf-8");

		// 加密报文3
		String signData = new String(Base64.encodeBase64(CryptogramUtils.digitalSign(plainBytes, PriKey, "SHA1WithRSA")), "utf-8");

		hashMap.put("Key", encrtptKey);
		hashMap.put("Data", encryptData);
		hashMap.put("signData", signData);
		hashMap.put("reqMsgId", payForm.getOrdMsgId());

		return hashMap;
	}

	/**
	 * 解密
	 * @param pp
	 * @return
	 */
	public static String decode(String pp) {

		PrivateKey PriKey = CryptogramUtils.getRSAPrivateKeyByFileSuffix(
				"pkcs8_rsa_private_key_2048.pem", "pem", null, "RSA");
		String encrtptKey;
		String encryptData;
		
		try {
			JSONObject json = JSON.parseObject(pp);
			encrtptKey = json.getString("respEncrtptKey");
			encryptData = json.getString("respEncrtptDate");
			if (StringUtils.isBlank(encrtptKey)) {
				return encryptData;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return pp;
		}

		// 解密密码
		byte[] key = Base64.decodeBase64(encrtptKey);
		byte[] aesKey = CryptogramUtils.RSADecrypt(key, PriKey, 2048, 11, "RSA/ECB/PKCS1Padding");
		byte[] Date = Base64.decodeBase64(encryptData);
		// 解密报文
		byte[] aesDate = CryptogramUtils.AESDecrypt(Date, aesKey, "AES", "AES/ECB/PKCS5Padding", null);
		String string = null;
		try {
			string = new String(aesDate, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return string;
	}

}
