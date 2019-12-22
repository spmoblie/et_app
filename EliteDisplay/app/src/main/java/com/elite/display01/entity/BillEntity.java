package com.elite.display01.entity;

import java.io.Serializable;

/**
 * Created by Beck on 2018/1/24.
 */
public class BillEntity implements Serializable{

    private static final long serialVersionUID = 1L;

    private int billId;

    private String billName;

    private String billPrice;

    private int billNum;

    private int priceTotal;

    public BillEntity() {

    }

    public BillEntity(int billId, String billName, String billPrice, int billNum, int priceTotal) {
        this.billId = billId;
        this.billName = billName;
        this.billPrice = billPrice;
        this.billNum = billNum;
        this.priceTotal = priceTotal;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public String getBillName() {
        return billName;
    }

    public void setBillName(String billName) {
        this.billName = billName;
    }

    public String getBillPrice() {
        return billPrice;
    }

    public void setBillPrice(String billPrice) {
        this.billPrice = billPrice;
    }

    public int getBillNum() {
        return billNum;
    }

    public void setBillNum(int billNum) {
        this.billNum = billNum;
    }

    public int getPriceTotal() {
        return priceTotal;
    }

    public void setPriceTotal(int priceTotal) {
        this.priceTotal = priceTotal;
    }

    @Override
    public String toString() {
        return "BillEntity{" +
                "billId=" + billId +
                ", billName='" + billName + '\'' +
                ", billPrice='" + billPrice + '\'' +
                ", billNum=" + billNum +
                ", priceTotal=" + priceTotal +
                '}';
    }
}
