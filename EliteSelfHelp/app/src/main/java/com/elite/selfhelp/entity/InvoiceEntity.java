package com.elite.selfhelp.entity;

import java.util.List;

/**
 * 打印票据数据实体类
 * Created by Beck on 2018/4/28.
 */
public class InvoiceEntity extends BaseEntity{

    private static final long serialVersionUID = 1L;

    private String totalPrice; //总计

    private String printSn; //单据号

    private String createTime; //创建时间

    private List<ShopEntity> shopLists; //商品集合

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPrintSn() {
        return printSn;
    }

    public void setPrintSn(String printSn) {
        this.printSn = printSn;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public List<ShopEntity> getShopLists() {
        return shopLists;
    }

    public void setShopLists(List<ShopEntity> shopLists) {
        this.shopLists = shopLists;
    }

    @Override
    public String toString() {
        return "InvoiceEntity{" +
                "totalPrice='" + totalPrice + '\'' +
                ", printSn='" + printSn + '\'' +
                ", createTime='" + createTime + '\'' +
                ", shopLists=" + shopLists +
                '}';
    }
}
