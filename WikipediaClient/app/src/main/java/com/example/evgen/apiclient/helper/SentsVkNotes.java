package com.example.evgen.apiclient.helper;

import android.util.Log;
import android.view.View;

import com.example.evgen.apiclient.Api;
import com.example.evgen.apiclient.auth.VkOAuthHelper;
import com.example.evgen.apiclient.os.AsyncTask;

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


    public SentsVkNotes (final String url){
        Log.d(LOG_TAG, "Url " + url);

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
                mPost = new HttpPost(Api.VKNOTES_GET + url.replaceAll(" ", "%20") +"&access_token=" + VkOAuthHelper.mAccessToken);//EncrManager.decrypt(getActivity(), mAm.getUserData(sAccount, "Token")));
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
