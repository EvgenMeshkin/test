package com.example.evgen.apiclient.helper;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.evgen.apiclient.Api;
import com.example.evgen.apiclient.auth.VkOAuthHelper;
import com.example.evgen.apiclient.bo.Category;
import com.example.evgen.apiclient.bo.NoteGsonModel;
import com.example.evgen.apiclient.fragments.DetailsFragment;
import com.example.evgen.apiclient.os.AsyncTask;
import com.example.evgen.apiclient.processing.LikeIsProcessor;
import com.example.evgen.apiclient.processing.NoteProcessor;
import com.example.evgen.apiclient.processing.NotesAllProcessor;
import com.example.evgen.apiclient.source.HttpDataSource;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.List;

/**
 * Created by evgen on 11.01.2015.
 */
public class SentsVkNotes implements DataManager.Callback<List<Category>>{

    final static String LOG_TAG = SentsVkNotes.class.getSimpleName();
    private String mBaseUrl;
    private Callbacks mCallbacks;
    private Context mContext;

    public interface Callbacks {
        void onReturnId(Long id);
    }

    public SentsVkNotes (final Callbacks callbacks, final Context context, final String url){
        mCallbacks = callbacks;
        mContext = context;
        mBaseUrl = url;
        DataManager.loadData( this,
                Api.VKNOTES_ALL_GET + VkOAuthHelper.mAccessToken,
                new HttpDataSource(),
                new NotesAllProcessor());
        Log.d(LOG_TAG, "Url " + VkOAuthHelper.mAccessToken);

    }


    @Override
    public void onDataLoadStart() {

    }

    @Override
    public void onDone(List<Category> data) {

        Long id = null;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getTitle().indexOf(mBaseUrl) != -1) {
                id = data.get(i).getId();
            }
        }
        if (id != null) {
            mCallbacks.onReturnId(id);
            Toast.makeText(mContext, "You already added this note", Toast.LENGTH_SHORT).show();
        } else {
            final Long[] mData = new Long[1];
            DataManager.loadData(new DataManager.Callback<Long>() {
                                     @Override
                                     public void onDataLoadStart() {
                                     }

                                     @Override
                                     public void onDone(Long data) {
                                         Log.d(LOG_TAG, "Sent Note" + data);
                                         mCallbacks.onReturnId(data);
                                         Toast.makeText(mContext, "Note added", Toast.LENGTH_SHORT).show();
                                     }

                                     @Override
                                     public void onError(Exception e) {
                                         onError(e);
                                     }
                                 },
                    Api.VKNOTES_GET + mBaseUrl + "&text=" + Api.MAIN_URL + mBaseUrl +"&access_token=" + VkOAuthHelper.mAccessToken,
                    new HttpDataSource(),
                    new NoteProcessor());

        }
    }

    @Override
    public void onError(Exception e) {

    }





}
