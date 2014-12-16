package com.example.evgen.apiclient.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by evgen on 13.12.2014.
 */
public class DBHelper extends SQLiteOpenHelper {
    static final String DB_NAME = "mydb";
    static final int DB_VERSION = 1;

    // Таблица
    static final String CONTACT_TABLE = "contacts";

    // Поля
    static final String CONTACT_ID = "_id";
    static final String CONTACT_NAME = "name";
    static final String CONTACT_EMAIL = "email";

    static final String DB_CREATE = "create table " + CONTACT_TABLE + "("
            + CONTACT_ID + " integer primary key autoincrement, "
            + CONTACT_NAME + " text, " + CONTACT_EMAIL + " text" + ");";
    final static String LOG_TAG = DBHelper.class.getSimpleName();

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "--- onCreate database ---");
        db.execSQL(DB_CREATE);
        ContentValues cv = new ContentValues();
        for (int i = 1; i <= 3; i++) {
            cv.put(CONTACT_NAME, "name " + i);
            cv.put(CONTACT_EMAIL, "email " + i);
            db.insert(CONTACT_TABLE, null, cv);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
