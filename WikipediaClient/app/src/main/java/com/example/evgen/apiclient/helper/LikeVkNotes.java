package com.example.evgen.apiclient.helper;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.evgen.apiclient.Api;
import com.example.evgen.apiclient.auth.VkOAuthHelper;
import com.example.evgen.apiclient.bo.Category;
import com.example.evgen.apiclient.processing.LikeIsProcessor;
import com.example.evgen.apiclient.processing.NotesAllProcessor;
import com.example.evgen.apiclient.source.HttpDataSource;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by User on 12.01.2015.
 */
public class LikeVkNotes implements DataManager.Callback<List<Category>>, SentsVkNotes.Callbacks{

    //TODO remove from this, refactoring!
    private HttpClient mClient;
    //TODO remove from this, refactoring!
    private HttpPost mPost;
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
        if (data.get(i).getTitle().indexOf(mBaseUrl) != -1) {
            id = data.get(i).getId();
        }
        }
        if (id != null) {
            Log.d(LOG_TAG, "ID = " + id);
            final String liked = "";
            final Long finalId = id;
            DataManager.loadData(new DataManager.Callback<String>() {
                                 @Override
                                 public void onDataLoadStart() {
                                 }

                                 @Override
                                 public void onDone(String data) {
                                     Log.d(LOG_TAG, "Sent" + data);
                                     if (data.equals("0")) {
                                         Log.d(LOG_TAG, "Sent like");
                                         new SentVkLike(Api.VKLIKE_GET + finalId + "&access_token=" + VkOAuthHelper.mAccessToken);//EncrManager.decrypt(getActivity(), mAm.getUserData(sAccount, "Token")));
                                     }
                                 }

                                 @Override
                                 public void onError(Exception e) {
                                     onError(e);
                                 }
                             },
                Api.VKLIKEIS_GET + id + "&access_token=" + VkOAuthHelper.mAccessToken,
                new HttpDataSource(),
                new LikeIsProcessor());
     } else {
            new SentsVkNotes(this, mBaseUrl);

            Log.d(LOG_TAG, "id sent note" + id);
            final Long finalId = id;

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

    @Override
    public void onReturnId(final Long id) {
        DataManager.loadData(new DataManager.Callback<String>() {
                                 @Override
                                 public void onDataLoadStart() {
                                 }

                                 @Override
                                 public void onDone(String data) {
                                     Log.d(LOG_TAG, "Sent" + data);
                                     if (data.equals("0")) {
                                         Log.d(LOG_TAG, "Sent like");
                                         new SentVkLike(Api.VKLIKE_GET + id + "&access_token=" + VkOAuthHelper.mAccessToken);//EncrManager.decrypt(getActivity(), mAm.getUserData(sAccount, "Token")));
                                     }
                                 }

                                 @Override
                                 public void onError(Exception e) {
                                     onError(e);
                                 }
                             },
                Api.VKLIKEIS_GET + id + "&access_token=" + VkOAuthHelper.mAccessToken,
                new HttpDataSource(),
                new LikeIsProcessor());
    }
}
