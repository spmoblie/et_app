package com.elite.inventory.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.elite.inventory.entity.OrderEntity;
import com.elite.inventory.utils.ExceptionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuzz on 2018/5/10.
 */

public class OrderDBService {

    private static final String TABLE_NAME = "orders";
    private static final String NAME_01 = "orderNo";
    private static final String NAME_02 = "currency";
    private static final String NAME_03 = "priceTotal";
    private static final String NAME_04 = "createTime";
    private static final String NAME_05 = "printSn";
    private static final String NAME_06 = "printStatus";
    private static final String NAME_07 = "shopNum";
    private static final String NAME_08 = "shopTag";

    private DBOpenHelper dbOpenHelper;
    private static OrderDBService instance = null;

    private OrderDBService(Context context) {
        this.dbOpenHelper = new DBOpenHelper(context);
    }

    private static synchronized void syncInit(Context context) {
        if (instance == null) {
            instance = new OrderDBService(context);
        }
    }

    public static OrderDBService getInstance(Context ctx) {
        if (instance == null) {
            syncInit(ctx);
        }
        return instance;
    }

    public void save(OrderEntity en) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME_01, en.getOrderNo());
        values.put(NAME_02, en.getCurrency());
        values.put(NAME_03, en.getPriceTotal());
        values.put(NAME_04, en.getCreateTime());
        values.put(NAME_05, en.getPrintSn());
        values.put(NAME_06, en.getPrintStatus());
        values.put(NAME_07, en.getShopNum());
        values.put(NAME_08, en.getShopTag());
        db.insert(TABLE_NAME, null, values);
    }

    public boolean delete(String orderNo) {
        boolean floot = false;
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        int count = db.delete(TABLE_NAME, NAME_01 + "=?", new String[] { orderNo });
        if (count == 1) {
            floot = true;
        }
        return floot;
    }

    public void deleteAll() {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        db.delete(TABLE_NAME, "", null);
    }

    public boolean update(OrderEntity en) {
        boolean floot = false;

        OrderEntity oldEn = find(en.getOrderNo());
        if (oldEn != null) {
            SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(NAME_01, en.getOrderNo());
            values.put(NAME_02, en.getCurrency());
            values.put(NAME_03, en.getPriceTotal());
            values.put(NAME_04, en.getCreateTime());
            values.put(NAME_05, en.getPrintSn());
            values.put(NAME_06, en.getPrintStatus());
            values.put(NAME_07, en.getShopNum());
            values.put(NAME_08, en.getShopTag());
            int count = db.update(TABLE_NAME, values, NAME_01 + "=?", new String[] { en.getOrderNo() });
            if (count == 1) {
                floot = true;
            }
        }else {
            save(en);
            floot = true;
        }
        return floot;
    }

    public OrderEntity find(String orderNo) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, NAME_01 + "=?", new String[] { orderNo }, null, null, null, "1");
        try {
            if (cursor.moveToFirst()) {
                return new OrderEntity(
                        cursor.getString(cursor.getColumnIndex(NAME_01)),
                        cursor.getString(cursor.getColumnIndex(NAME_02)),
                        cursor.getString(cursor.getColumnIndex(NAME_03)),
                        cursor.getString(cursor.getColumnIndex(NAME_04)),
                        cursor.getString(cursor.getColumnIndex(NAME_05)),
                        cursor.getInt(cursor.getColumnIndex(NAME_06)),
                        cursor.getInt(cursor.getColumnIndex(NAME_07)),
                        cursor.getString(cursor.getColumnIndex(NAME_08)));
            }
            return null;
        } catch (Exception e) {
            ExceptionUtils.handle(e);
            return null;
        } finally {
            cursor.close();
        }
    }

    public List<OrderEntity> getNoPrintDatas() {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, NAME_06 + "=?", new String[] { "0" }, null, null, null, null);
        try {
            List<OrderEntity> lists = new ArrayList<OrderEntity>();
            while (cursor.moveToNext()) {
                lists.add(new OrderEntity(
                        cursor.getString(cursor.getColumnIndex(NAME_01)),
                        cursor.getString(cursor.getColumnIndex(NAME_02)),
                        cursor.getString(cursor.getColumnIndex(NAME_03)),
                        cursor.getString(cursor.getColumnIndex(NAME_04)),
                        cursor.getString(cursor.getColumnIndex(NAME_05)),
                        cursor.getInt(cursor.getColumnIndex(NAME_06)),
                        cursor.getInt(cursor.getColumnIndex(NAME_07)),
                        cursor.getString(cursor.getColumnIndex(NAME_08))));
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
