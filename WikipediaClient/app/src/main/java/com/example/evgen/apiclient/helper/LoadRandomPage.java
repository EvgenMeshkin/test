package com.example.evgen.apiclient.helper;

import android.content.Context;
import android.util.Log;
import android.view.Window;

import com.example.evgen.apiclient.Api;
import com.example.evgen.apiclient.WikiActivity;
import com.example.evgen.apiclient.bo.Category;
import com.example.evgen.apiclient.bo.NoteGsonModel;
import com.example.evgen.apiclient.processing.RandomProcessor;
import com.example.evgen.apiclient.source.HttpDataSource;
import com.example.evgen.apiclient.source.VkDataSource;
import com.example.evgen.apiclient.view.VkUserDataView;

import java.util.List;

import javax.security.auth.callback.Callback;

/**
 * Created by User on 05.01.2015.
 */
public class LoadRandomPage implements DataManager.Callback<List<Category>>{
    private Callbacks mCallback;
    final static String LOG_TAG = LoadRandomPage.class.getSimpleName();



    public interface Callbacks {
        void onShowDetails(int index, NoteGsonModel note);
        boolean isDualPane();
        void onErrorA(Exception e);
    }

    public void loadingRandomPage (Callbacks callback){
        Log.d(LOG_TAG, "SrtartLoader");
        mCallback = callback;
        DataManager.loadData(this,
                Api.RANDOM_GET,
                new HttpDataSource(),
                new RandomProcessor());
    }

    @Override
    public void onDataLoadStart() {

    }

    @Override
    public void onDone(List<Category> data) {
      Log.d(LOG_TAG, "Item ");
      Integer i = data.size();
      Category item = data.get(i-1);
      Log.d(LOG_TAG, "Item " + item.getTITLE());
      NoteGsonModel note = new NoteGsonModel(null, item.getTITLE(), null);

      mCallback.onShowDetails(0, note);
    }

    @Override
    public void onError(Exception e) {
        Log.d(LOG_TAG, "Error" );
    }
}
