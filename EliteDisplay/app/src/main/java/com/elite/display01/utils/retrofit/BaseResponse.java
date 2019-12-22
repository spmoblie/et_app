package com.elite.display01.utils.retrofit;

/**
 * 网络请求结果 基类
 * Created by user on 05/03/18.
 */

public class BaseResponse<T> {
    public int status;
    public String message;

    public T data;

    public boolean isSuccess(){
        return status == 200;
    }
}
