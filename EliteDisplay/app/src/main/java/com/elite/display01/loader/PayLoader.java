package com.elite.display01.loader;

import com.elite.display01.entity.PayEntity;
import com.elite.display01.utils.retrofit.BaseResponse;
import com.elite.display01.utils.retrofit.RetrofitServiceManager;
import com.elite.display01.utils.retrofit.StripLoad;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by user on 05/03/18.
 */

public class PayLoader extends ObjectLoader {

    private PayService mPayService;

    public PayLoader(){
        mPayService = RetrofitServiceManager.getInstance().create(PayService.class);
    }

    public Observable<String> getPayResult(String orderNo){
        return observe(mPayService.getPayResult(orderNo))
                .map(new StripLoad<String>() {
                    @Override
                    public String call(BaseResponse<String> baseResponse) {
                        return baseResponse.data;
                    }
                });
    }

    public Observable<String> postPayDatas(Map<String, String> parameters){
        return observe(mPayService.postPayDatas(parameters))
                .map(new Func1<PayEntity, String>() {
                    @Override
                    public String call(PayEntity str) {
                        return str.getCode();
                    }
                });
    }


    public interface PayService{

        @GET("lhpayweb/dq/pay")
        Observable<BaseResponse<String>> getPayResult(@Query("orderNo") String orderNo);

        @FormUrlEncoded
        @POST("lhpayweb/dq/pay")
        Observable<PayEntity> postPayDatas(@FieldMap Map<String, String> parameters);

    }
}
