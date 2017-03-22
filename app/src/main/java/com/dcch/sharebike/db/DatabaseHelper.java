package com.dcch.sharebike.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dcch.sharebike.utils.LogUtils;

/**
 * Created by Administrator on 2017/3/17 0017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "shareBike.db";
    private SQLiteDatabase mDatabase;
    private static final String TABLE_NAME = "routePoint";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase mDatabase) {
////        创建一个数据库
        mDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (route_id integer primary key autoincrement ," +
                "cycle_points text not null ," +
                "cycle_distance text not null ) ");
        LogUtils.d("数据库","创建成功");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


}
