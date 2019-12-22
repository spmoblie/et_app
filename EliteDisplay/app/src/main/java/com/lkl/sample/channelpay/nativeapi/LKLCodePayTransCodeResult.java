package com.lkl.sample.channelpay.nativeapi;

/**
 * Created by huangyong on 2017/11/29.
 */

public class LKLCodePayTransCodeResult {

    public String getProcessCode() {return processCode_;}
    public String getAmount() {return amount_;}
    public String getTransNo() {return transNo_;}
    public String getOriTransNo() {return oriTransNo_;}
    public String getBatchNo() {return batchNo_;}
    public String getRefNo() {return refNo_;}
    public String getOrderNo() {return orderNo_;}
    public String getTradeOrderNo() {return tradeOrderNo_;}
    public String getTransTime() {return transTime_;}
    public String getTransDate() {return transDate_;}
    public String getTerminalId() {return terminalId_;}
    public String getMerchantId() {return merchantId_;}
    public String getMerchantName() {return merchantName_;}
    public String getChannel() {return channel_;}

    public void setProcessCode(String processCode) {processCode_ = processCode;}
    public void setAmount(String amount) {amount_ = amount;}
    public void setTransNo(String transNo) {transNo_ = transNo;}
    public void setOriTransNo(String oriTransNo) {oriTransNo_ = oriTransNo;}
    public void setBatchNo(String batchNo) {batchNo_ = batchNo;}
    public void setRefNo(String refNo) {refNo_ = refNo;}
    public void setOrderNo(String orderNo) {orderNo_ = orderNo;}
    public void setTradeOrderNo(String tradeOrderNo) {tradeOrderNo_ = tradeOrderNo;}
    public void setTransTime(String transTime) {transTime_ = transTime;}
    public void setTransDate(String transDate) {transDate_ = transDate;}
    public void setTerminalId(String terminalId) {terminalId_ = terminalId;}
    public void setMerchantId(String merchantId) {merchantId_ = merchantId;}
    public void setMerchantName(String merchantName) {merchantName_ = merchantName;}
    public void setChannel(String channel) {channel_ = channel;}

    public String dump() {
        return "processCode=[" + processCode_ + "]\n" +
                "amount_=[" + amount_ + "]\n" +
                "transNo_=[" + transNo_ + "]\n" +
                "oriTransNo_=[" + oriTransNo_ + "]\n" +
                "batchNo_=[" + batchNo_ + "]\n" +
                "refNo_=[" + refNo_ + "]\n" +
                "orderNo_=[" + orderNo_ + "]\n" +
                "tradeOrderNo_=[" + tradeOrderNo_ + "]\n" +
                "transTime_=[" + transTime_ + "]\n" +
                "transDate_=[" + transDate_ + "]\n" +
                "terminalId_=[" + terminalId_ + "]\n" +
                "merchantId_=[" + merchantId_ + "]\n" +
                "merchantName_=[" + merchantName_ + "]\n" +
                "channel_=[" + channel_ + "]\n";
    }

    //members
    public String processCode_ = "";
    public String amount_ = "";
    public String transNo_ = "";
    public String oriTransNo_ = "";
    public String batchNo_ = "";
    public String refNo_ = "";
    public String orderNo_ = "";
    public String tradeOrderNo_ = "";
    public String transTime_ = "";
    public String transDate_ = "";
    public String terminalId_ = "";
    public String merchantId_ = "";
    public String merchantName_ = "";
    public String channel_ = "";
}
