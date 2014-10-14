package com.telerikacademy.jasmine.thebucketlistapp.persisters;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Boyko on 14.10.2014 г..
 */
public class SQLiteDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "bucketList.db";
    private static final int DATABASE_VERSION = 1;

    public SQLiteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        SQLiteTables.onCreate(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion,int newVersion) {
        SQLiteTables.onUpgrade(database, oldVersion, newVersion);
    }

}
