package com.better.news.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.better.news.MainApp;
import com.better.news.db.table.DayTable;
import com.better.news.db.table.NewsTable;
import com.better.news.db.table.ReadingTable;
import com.better.news.db.table.ScienceTable;

/**
 * Created by Better on 2016/4/5.
 */
public class DbOpenHelper extends SQLiteOpenHelper {
    private static final String dbName="better.db";
    private static final int version=1;
    private static DbOpenHelper instance;

    public static final String DELETE_TABLE_DATA = "delete from ";
    public static final String DROP_TABLE = "drop table if exists ";
    public DbOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(NewsTable.CREATE_TABLE);
        db.execSQL(NewsTable.CREATE_COLLECTION_TABLE);

        db.execSQL(DayTable.CREATE_TABLE);
        db.execSQL(DayTable.CREATE_COLLECTION_TABLE);

        db.execSQL(ScienceTable.CREATE_TABLE);
        db.execSQL(ScienceTable.CREATE_COLLECTION_TABLE);

        db.execSQL(ReadingTable.CREATE_TABLE);
        db.execSQL(ReadingTable.CREATE_COLLECTION_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
    public static DbOpenHelper getInstance(){
        if (null==instance){
            synchronized (DbOpenHelper.class){
                if (null==instance){
                    instance=new DbOpenHelper(MainApp.getInstance(),dbName,null,version);
                }
            }
        }
        return instance;
    }
}
