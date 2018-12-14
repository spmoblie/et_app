package com.elite.inventory.entity;


/**
 * 商品数据实体类
 */
public class GoodsEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String sku; //商品编码

    private int categoryId; //所属类别ID

    private String weight; //重量

    private String brand; //品牌

    private String model; //型号

    private String name; //商品名称

    private String productArea; //产地

    private String upc; //条形码

    private String saleUnit; //销售单位

    private double suggestPrice; //建议零售价

    private double price; //销售价格

    private double factory_price; //出厂价


    public GoodsEntity() {

    }

    public GoodsEntity(String sku, int categoryId, String weight
            , String brand, String model, String name
            , String productArea, String upc, String saleUnit
            , double suggestPrice, double price, double factory_price) {
        this.sku = sku;
        this.categoryId = categoryId;
        this.weight = weight;
        this.brand = brand;
        this.model = model;
        this.name = name;
        this.productArea = productArea;
        this.upc = upc;
        this.saleUnit = saleUnit;
        this.suggestPrice = suggestPrice;
        this.price = price;
        this.factory_price = factory_price;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductArea() {
        return productArea;
    }

    public void setProductArea(String productArea) {
        this.productArea = productArea;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public String getSaleUnit() {
        return saleUnit;
    }

    public void setSaleUnit(String saleUnit) {
        this.saleUnit = saleUnit;
    }

    public double getSuggestPrice() {
        return suggestPrice;
    }

    public void setSuggestPrice(double suggestPrice) {
        this.suggestPrice = suggestPrice;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getFactory_price() {
        return factory_price;
    }

    public void setFactory_price(double factory_price) {
        this.factory_price = factory_price;
    }

    @Override
    public String toString() {
        return "GoodsEntity{" +
                "sku='" + sku + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", weight='" + weight + '\'' +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", name='" + name + '\'' +
                ", productArea='" + productArea + '\'' +
                ", upc='" + upc + '\'' +
                ", saleUnit='" + saleUnit + '\'' +
                ", suggestPrice=" + suggestPrice +
                ", price=" + price +
                ", factory_price=" + factory_price +
                '}';
    }
}
