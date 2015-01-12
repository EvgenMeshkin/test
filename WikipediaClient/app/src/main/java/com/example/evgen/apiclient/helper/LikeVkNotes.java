package com.example.evgen.apiclient.helper;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.evgen.apiclient.Api;
import com.example.evgen.apiclient.auth.VkOAuthHelper;
import com.example.evgen.apiclient.bo.Category;
import com.example.evgen.apiclient.processing.NotesAllProcessor;
import com.example.evgen.apiclient.source.HttpDataSource;

import java.util.List;

/**
 * Created by User on 12.01.2015.
 */
public class LikeVkNotes implements DataManager.Callback<List<Category>>{
    final static String LOG_TAG = LikeVkNotes.class.getSimpleName();

    private String mBaseUrl;

    public LikeVkNotes (String url){
        mBaseUrl = url;
        DataManager.loadData( this,
                Api.VKNOTES_ALL_GET + VkOAuthHelper.mAccessToken,
                new HttpDataSource(),
                new NotesAllProcessor());
    }

    @Override
    public void onDataLoadStart() {

    }

    @Override
    public void onDone(List<Category> data) {
        Log.d(LOG_TAG, "SrtartLoader" + data.toString());
        Long id = null;
        for (int i = 0; i < data.size(); i++) {
        if (data.get(i).getText().indexOf(mBaseUrl) != -1) {
            id = data.get(i).getId();
        }
        }
        if (id != null) {
            Log.d(LOG_TAG, "ID = " + id);
            Uri uri = Uri.parse(Api.VKLIKEIS_GET + id + "&access_token=" + VkOAuthHelper.mAccessToken);
            String fragment = uri.getFragment();
            Uri parsedFragment = Uri.parse("http://temp.com?" + fragment);
            String liked = parsedFragment.getQueryParameter("liked");
            Log.d(LOG_TAG, "Liked =" + liked);
            if (liked == "0") {
                Log.d(LOG_TAG, "Sent" + liked);
                Uri uriLike = Uri.parse(Api.VKLIKE_GET + id + "&access_token=" + VkOAuthHelper.mAccessToken);
            }
        }

//        DataManager.loadData(new DataManager.Callback<List<Category>>() {
//                                 @Override
//                                 public void onDataLoadStart() {
//                                 }
//
//                                 @Override
//                                 public void onDone(List<Category> data) {
//                                 }
//
//                                 @Override
//                                 public void onError(Exception e) {
//                                     onError(e);
//                                 }
//                             },
//                getUrl(COUNT, count),
//                getHttpDataSource(),
//                getProcessor());
    }

    @Override
    public void onError(Exception e) {
        Log.d(LOG_TAG, "Error");

    }
}
