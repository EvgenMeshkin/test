package com.example.evgen.apiclient.db.wikiorm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by User on 15.12.2014.
 */
public class WikiOrmFactory {

    private static WikiOrmHelper ormHelper;
    private static SQLiteDatabase database;

    static WikiOrmHelper getHelper() {
        return ormHelper;
    }

    static SQLiteDatabase getDatabase() {
        return database;
    }

    public static void SetHelper(Class<? extends WikiOrmHelper> ormHelperClass, Context context) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Constructor<?> constructor = ormHelperClass.getDeclaredConstructor(Context.class, String.class, SQLiteDatabase.CursorFactory.class, int.class);
        ormHelper = (WikiOrmHelper) constructor.newInstance(context, getDatabaseName(context), null, getDatabaseVersion(context));
        ormHelper.getWritableDatabase().execSQL("PRAGMA foreign_keys=ON;");
    }

    private static int getDatabaseVersion(Context context) {
        Integer version = WikiOrmMeta.getMetaData(context, "UO_DB_VERSION");
        if (version == null)
            version = 1;
        return version;
    }

    private static String getDatabaseName(Context context) {
        String dbName = WikiOrmMeta.getMetaData(context, "UO_DB_NAME");
        if (dbName == null)
            dbName = WikiOrmMeta.getAppName(context) + ".db";
        return dbName;
    }

    public static void ReleaseHelper() {
        database.close();
        ormHelper = null;
    }

    static void setDatabase(SQLiteDatabase database) {
        WikiOrmFactory.database = database;
    }
}
