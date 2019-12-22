package com.elite.display01.utils.retrofit;

/**
 * 异常处理类，将异常包装成一个Fault,抛给上层统一处理
 * Created by user on 05/03/18.
 */

public class Fault extends RuntimeException {

    private int errorCode;

    public Fault(int errorCode, String message){
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

}
