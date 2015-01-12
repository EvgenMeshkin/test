package com.example.evgen.apiclient.helper;

import android.util.Log;

import com.example.evgen.apiclient.Api;
import com.example.evgen.apiclient.auth.VkOAuthHelper;
import com.example.evgen.apiclient.os.AsyncTask;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Created by evgen on 13.01.2015.
 */
public class SentVkLike {
    //TODO remove from this, refactoring!
    private HttpClient mClient;
    //TODO remove from this, refactoring!
    private HttpPost mPost;
    final static String LOG_TAG = SentVkLike.class.getSimpleName();


    public SentVkLike (final String url){
        Log.d(LOG_TAG, "Url " + VkOAuthHelper.mAccessToken);

        new AsyncTask() {
            @Override
            protected void onPostExecute(Object processingResult) {
                super.onPostExecute(processingResult);
            }
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mClient = new DefaultHttpClient();
            }
            @Override
            protected Object doInBackground(Object[] params) throws Exception {
                mPost = new HttpPost(url);//EncrManager.decrypt(getActivity(), mAm.getUserData(sAccount, "Token")));
                mClient.execute(mPost);
                //  content.findViewById(android.R.id.progress).setVisibility(View.GONE);
                return null;
            }
            @Override
            protected void onPostException(Exception e) {
                Log.d(LOG_TAG, "Error = " + e);

                //    onError(e);
            }
        }.execute();
    }
}
