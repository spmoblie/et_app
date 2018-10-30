package com.elite.selfhelp.entity;

import java.util.List;

/**
 * 商品分类数据实体类
 * Created by xuzz on 2018/5/8.
 */

public class CategoryEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String categoryId; //品类ID

    private String name; //品类名称

    private String parentId; //父类ID，为空时为“null”

    private List<GoodsEntity> goodsLists; //子级商品集合


    public CategoryEntity() {

    }

    public CategoryEntity(String categoryId, String name, String parentId) {
        this.categoryId = categoryId;
        this.name = name;
        this.parentId = parentId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public List<GoodsEntity> getGoodsLists() {
        return goodsLists;
    }

    public void setGoodsLists(List<GoodsEntity> goodsLists) {
        this.goodsLists = goodsLists;
    }

    @Override
    public String toString() {
        return "CategoryEntity{" +
                "categoryId='" + categoryId + '\'' +
                ", name='" + name + '\'' +
                ", parentId='" + parentId + '\'' +
                ", goodsLists=" + goodsLists +
                '}';
    }
}
