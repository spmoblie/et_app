package com.lkl.sample.channelpay.nativeapi;

import java.io.Serializable;

/**
 * Created by huangyong on 2017/11/30.
 */

public class LKLCodePayQRTransCodeResult extends LKLCodePayTransCodeResult implements Serializable {

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
                "qrCode_=[" + qrCode_ + "]\n" +
                "codeImage_=[" + codeImage_ + "]\n" +
                "expires_=[" + expires_ + "]\n" +
                "lklOrderNo_=[" + lklOrderNo_ + "]\n" +
                "retAmt=[" + retAmt_ + "]\n" +
                "weOrderNo=[" + weOrderNo_ + "]\n";
    }

    //members
    public String qrCode_ = "";
    public String codeImage_ = "";
    public String expires_ = "";
    public String lklOrderNo_ = "";
    public String retAmt_ = "";
    public String weOrderNo_ = "";

}
