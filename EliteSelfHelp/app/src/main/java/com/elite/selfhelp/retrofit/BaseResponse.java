package com.elite.selfhelp.retrofit;

/**
 * 网络请求结果 基类
 * Created by user on 2018/5/9.
 */

public class BaseResponse<T> {
    public int status;
    public String message;

    public T data;

    public boolean isSuccess(){
        return status == 200;
    }
}
