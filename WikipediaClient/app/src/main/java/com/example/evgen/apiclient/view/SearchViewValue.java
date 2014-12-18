package com.example.evgen.apiclient.view;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.example.evgen.apiclient.R;
import com.example.evgen.apiclient.helper.DataManager;

/**
 * Created by evgen on 18.12.2014.
 */
public class SearchViewValue extends ActionBarActivity implements SearchView.OnQueryTextListener{
    private static Callbacks callbacks;
    final static String LOG_TAG = SearchViewValue.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wiki);
        Log.d(LOG_TAG, "Oncreat");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint("Поиск");
        searchView.setOnQueryTextListener(this);
        Log.d(LOG_TAG, "onCreatMenu");
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        textsearch(s);
        Log.d(LOG_TAG, "EndInput");
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        textsearch(s);
        Log.d(LOG_TAG, "Input");
        return true;
    }

    public interface Callbacks {
        void onSetSearch (String value);
        void onEndSearch (String value);
    }

    public void setCallbacks (Callbacks callbacks) {
        this.callbacks = callbacks;

    }

    public static void textsearch(String search) {
        if (search.equals(null)) {
            return;
        }
        callbacks.onSetSearch(search);
        Log.d("id", search);
    }
    public static void endsearch(String search) {
        if (search.equals(null)) {
            return;
        }
        callbacks.onEndSearch(search);
        Log.d("id", search);
    }

}
