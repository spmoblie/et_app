package com.elite.selfhelp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.elite.selfhelp.entity.CategoryEntity;
import com.elite.selfhelp.utils.ExceptionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuzz on 2018/5/8.
 */

public class CategoryDBService {

    private static final String TABLE_NAME = "category";
    private static final String NAME_01 = "categoryId";
    private static final String NAME_02 = "name";
    private static final String NAME_03 = "parentId";

    private DBOpenHelper dbOpenHelper;
    private static CategoryDBService instance = null;

    private CategoryDBService(Context context) {
        this.dbOpenHelper = new DBOpenHelper(context);
    }

    private static synchronized void syncInit(Context context) {
        if (instance == null) {
            instance = new CategoryDBService(context);
        }
    }

    public static CategoryDBService getInstance(Context ctx) {
        if (instance == null) {
            syncInit(ctx);
        }
        return instance;
    }

    public void save(CategoryEntity en) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME_01, en.getCategoryId());
        values.put(NAME_02, en.getName());
        values.put(NAME_03, en.getParentId());
        db.insert(TABLE_NAME, null, values);
    }

    public boolean delete(String id) {
        boolean floot = false;
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        int count = db.delete(TABLE_NAME, NAME_01 + "=?", new String[] { id });
        if (count == 1) {
            floot = true;
        }
        return floot;
    }

    public void deleteAll() {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        db.delete(TABLE_NAME, "", null);
    }

    public boolean update(CategoryEntity en) {
        boolean floot = false;

        CategoryEntity oldEn = find(en.getCategoryId());
        if (oldEn != null) {
            SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(NAME_01, en.getCategoryId());
            values.put(NAME_02, en.getName());
            values.put(NAME_03, en.getParentId());
            int count = db.update(TABLE_NAME, values, NAME_01 + "=?", new String[] { en.getCategoryId() });
            if (count == 1) {
                floot = true;
            }
        }else {
            save(en);
            floot = true;
        }
        return floot;
    }

    public CategoryEntity find(String id) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, NAME_01 + "=?", new String[] { id }, null, null, null, "1");
        try {
            if (cursor.moveToFirst()) {
                return new CategoryEntity(
                        cursor.getString(cursor.getColumnIndex(NAME_01)),
                        cursor.getString(cursor.getColumnIndex(NAME_02)),
                        cursor.getString(cursor.getColumnIndex(NAME_03)));
            }
            return null;
        } catch (Exception e) {
            ExceptionUtils.handle(e);
            return null;
        } finally {
            cursor.close();
        }
    }

    public List<CategoryEntity> getListData(String parentId) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, NAME_03 + "=?", new String[] { parentId }, null, null, null, null);
        try {
            List<CategoryEntity> lists = new ArrayList<CategoryEntity>();
            while (cursor.moveToNext()) {
                lists.add(new CategoryEntity(
                        cursor.getString(cursor.getColumnIndex(NAME_01)),
                        cursor.getString(cursor.getColumnIndex(NAME_02)),
                        cursor.getString(cursor.getColumnIndex(NAME_03))));
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
