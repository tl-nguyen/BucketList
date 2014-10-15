package com.telerikacademy.jasmine.thebucketlistapp.persisters;

/**
 * Created by Boyko on 14.10.2014 г..
 */
import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class SQLiteContentProvider extends ContentProvider {

    private SQLiteDBHelper database;

    // used for the UriMatcher
    private static final int Users = 10;
    private static final int Users_ID = 20;

    private static final String AUTHORITY = "com.telerikacademy.jasmine.thebucketlistapp.contentprovider";

    private static final String BASE_PATH = "thebucketlist";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/thebucketlist";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/thebucketlist";

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, Users);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", Users_ID);
    }

    @Override
    public boolean onCreate() {
        database = new SQLiteDBHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // check if the caller has requested a column which does not exists
        checkColumns(projection);

        queryBuilder.setTables(SQLiteTables.TABLE_Users);

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case Users:
                break;
            case Users_ID:
                // adding the ID to the original query
                queryBuilder.appendWhere(SQLiteTables.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        // make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsDeleted = 0;
        long id = 0;
        switch (uriType) {
            case Users:
                id = sqlDB.insert(SQLiteTables.TABLE_Users, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsDeleted = 0;
        switch (uriType) {
            case Users:
                rowsDeleted = sqlDB.delete(SQLiteTables.TABLE_Users, selection, selectionArgs);
                break;
            case Users_ID:
                String id = uri.getLastPathSegment();

                if (TextUtils.isEmpty(selection)) rowsDeleted = sqlDB.delete(SQLiteTables.TABLE_Users, SQLiteTables.COLUMN_ID + "=" + id, null);
                else rowsDeleted = sqlDB.delete(SQLiteTables.TABLE_Users, SQLiteTables.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);

                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsUpdated = 0;
        switch (uriType) {
            case Users:
                rowsUpdated = sqlDB.update(SQLiteTables.TABLE_Users, values, selection, selectionArgs);
                break;
            case Users_ID:
                String id = uri.getLastPathSegment();

                if (TextUtils.isEmpty(selection)) rowsUpdated = sqlDB.update(SQLiteTables.TABLE_Users, values, SQLiteTables.COLUMN_ID + "=" + id, null);
                else rowsUpdated = sqlDB.update(SQLiteTables.TABLE_Users, values, SQLiteTables.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);

                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    private void checkColumns(String[] projection) {
        String[] available = { SQLiteTables.COLUMN_Username, SQLiteTables.COLUMN_Password, SQLiteTables.COLUMN_ID };
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
            // check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) throw new IllegalArgumentException("Unknown columns in projection");
        }
    }

}

