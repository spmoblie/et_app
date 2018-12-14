package com.elite.inventory.entity;

import java.util.List;

/**
 * 订单数据实体类
 */
public class OrderEntity extends BaseEntity{

    private static final long serialVersionUID = 1L;

    private String orderNo; //订单号

    private String currency; //当前货币

    private String priceTotal; //订单金额

    private String createTime; //创建时间

    private String printSn; //小票单据号

    private int printStatus; //打印小票状态（1：已打印/0：未打印）

    private int shopNum; //商品总数量

    private String shopTag; //商品集合入库标识

    private List<BillEntity> shopLists; //商品集合


    public OrderEntity() {

    }

    public OrderEntity(String orderNo, String currency, String priceTotal, String createTime
            , String printSn, int printStatus, int shopNum, String shopTag) {
        this.orderNo = orderNo;
        this.currency = currency;
        this.priceTotal = priceTotal;
        this.createTime = createTime;
        this.printSn = printSn;
        this.printStatus = printStatus;
        this.shopNum = shopNum;
        this.shopTag = shopTag;
    }

    public String getOrderNo() {

        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPriceTotal() {
        return priceTotal;
    }

    public void setPriceTotal(String priceTotal) {
        this.priceTotal = priceTotal;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPrintSn() {
        return printSn;
    }

    public void setPrintSn(String printSn) {
        this.printSn = printSn;
    }

    public int getPrintStatus() {
        return printStatus;
    }

    public void setPrintStatus(int printStatus) {
        this.printStatus = printStatus;
    }

    public int getShopNum() {
        return shopNum;
    }

    public void setShopNum(int shopNum) {
        this.shopNum = shopNum;
    }

    public String getShopTag() {
        return shopTag;
    }

    public void setShopTag(String shopTag) {
        this.shopTag = shopTag;
    }

    public List<BillEntity> getShopLists() {
        return shopLists;
    }

    public void setShopLists(List<BillEntity> shopLists) {
        this.shopLists = shopLists;
    }
}
