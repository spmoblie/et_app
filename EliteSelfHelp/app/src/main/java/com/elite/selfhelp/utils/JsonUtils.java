package com.elite.selfhelp.utils;

import com.elite.selfhelp.entity.BaseEntity;
import com.elite.selfhelp.entity.CategoryEntity;
import com.elite.selfhelp.entity.GoodsEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2018/5/9.
 */

public class JsonUtils {

    /**
     * 解析商品列表数据
     */
    public static BaseEntity getGoodsLists(JSONObject jsonObject) throws JSONException {
        if (jsonObject == null) return null;
        BaseEntity mainEn = new BaseEntity();
        getCommonKeyValue(mainEn, jsonObject);

        if (StringUtils.notNull(jsonObject, "Result")) {
            JSONArray datas = jsonObject.getJSONArray("Result");
            GoodsEntity childEn;
            List<GoodsEntity> lists = new ArrayList<GoodsEntity>();
            for (int j = 0; j < datas.length(); j++) {
                JSONObject item = datas.getJSONObject(j);
                childEn = new GoodsEntity();
                childEn.setSku(item.getString("sku"));
                childEn.setCategoryId(item.getString("categoryId"));
                //childEn.setWeight(item.getString("weight"));
                childEn.setBrand(item.getString("brand"));
                childEn.setModel(item.getString("model"));
                childEn.setName(item.getString("name"));
                childEn.setProductArea(item.getString("productArea"));
                childEn.setUpc(item.getString("upc"));
                childEn.setSaleUnit(item.getString("saleUnit"));
                //childEn.setSuggestPrice(StringUtils.getDouble(item.getString("suggestPrice")));
                childEn.setPrice(StringUtils.getDouble(item.getString("price")));
                childEn.setFactory_price(StringUtils.getDouble(item.getString("factory_price")));
                lists.add(childEn);
            }
            mainEn.setLists(lists);
        }
        return mainEn;
    }

    /**
     * 解析商品分类数据
     */
    public static BaseEntity getCategoryLists(JSONObject jsonObject) throws JSONException {
        if (jsonObject == null) return null;
        BaseEntity mainEn = new BaseEntity();
        getCommonKeyValue(mainEn, jsonObject);

        if (StringUtils.notNull(jsonObject, "Result")) {
            JSONArray datas = jsonObject.getJSONArray("Result");
            CategoryEntity childEn;
            List<CategoryEntity> lists = new ArrayList<CategoryEntity>();
            for (int j = 0; j < datas.length(); j++) {
                JSONObject item = datas.getJSONObject(j);
                childEn = new CategoryEntity();
                childEn.setCategoryId(item.getString("categoryId"));
                childEn.setName(item.getString("name"));
                childEn.setParentId(item.getString("parentId"));
                lists.add(childEn);
            }
            mainEn.setLists(lists);
        }
        return mainEn;
    }

    /**
     * 解析通用数据
     */
    private static void getCommonKeyValue(BaseEntity baseEn, JSONObject jsonObj) throws JSONException{
        if (jsonObj.has("isSuccess")) {
            baseEn.setSuccess(jsonObj.getBoolean("isSuccess"));
        }
        if (jsonObj.has("returnMsg")) {
            baseEn.setReturnMsg(jsonObj.getString("returnMsg"));
        }
    }

}
