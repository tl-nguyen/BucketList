package com.telerikacademy.jasmine.thebucketlistapp.persisters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.telerikacademy.jasmine.thebucketlistapp.models.SQLiteUser;

import java.sql.SQLException;

public class SQLiteDBPref extends SQLiteDBHelper {

    public SQLiteDBPref(Context context) {
        super(context);
    }

    public void addRecord(String username, String password) {
        if (username == null || password == null || username.equals("") || password.equals("")) {
            return;
        }

        try {
            open();

            ContentValues values = new ContentValues();
            values.put(SQLiteTables.COLUMN_Username, username);
            values.put(SQLiteTables.COLUMN_Password, password);

            this.db.insert(SQLiteTables.TABLE_Users, null, values);

            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean userExist(String userName) {
        String query = "SELECT * FROM " + SQLiteTables.TABLE_Users + " WHERE " + SQLiteTables.COLUMN_Username + " =  \"" + userName+ "\"";

        try {
            open();

            Cursor cursor = db.rawQuery(query, null);

            return (cursor.moveToFirst());

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return false;
    }

    public void updateRecord(String username, String password) {
        try {
            open();

            ContentValues values = new ContentValues();
            values.put(SQLiteTables.COLUMN_Password, password);

            this.db.update(SQLiteTables.TABLE_Users, values, SQLiteTables.COLUMN_Username + "=?", new String[] {username});
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    public SQLiteUser findUser(String userName) {
        String query = "SELECT " + SQLiteTables.COLUMN_Username +", " + SQLiteTables.COLUMN_Password +
                " FROM " + SQLiteTables.TABLE_Users +
                " WHERE " + SQLiteTables.COLUMN_Username + " =  \"" + userName+ "\"";

        try {
            open();

            Cursor cursor = db.rawQuery(query, null);

            SQLiteUser user = new SQLiteUser();

            if (cursor.moveToFirst()) {
                user.setUsername(cursor.getString(cursor.getColumnIndex(SQLiteTables.COLUMN_Username)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(SQLiteTables.COLUMN_Password)));
            }

            cursor.close();

            return user;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return null;
    }
}
