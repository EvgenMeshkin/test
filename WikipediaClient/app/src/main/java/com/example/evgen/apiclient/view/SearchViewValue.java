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
//TODO remove
public class SearchViewValue  {
    private static Callbacks callbacks;
    final static String LOG_TAG = SearchViewValue.class.getSimpleName();


    public interface Callbacks {
        void onSetSearch (String value);
        void onEndSearch (Context context, String value);
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
    public static void endsearch(Context context, String search) {
        if (search.equals(null)) {
            return;
        }
        callbacks.onEndSearch(context, search);
        Log.d("id", search);
    }

}
