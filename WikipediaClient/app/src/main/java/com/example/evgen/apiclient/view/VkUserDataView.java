package com.example.evgen.apiclient.view;

import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.example.evgen.apiclient.Api;
import com.example.evgen.apiclient.auth.VkOAuthHelper;
import com.example.evgen.apiclient.bo.Category;
import com.example.evgen.apiclient.helper.DataManager;
import com.example.evgen.apiclient.processing.BitmapProcessor;
import com.example.evgen.apiclient.processing.CategoryArrayProcessor;
import com.example.evgen.apiclient.processing.FotoIdUrlProcessor;
import com.example.evgen.apiclient.source.HttpDataSource;
import com.example.evgen.apiclient.source.VkDataSource;

import java.util.List;

/**
 * Created by User on 20.12.2014.
 */
public class VkUserDataView  extends ActionBarActivity implements DataManager.Callback<List<Category>>, VkOAuthHelper.Callbacks{

    final static String LOG_TAG = VkUserDataView.class.getSimpleName();
    private Callbacks mCallbacks;

    public VkUserDataView (Callbacks callbacks){
        Log.d(LOG_TAG, "New VkUserDataView " );
        VkOAuthHelper.procedId(this, VkOAuthHelper.AUTORIZATION_URL, this);
        mCallbacks = callbacks;
    }

    public static interface Callbacks {

        void onUserData(Bitmap foto, String first, String last);

    }


    @Override
    public void onSuccess() {

    }

    @Override
    public void onUserId(String id) {
        Log.d(LOG_TAG, "Id= " + id);
        String url = Api.VKFOTOS_GET + id;
        DataManager.loadData(this,
                url,
                new VkDataSource(),
                new FotoIdUrlProcessor());
    }

    @Override
    public void onDataLoadStart() {

    }

    @Override
    public void onDone(List<Category> data) {
        Category item = data.get(0);
        String url = item.getURLFOTO();
        final String first = item.getFIRSTNAME();
        final String last = item.getLASTNAME();
        Log.d(LOG_TAG, "URL= " + url);

        DataManager.loadData(new DataManager.Callback<Bitmap>() {
                                 @Override
                                 public void onDataLoadStart() {

                                 }

                                 @Override
                                 public void onDone(Bitmap bitmap) {
                                     mCallbacks.onUserData(bitmap, first, last);
                                 }

                                 @Override
                                 public void onError(Exception e) {

                                 }
                             },
                url,
                new VkDataSource(),
                new BitmapProcessor());
    }


    @Override
    public void onError(Exception e) {

    }


}
