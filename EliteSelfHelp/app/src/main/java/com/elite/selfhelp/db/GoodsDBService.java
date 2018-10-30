package com.elite.selfhelp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.elite.selfhelp.entity.GoodsEntity;
import com.elite.selfhelp.utils.ExceptionUtils;
import com.elite.selfhelp.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuzz on 2018/5/10.
 */

public class GoodsDBService {

    private static final String TABLE_NAME = "goods";
    private static final String NAME_01 = "sku";
    private static final String NAME_02 = "categoryId";
    private static final String NAME_03 = "weight";
    private static final String NAME_04 = "brand";
    private static final String NAME_05 = "model";
    private static final String NAME_06 = "name";
    private static final String NAME_07 = "productArea";
    private static final String NAME_08 = "upc";
    private static final String NAME_09 = "saleUnit";
    private static final String NAME_10 = "suggestPrice";
    private static final String NAME_11 = "price";
    private static final String NAME_12 = "factory_price";

    private DBOpenHelper dbOpenHelper;
    private static GoodsDBService instance = null;

    private GoodsDBService(Context context) {
        this.dbOpenHelper = new DBOpenHelper(context);
    }

    private static synchronized void syncInit(Context context) {
        if (instance == null) {
            instance = new GoodsDBService(context);
        }
    }

    public static GoodsDBService getInstance(Context ctx) {
        if (instance == null) {
            syncInit(ctx);
        }
        return instance;
    }

