package com.elite.display01.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class BaseReq implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7712941588385745273L;

	private Long userId; // 数据库商户id
	private String saru; //商户号
	private String merchantId;
	private String key;
	private String ordMsgId; // 订单号
	private String transAmt; // 交易金额，单位为分
	private String type; // 交易类型 "1"=微信 ,2=支付宝 3 无卡 4荷包
	private String settlement; // 结算方式  0 D0 1 T1 3 D1 4 T0
	private String ipAddress; // ip 地址
	private String address;// 交易地址
	private Date timeStamp; // 交易时间
	private BigDecimal rate; // 费率
	private BigDecimal tx; // 提现费
	private String channel; // 通道Id
	//
	private String requestIp;//商户服务器Ip地址;
	private String partner; //商户标识;
	
	

	public String getRequestIp() {
		return requestIp;
	}

	public void setRequestIp(String requestIp) {
		this.requestIp = requestIp;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String getSaru() {
		return saru;
	}

	public void setSaru(String saru) {
		this.saru = saru;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getSettlement() {
		return settlement;
	}

	public void setSettlement(String settlement) {
		this.settlement = settlement;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public BigDecimal getTx() {
		return tx;
	}

	public void setTx(BigDecimal tx) {
		this.tx = tx;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getOrdMsgId() {
		return ordMsgId;
	}

	public void setOrdMsgId(String ordMsgId) {
		this.ordMsgId = ordMsgId;
	}

	public String getTransAmt() {
		return transAmt;
	}

	public void setTransAmt(String transAmt) {
		this.transAmt = transAmt;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}
	
	public static void main(String[] args) {
		Long a=2L|4L|1L;
		System.out.println(a);
	}

}
