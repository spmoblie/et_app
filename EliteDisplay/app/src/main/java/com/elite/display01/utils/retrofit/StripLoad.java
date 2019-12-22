package com.elite.display01.utils.retrofit;

import rx.functions.Func1;

/**
 * 剥离 最终数据
 * Created by user on 05/03/18.
 */

public class StripLoad<T> implements Func1<BaseResponse<T>, T>{

    @Override
    public T call(BaseResponse<T> tBaseResponse) {
        return null;
    }

}
