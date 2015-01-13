package com.example.evgen.apiclient.helper;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

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
public class LikeVkNotes implements SentsVkNotes.Callbacks{

    final static String LOG_TAG = LikeVkNotes.class.getSimpleName();

    private String mBaseUrl;
    private Context mContext;

    public LikeVkNotes (Context context, String url){
        mBaseUrl = url;
        mContext = context;
        new SentsVkNotes(this, context, url);

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
                                         new SentVkLike(mContext, Api.VKLIKE_GET + id + "&access_token=" + VkOAuthHelper.mAccessToken);//EncrManager.decrypt(getActivity(), mAm.getUserData(sAccount, "Token")));
                                     } else {
                                         Toast.makeText(mContext, "You already added Like this note", Toast.LENGTH_SHORT).show();
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
