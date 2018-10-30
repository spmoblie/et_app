package com.elite.selfhelp.entity;

/**
 * 商品列表数据实体类
 * Created by Beck on 2018/1/24.
 */
public class ShopEntity extends BaseEntity{

    private static final long serialVersionUID = 1L;

    private String sku; //商品编码

    private String upc; //条形码

    private String name; //商品名称

    private int num; //商品数量

    private double price; //销售价格

    private double subtotal; //价格小计


    public ShopEntity() {

    }

    public ShopEntity(String sku, String upc, String name, int num, double price, double subtotal) {
        this.sku = sku;
        this.upc = upc;
        this.name = name;
        this.num = num;
        this.price = price;
        this.subtotal = subtotal;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    @Override
    public String toString() {
        return "ShopEntity{" +
                "sku='" + sku + '\'' +
                ", upc='" + upc + '\'' +
                ", name='" + name + '\'' +
                ", num=" + num +
                ", price=" + price +
                ", subtotal=" + subtotal +
                '}';
    }
}
