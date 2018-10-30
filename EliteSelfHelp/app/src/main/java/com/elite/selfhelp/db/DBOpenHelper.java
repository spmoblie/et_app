package com.elite.selfhelp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by xuzz on 2018/5/8.
 */

public class DBOpenHelper extends SQLiteOpenHelper {

    //将原先的表名修改为备用表名
    //private String CREATE_TEMP_MESSAGE = "alter table message rename to _temp_message";
    //将备用表中的数据复制到新建的表中（注意' '是为新加的字段插入默认值的必须加上，否则就会出错）。
    //private String INSERT_DATA = "insert into message select *,'' from _temp_message";
    //删除备用的表
    //private String DROP_MESSAGE = "drop table _temp_message";
    //如果表不存在则创建表
    private String CREATE_GOODS = "create table if not exists goods("
            + " _id integer primary key autoincrement,"
            + " sku text,"
            + " categoryId text,"
            + " weight text,"
            + " brand text,"
            + " model text,"
            + " name text,"
            + " productArea text,"
            + " upc text,"
            + " saleUnit text,"
            + " suggestPrice double,"
            + " price double,"
            + " factory_price double);";

    private String CREATE_CATEGORY = "create table if not exists category("
            + " _id integer primary key autoincrement,"
            + " categoryId text,"
            + " name text,"
            + " parentId text);";

    private String CREATE_ORDER = "create table if not exists orders("
            + " _id integer primary key autoincrement,"
            + " orderNo text,"
            + " currency text,"
            + " priceTotal text,"
            + " createTime text,"
            + " printSn text,"
            + " printStatus integer,"
            + " shopNum integer,"
            + " shopTag text);";


    public DBOpenHelper(Context context) {
        super(context, "eliteselfhelp.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //商品明细数据库表
        db.execSQL(CREATE_GOODS);
        //商品分类数据库表
        db.execSQL(CREATE_CATEGORY);
        //自助订单数据库表
        db.execSQL(CREATE_ORDER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
