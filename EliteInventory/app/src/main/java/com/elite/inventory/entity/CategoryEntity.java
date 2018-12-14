package com.elite.inventory.entity;

import java.util.List;

/**
 * 商品分类数据实体类
 */

public class CategoryEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private int classId; //分类ID

    private int parentId; //父类ID，为空时为“0”

    private String className; //分类名称

    private List<GoodsEntity> goodsLs; //子级商品集合

    private List<CategoryEntity> childLs; //子级分类集合


    public CategoryEntity() {

    }

    public CategoryEntity(int classId, int parentId, String className) {
        this.classId = classId;
        this.parentId = parentId;
        this.className = className;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<GoodsEntity> getGoodsLs() {
        return goodsLs;
    }

    public void setGoodsLs(List<GoodsEntity> goodsLs) {
        this.goodsLs = goodsLs;
    }

    public List<CategoryEntity> getChildLs() {
        return childLs;
    }

    public void setChildLs(List<CategoryEntity> childLs) {
        this.childLs = childLs;
    }
}
