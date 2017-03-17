package com.dcch.sharebike.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.dcch.sharebike.moudle.home.bean.RoutePoint;

/**
 * Created by Administrator on 2017/3/17 0017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "shareBike.db";
    private SQLiteDatabase mDatabase;
    private static final String TABLE_NAME = "routePoint";
    private RoutePoint mPoint;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        mDatabase = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase mDatabase) {
        //创建表的sql语句
        String sql = "CREATE TABLE " + TABLE_NAME + "("
                + "id INTEGER primary key autoincrement ,"
                + "routeLat double"
                + "routeLng double"
                + "distance float"
                + ")";
        //执行创建数据库操作
        mDatabase.execSQL(sql);
        Log.e("create", "数据库创建成功");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public long insert(RoutePoint mPoint) {
        ContentValues values = new ContentValues();
        values.put("routeLat", mPoint.getRouteLat());
        values.put("routeLng", mPoint.getRouteLng());
        values.put("distance", mPoint.getDistance());
        long lon = mDatabase.insert(TABLE_NAME, null, values);
        mDatabase.close();
        return lon;
    }

    public RoutePoint query() {
        Cursor cursor = mDatabase.query(TABLE_NAME, new String[]{"id,routeLat,routeLng,distance"}, null, null, null, null, null);
        RoutePoint routePoint = new RoutePoint();
        while (cursor.moveToNext()) {
            routePoint.setId(cursor.getInt(0));
            routePoint.setRouteLat(cursor.getDouble(1));
            routePoint.setRouteLng(cursor.getDouble(2));
            routePoint.setDistance(cursor.getFloat(3));
        }
        cursor.close();
        mDatabase.close();
        return routePoint;
    }

    public int update(RoutePoint mPoint) {
        ContentValues values = new ContentValues();
        values.put("routeLat", mPoint.getRouteLat());
        values.put("routeLng", mPoint.getRouteLng());
        values.put("distance", mPoint.getDistance());
        int res = mDatabase.update(TABLE_NAME, values, "id=?", new String[]{"1"});
        mDatabase.close();
        return res;
    }

}
