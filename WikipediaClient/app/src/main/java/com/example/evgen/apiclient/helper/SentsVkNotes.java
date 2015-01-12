package com.example.evgen.apiclient.helper;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.example.evgen.apiclient.Api;
import com.example.evgen.apiclient.auth.VkOAuthHelper;
import com.example.evgen.apiclient.bo.NoteGsonModel;
import com.example.evgen.apiclient.fragments.DetailsFragment;
import com.example.evgen.apiclient.os.AsyncTask;
import com.example.evgen.apiclient.processing.LikeIsProcessor;
import com.example.evgen.apiclient.processing.NoteProcessor;
import com.example.evgen.apiclient.source.HttpDataSource;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Created by evgen on 11.01.2015.
 */
public class SentsVkNotes {

    //TODO remove from this, refactoring!
    private HttpClient mClient;
    //TODO remove from this, refactoring!
    private HttpPost mPost;
    final static String LOG_TAG = SentsVkNotes.class.getSimpleName();

    public interface Callbacks {
        void onReturnId(Long id);
    }

    public SentsVkNotes (final Callbacks callbacks, final String url){
        Log.d(LOG_TAG, "Url " + VkOAuthHelper.mAccessToken);
        final Long[] mData = new Long[1];
        DataManager.loadData(new DataManager.Callback<Long>() {
                                 @Override
                                 public void onDataLoadStart() {
                                 }

                                 @Override
                                 public void onDone(Long data) {
                                     Log.d(LOG_TAG, "Sent Note" + data);
                                     callbacks.onReturnId(data);

                                 }

                                 @Override
                                 public void onError(Exception e) {
                                     onError(e);
                                 }
                             },
                Api.VKNOTES_GET + url + "&text=" + Api.MAIN_URL + url +"&access_token=" + VkOAuthHelper.mAccessToken,
                new HttpDataSource(),
                new NoteProcessor());
//        new AsyncTask() {
//            @Override
//            protected void onPostExecute(Object processingResult) {
//                super.onPostExecute(processingResult);
//            }
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                mClient = new DefaultHttpClient();
//            }
//            @Override
//            protected Object doInBackground(Object[] params) throws Exception {
//                mPost = new HttpPost(Api.VKNOTES_GET + url + "&text=" + Api.MAIN_URL + url +"&access_token=" + VkOAuthHelper.mAccessToken);//EncrManager.decrypt(getActivity(), mAm.getUserData(sAccount, "Token")));
//                mClient.execute(mPost);
//                //  content.findViewById(android.R.id.progress).setVisibility(View.GONE);
//                return null;
//            }
//            @Override
//            protected void onPostException(Exception e) {
//                Log.d(LOG_TAG, "Error = " + e);
//
//                //    onError(e);
//            }
//        }.execute();
    }
}
