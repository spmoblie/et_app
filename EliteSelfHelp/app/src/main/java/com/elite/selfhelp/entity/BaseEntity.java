package com.elite.selfhelp.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 所有数据实体类的父类
 * Created by xuzz on 2018/5/8.
 */

public class BaseEntity<T> implements Serializable {

    private static final long serialVersionUID = 2980439304361030908L;

    private boolean isSuccess; //响应状态
    private String returnMsg; //状态描述

    private T data; //单个数据
    private List<T> lists; //集合数据

    public BaseEntity() {
        super();
    }

    public BaseEntity(boolean isSuccess, String returnMsg) {
        this.isSuccess = isSuccess;
        this.returnMsg = returnMsg;
    }

    public String getEntityId() {
        return "";
    };

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<T> getLists() {
        return lists;
    }

    public void setLists(List<T> lists) {
        this.lists = lists;
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "isSuccess=" + isSuccess +
                ", returnMsg='" + returnMsg + '\'' +
                ", data=" + data +
                ", lists=" + lists +
                '}';
    }
}
