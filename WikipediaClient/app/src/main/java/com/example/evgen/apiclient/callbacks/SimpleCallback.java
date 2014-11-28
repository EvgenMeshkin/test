package com.example.evgen.apiclient.callbacks;

import android.util.Log;

import com.example.evgen.apiclient.helper.DataManager;

/**
 * Created by evgen on 18.10.2014.
 */
public abstract class SimpleCallback<Result> implements DataManager.Callback {

    @Override
    public void onDataLoadStart() {
        Log.d("SimpleCallback", "onDataLoadStart");
    }

    @Override
    public void onError(Exception e) {
        Log.e("SimpleCallback", "onError", e);
    }

}
