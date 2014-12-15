package com.example.evgen.apiclient.db.wikiorm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by evgen on 13.12.2014.
 */
public abstract class WikiOrmHelper extends SQLiteOpenHelper {

    protected abstract void onCreate();
    protected abstract void onUpgrade(int oldVersion, int newVersion);

    public WikiOrmHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        WikiOrmFactory.setDatabase(database);
        onCreate();
    }

    @Override
    public void onOpen(SQLiteDatabase database) {
        super.onOpen(database);
        WikiOrmFactory.setDatabase(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        WikiOrmFactory.setDatabase(database);
        onUpgrade(oldVersion, newVersion);
    }
}