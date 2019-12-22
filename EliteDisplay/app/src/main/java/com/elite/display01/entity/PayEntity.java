package com.elite.display01.entity;

/**
 * Created by user on 05/03/18.
 */

public class PayEntity {

    public String code;

    public String message;

    public String orderNo; //订单号

    public String respMsg; //二维码

    public PayEntity(String code, String message, String orderNo, String respMsg) {
        this.code = code;
        this.message = message;
        this.orderNo = orderNo;
        this.respMsg = respMsg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getRespMsg() {
        return respMsg;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }

    @Override
    public String toString() {
        return "PayEntity{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", respMsg='" + respMsg + '\'' +
                '}';
    }

}
