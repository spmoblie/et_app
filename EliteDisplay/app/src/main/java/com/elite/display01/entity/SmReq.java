package com.elite.display01.entity;

/**
 * @author lq
 *
 */
/**
 * @author lq
 *
 */
public class SmReq extends BaseReq {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2996964633935809621L;

	private String sn; // 台卡编号

	private String isJF; // 是否积分 "0" 非 "1"是

	private String mark; //// 1是公众支付 // 2条码支付 // 3 扫码支付

	private String unionid;

	private String openid;
	
	private String userAppid;

	private String AuthCode; // 授权码 条码的

	private String remarks; // 备注

	//private boolean flag;// 是否通过
	
	
	private String callBackurl;
	
	private String notifyUrl;
	
	
	
	

	public String getUserAppid() {
		return userAppid;
	}

	public void setUserAppid(String userAppid) {
		this.userAppid = userAppid;
	}

	public String getCallBackurl() {
		return callBackurl;
	}

	public void setCallBackurl(String callBackurl) {
		this.callBackurl = callBackurl;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getIsJF() {
		return isJF;
	}

	public void setIsJF(String isJF) {
		this.isJF = isJF;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getAuthCode() {
		return AuthCode;
	}

	public void setAuthCode(String authCode) {
		AuthCode = authCode;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	

}
