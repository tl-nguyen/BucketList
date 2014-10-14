package com.telerikacademy.jasmine.thebucketlistapp.persisters;

/**
 * Created by Boyko on 14.10.2014 г..
 */
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SQLiteTables {

    // Database table
    public static final String TABLE_Users = "users";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_Username = "username";
    public static final String COLUMN_Password = "password";

    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table " + TABLE_Users + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_Username + " text not null, "
            + COLUMN_Password + " text not null "+ ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w(SQLiteTables.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_Users);
        onCreate(database);
    }
}
