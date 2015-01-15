package by.evgen.android.apiclient.helper;

import android.graphics.Bitmap;


import by.evgen.android.apiclient.Api;
import by.evgen.android.apiclient.auth.VkOAuthHelper;
import by.evgen.android.apiclient.bo.Category;
import by.evgen.android.apiclient.processing.BitmapProcessor;
import by.evgen.android.apiclient.processing.FotoIdUrlProcessor;
import by.evgen.android.apiclient.source.VkDataSource;
import by.evgen.android.apiclient.utils.Log;
import by.evgen.android.imageloader.CircleMaskedBitmap;

import java.util.List;

/**
 * Created by User on 20.12.2014.
 */
public class LoadVkUserData implements ManagerDownload.Callback<List<Category>> {

    final static String LOG_TAG = LoadVkUserData.class.getSimpleName();
    private Callbacks mCallbacks;

    public LoadVkUserData(Callbacks callbacks){
        String url = Api.VKFOTOS_GET + VkOAuthHelper.mAccessToken;
        ManagerDownload.load(this,
                url,
                new VkDataSource(),
                new FotoIdUrlProcessor());
        mCallbacks = callbacks;
    }

    public static interface Callbacks {
        void onUserData(Bitmap foto, String first, String last);
    }

   @Override
    public void onPreExecute() {

    }

    @Override
    public void onPostExecute(List<Category> data) {
        Category item = data.get(0);
        String url = item.getUrlFoto();
        final String first = item.getFirstName();
        final String last = item.getLastName();
        Log.text(this.getClass(), "Load url " + url);
        ManagerDownload.load(new ManagerDownload.Callback<Bitmap>() {

                                 @Override
                                 public void onPreExecute() {

                                 }

                                 @Override
                                 public void onPostExecute(Bitmap bitmap) {
                                     bitmap = CircleMaskedBitmap.getCircleMaskedBitmapUsingShader(bitmap, 100);
                                     mCallbacks.onUserData(bitmap, first, last);
                                 }

                                 @Override
                                 public void onError(Exception e) {
                                     //TODO
                                 }
                             },
                url,
                new VkDataSource(),
                new BitmapProcessor());
    }

   @Override
    public void onError(Exception e) {
        //TODO
    }

}
