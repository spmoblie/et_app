package com.elite.inventory.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.elite.inventory.entity.CategoryEntity;
import com.elite.inventory.utils.ExceptionUtils;

import java.util.ArrayList;
import java.util.List;


public class CategoryDBService {

    private static final String TABLE_NAME = "category";
    private static final String NAME_01 = "class_id";
    private static final String NAME_02 = "parentId";
    private static final String NAME_03 = "className";

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
        values.put(NAME_02, en.getParentId());
        values.put(NAME_03, en.getClassName());
        db.insert(TABLE_NAME, null, values);
    }

    public boolean delete(int id) {
        boolean floot = false;
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        int count = db.delete(TABLE_NAME, NAME_01 + "=?", new String[] { String.valueOf(id) });
        if (count == 1) {
            floot = true;
        }
        return floot;
    }

    public boolean deleteChild(int ParentId) {
        boolean floot = false;
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        int count = db.delete(TABLE_NAME, NAME_02 + "=?", new String[] { String.valueOf(ParentId) });
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

        CategoryEntity oldEn = find(en.getClassId());
        if (oldEn != null) {
            SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(NAME_02, en.getParentId());
            values.put(NAME_03, en.getClassName());
            int count = db.update(TABLE_NAME, values, NAME_01 + "=?", new String[] { String.valueOf(en.getClassId()) });
            if (count == 1) {
                floot = true;
            }
        }else {
            save(en);
            floot = true;
        }
        return floot;
    }

    public CategoryEntity find(int id) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, NAME_01 + "=?", new String[] { String.valueOf(id) }, null, null, null, "1");
        try {
            if (cursor.moveToFirst()) {
                return new CategoryEntity(
                        cursor.getInt(cursor.getColumnIndex(NAME_01)),
                        cursor.getInt(cursor.getColumnIndex(NAME_02)),
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

    public List<CategoryEntity> getListData(int id) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, NAME_02 + "=?", new String[] { String.valueOf(id) }, null, null, null, null);
        try {
            List<CategoryEntity> lists = new ArrayList<CategoryEntity>();
            while (cursor.moveToNext()) {
                lists.add(new CategoryEntity(
                        cursor.getInt(cursor.getColumnIndex(NAME_01)),
                        cursor.getInt(cursor.getColumnIndex(NAME_02)),
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

    public boolean isNameExist(String name) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        String queryStr = "Select * from " + TABLE_NAME + " where " + NAME_03 + " = ?";
        Cursor cursor = db.rawQuery(queryStr,new String[] { name });
        if (cursor.getCount() > 0){
            cursor.close();
            return  true;
        }
        cursor.close();
        return false;
    }

}
