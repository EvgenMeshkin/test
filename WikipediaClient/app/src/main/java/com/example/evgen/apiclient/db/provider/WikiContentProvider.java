package com.example.evgen.apiclient.db.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.example.evgen.apiclient.db.DBHelper;

/**
 * Created by evgen on 13.12.2014.
 */
public class WikiContentProvider extends ContentProvider {
    final String LOG_TAG = WikiContentProvider.class.getSimpleName();
    // Table
    static final String WIKI_TABLE = "geodata";

    // Items
    static final String WIKI_ID = "_id";
    static final String WIKI_NAME = "name";
    static final String WIKI_KOR = "koordinaty";

    // Uri
    // authority
    static final String AUTHORITY = "com.example.evgenmeshkin.GeoData";

    static final String WIKI_PATH = "geodata";

    public static final Uri WIKI_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + WIKI_PATH);
    static final String WIKI_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + WIKI_PATH;
    static final String WIKI_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."
            + AUTHORITY + "." + WIKI_PATH;

    // UriMatcher
    static final int URI_DATA = 1;
    static final int URI_DATA_ID = 2;
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, WIKI_PATH, URI_DATA);
        uriMatcher.addURI(AUTHORITY, WIKI_PATH + "/#", URI_DATA_ID);
    }

    DBHelper dbHelper;
    SQLiteDatabase db;

    public boolean onCreate() {
        Log.d(LOG_TAG, "onCreate");
        dbHelper = new DBHelper(getContext());
        return true;
    }

    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Log.d(LOG_TAG, "query, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_DATA:
                Log.d(LOG_TAG, "URI_DATA");
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = WIKI_NAME + " ASC";
                }
                break;
            case URI_DATA_ID: // Uri с ID
                String id = uri.getLastPathSegment();
                Log.d(LOG_TAG, "URI_DATA_ID, " + id);
                if (TextUtils.isEmpty(selection)) {
                    selection = WIKI_ID + " = " + id;
                } else {
                    selection = selection + " AND " + WIKI_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(WIKI_TABLE, projection, selection,
                selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(),
                WIKI_CONTENT_URI);
        return cursor;
    }

    public Uri insert(Uri uri, ContentValues values) {
        Log.d(LOG_TAG, "insert, " + uri.toString());
        if (uriMatcher.match(uri) != URI_DATA)
            throw new IllegalArgumentException("Wrong URI: " + uri);

        db = dbHelper.getWritableDatabase();
        long rowID = db.insert(WIKI_TABLE, null, values);
        Uri resultUri = ContentUris.withAppendedId(WIKI_CONTENT_URI, rowID);
        getContext().getContentResolver().notifyChange(resultUri, null);
        return resultUri;
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(LOG_TAG, "delete, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_DATA:
                Log.d(LOG_TAG, "URI_DATA");
                break;
            case URI_DATA_ID:
                String id = uri.getLastPathSegment();
                Log.d(LOG_TAG, "URI_DATA_ID, " + id);
                if (TextUtils.isEmpty(selection)) {
                    selection = WIKI_ID + " = " + id;
                } else {
                    selection = selection + " AND " + WIKI_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        int cnt = db.delete(WIKI_TABLE, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        Log.d(LOG_TAG, "update, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_DATA:
                Log.d(LOG_TAG, "URI_DATA");

                break;
            case URI_DATA_ID:
                String id = uri.getLastPathSegment();
                Log.d(LOG_TAG, "URI_DATA_ID, " + id);
                if (TextUtils.isEmpty(selection)) {
                    selection = WIKI_ID + " = " + id;
                } else {
                    selection = selection + " AND " + WIKI_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        int cnt = db.update(WIKI_TABLE, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    public String getType(Uri uri) {
        Log.d(LOG_TAG, "getType, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_DATA:
                return WIKI_CONTENT_TYPE;
            case URI_DATA_ID:
                return WIKI_CONTENT_ITEM_TYPE;
        }
        return null;
    }
}