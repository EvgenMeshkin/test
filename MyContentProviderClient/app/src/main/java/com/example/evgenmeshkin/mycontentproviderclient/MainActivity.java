package com.example.evgenmeshkin.mycontentproviderclient;

/**
 * Created by User on 01.12.2014.
 */
import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends FragmentActivity  {

    final String LOG_TAG = "myLogs";

    final Uri CONTACT_URI = Uri
            .parse("content://ru.startandroid.providers.AdressBook/contacts");

    final String CONTACT_NAME = "name";
    final String CONTACT_EMAIL = "email";
    Cursor mCursor;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        LoaderManager supportLoaderManager = getSupportLoaderManager();
        supportLoaderManager.restartLoader(0,
                new Bundle(),
                new LoaderManager.LoaderCallbacks<Cursor>() {

                    @Override
                    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
                        return new CursorLoader(MainActivity.this){

                            @Override
                            public Cursor loadInBackground() {
                                Cursor cursor = getContentResolver().query(CONTACT_URI, null, null, null, null);
                                //cursor.addRow(new Object[]{1l, "Vasya"});
                                return cursor;
                            }
                        };
                    }

                    @Override
                    public void onLoadFinished(Loader<Cursor> objectLoader, Cursor cursor) {
                        mCursor = cursor;
                        String from[] = { "name", "email" };
                        int to[] = { android.R.id.text1, android.R.id.text2 };
                        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getApplicationContext(),
                                android.R.layout.simple_list_item_2, cursor, from, to);
                        ListView lvContact = (ListView) findViewById(R.id.lvContact);
                        lvContact.setAdapter(adapter);

                    }

                    @Override
                    public void onLoaderReset(Loader<Cursor> objectLoader) {
                        mCursor = null;
                    }

                });
    }



    public void onClickInsert(View v) {
        ContentValues cv = new ContentValues();
        cv.put(CONTACT_NAME, "name 4");
        cv.put(CONTACT_EMAIL, "email 4");
        Uri newUri = getContentResolver().insert(CONTACT_URI, cv);
        Log.d(LOG_TAG, "insert, result Uri : " + newUri.toString());
    }

    public void onClickUpdate(View v) {
        ContentValues cv = new ContentValues();
        cv.put(CONTACT_NAME, "name 5");
        cv.put(CONTACT_EMAIL, "email 5");
        Uri uri = ContentUris.withAppendedId(CONTACT_URI, 2);
        int cnt = getContentResolver().update(uri, cv, null, null);
        Log.d(LOG_TAG, "update, count = " + cnt);
    }

    public void onClickDelete(View v) {
        Uri uri = ContentUris.withAppendedId(CONTACT_URI, 3);
        int cnt = getContentResolver().delete(uri, null, null);
        Log.d(LOG_TAG, "delete, count = " + cnt);
    }

    public void onClickError(View v) {
        Uri uri = Uri.parse("content://ru.startandroid.providers.AdressBook/phones");
        try {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        } catch (Exception ex) {
            Log.d(LOG_TAG, "Error: " + ex.getClass() + ", " + ex.getMessage());
        }

    }




}
