package com.elite.display01.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.elite.display01.entity.PayEntity;

/**
 * Created by user on 05/03/18.
 */

public class JsonUtil {

    public static PayEntity getPayResult(String json) throws JSONException {
        // {"message":"请求成功","orderNo":"Test1511669660009","respMsg":"weixin://wxpay/bizpayurl?pr=zVXjFYk","code":"000000"}
        LogUtil.i("JsonUtil", json);
        JSONObject obj = JSON.parseObject(json);
        return new PayEntity(obj.getString("code")
                ,obj.getString("message")
                ,obj.getString("orderNo")
                ,obj.getString("respMsg"));
    }

}