    public void save(GoodsEntity en) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME_01, en.getSku());
        values.put(NAME_02, en.getCategoryId());
        values.put(NAME_03, en.getWeight());
        values.put(NAME_04, en.getBrand());
        values.put(NAME_05, en.getModel());
        values.put(NAME_06, en.getName());
        values.put(NAME_07, en.getProductArea());
        values.put(NAME_08, en.getUpc());
        values.put(NAME_09, en.getSaleUnit());
        values.put(NAME_10, en.getSuggestPrice());
        values.put(NAME_11, en.getPrice());
        values.put(NAME_12, en.getFactory_price());
        db.insert(TABLE_NAME, null, values);
    }

    public boolean delete(String upc) {
        boolean floot = false;
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        int count = db.delete(TABLE_NAME, NAME_08 + "=?", new String[] { upc });
        if (count == 1) {
            floot = true;
        }
        return floot;
    }

    public void deleteAll() {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        db.delete(TABLE_NAME, "", null);
    }

    public boolean update(GoodsEntity en) {
        boolean floot = false;

        GoodsEntity oldEn = find(en.getUpc());
        if (oldEn != null) {
            SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(NAME_01, en.getSku());
            values.put(NAME_02, en.getCategoryId());
            values.put(NAME_03, en.getWeight());
            values.put(NAME_04, en.getBrand());
            values.put(NAME_05, en.getModel());
            values.put(NAME_06, en.getName());
            values.put(NAME_07, en.getProductArea());
            values.put(NAME_08, en.getUpc());
            values.put(NAME_09, en.getSaleUnit());
            values.put(NAME_10, en.getSuggestPrice());
            values.put(NAME_11, en.getPrice());
            values.put(NAME_12, en.getFactory_price());
            int count = db.update(TABLE_NAME, values, NAME_08 + "=?", new String[] { en.getUpc() });
            if (count == 1) {
                floot = true;
            }
        }else {
            save(en);
            floot = true;
        }
        return floot;
    }

    public GoodsEntity find(String upc) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, NAME_08 + "=?", new String[] { upc }, null, null, null, "1");
        try {
            if (cursor.moveToFirst()) {
                return new GoodsEntity(
                        cursor.getString(cursor.getColumnIndex(NAME_01)),
                        cursor.getString(cursor.getColumnIndex(NAME_02)),
                        cursor.getString(cursor.getColumnIndex(NAME_03)),
                        cursor.getString(cursor.getColumnIndex(NAME_04)),
                        cursor.getString(cursor.getColumnIndex(NAME_05)),
                        cursor.getString(cursor.getColumnIndex(NAME_06)),
                        cursor.getString(cursor.getColumnIndex(NAME_07)),
                        cursor.getString(cursor.getColumnIndex(NAME_08)),
                        cursor.getString(cursor.getColumnIndex(NAME_09)),
                        cursor.getDouble(cursor.getColumnIndex(NAME_10)),
                        cursor.getDouble(cursor.getColumnIndex(NAME_11)),
                        cursor.getDouble(cursor.getColumnIndex(NAME_12)));
            }
            return null;
        } catch (Exception e) {
            ExceptionUtils.handle(e);
            return null;
        } finally {
            cursor.close();
        }
    }

    public List<GoodsEntity> getListData(String categoryId) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, NAME_02 + "=?", new String[] { categoryId }, null, null, null, null);
        try {
            List<GoodsEntity> lists = new ArrayList<GoodsEntity>();
            while (cursor.moveToNext()) {
                lists.add(new GoodsEntity(
                        cursor.getString(cursor.getColumnIndex(NAME_01)),
                        cursor.getString(cursor.getColumnIndex(NAME_02)),
                        cursor.getString(cursor.getColumnIndex(NAME_03)),
                        cursor.getString(cursor.getColumnIndex(NAME_04)),
                        cursor.getString(cursor.getColumnIndex(NAME_05)),
                        cursor.getString(cursor.getColumnIndex(NAME_06)),
                        cursor.getString(cursor.getColumnIndex(NAME_07)),
                        cursor.getString(cursor.getColumnIndex(NAME_08)),
                        cursor.getString(cursor.getColumnIndex(NAME_09)),
                        cursor.getDouble(cursor.getColumnIndex(NAME_10)),
                        cursor.getDouble(cursor.getColumnIndex(NAME_11)),
                        cursor.getDouble(cursor.getColumnIndex(NAME_12))));
            }
            return lists;
        } catch (Exception e) {
            ExceptionUtils.handle(e);
            return null;
        } finally {
            cursor.close();
        }
    }

    public List<GoodsEntity> getKeywordData(String keyword) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        String selection = NAME_06; //默认匹配商品名称
        if (StringUtils.isNumeric(keyword)) {
            selection = NAME_08; //全数字为商品条形码
        }
        Cursor cursor = db.query(TABLE_NAME, null, selection + " like '%" + keyword + "%'", null, null, null, null, null);
        try {
            List<GoodsEntity> lists = new ArrayList<GoodsEntity>();
            while (cursor.moveToNext()) {
                lists.add(new GoodsEntity(
                        cursor.getString(cursor.getColumnIndex(NAME_01)),
                        cursor.getString(cursor.getColumnIndex(NAME_02)),
                        cursor.getString(cursor.getColumnIndex(NAME_03)),
                        cursor.getString(cursor.getColumnIndex(NAME_04)),
                        cursor.getString(cursor.getColumnIndex(NAME_05)),
                        cursor.getString(cursor.getColumnIndex(NAME_06)),
                        cursor.getString(cursor.getColumnIndex(NAME_07)),
                        cursor.getString(cursor.getColumnIndex(NAME_08)),
                        cursor.getString(cursor.getColumnIndex(NAME_09)),
                        cursor.getDouble(cursor.getColumnIndex(NAME_10)),
                        cursor.getDouble(cursor.getColumnIndex(NAME_11)),
                        cursor.getDouble(cursor.getColumnIndex(NAME_12))));
            }
            return lists;
        } catch (Exception e) {
            ExceptionUtils.handle(e);
            return null;
        } finally {
            cursor.close();
        }
    }

    public int getCount() {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from " + TABLE_NAME, null);
        try {
            cursor.moveToFirst();
            return cursor.getInt(0);
        } catch (Exception e) {
            ExceptionUtils.handle(e);
            return 0;
        } finally {
            cursor.close();
        }
    }
}
