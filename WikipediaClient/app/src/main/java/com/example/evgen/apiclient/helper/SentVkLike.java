package com.example.evgen.apiclient.helper;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.evgen.apiclient.Api;
import com.example.evgen.apiclient.auth.VkOAuthHelper;
import com.example.evgen.apiclient.os.AsyncTask;
import com.example.evgen.apiclient.processing.StringProcessor;
import com.example.evgen.apiclient.source.HttpDataSource;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

/**
 * Created by evgen on 13.01.2015.
 */
public class SentVkLike {
    //TODO remove from this, refactoring!
    private HttpClient mClient;
    //TODO remove from this, refactoring!
    private HttpPost mPost;
    final static String LOG_TAG = SentVkLike.class.getSimpleName();


    public SentVkLike (final Context context, final String url){
        Log.d(LOG_TAG, "Url " + VkOAuthHelper.mAccessToken);
        DataManager.loadData(new DataManager.Callback() {
            @Override
            public void onDataLoadStart() {

            }

            @Override
            public void onDone(Object data) {

            }

            @Override
            public void onError(Exception e) {

            }
        }, url, new HttpDataSource(), new StringProcessor());
//        mClient = new DefaultHttpClient();
//       //EncrManager.decrypt(getActivity(), mAm.getUserData(sAccount, "Token")));
//        try {
//            mPost = new HttpPost(url);
//            mClient.execute(mPost);
//            Toast.makeText(context, "Like added", Toast.LENGTH_SHORT).show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

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
//                mPost = new HttpPost(url);//EncrManager.decrypt(getActivity(), mAm.getUserData(sAccount, "Token")));
//                mClient.execute(mPost);
//                Toast.makeText(context, "Like added", Toast.LENGTH_SHORT).show();
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
