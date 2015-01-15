package by.evgen.android.apiclient.helper;

import android.content.Context;
import android.widget.Toast;

import by.evgen.android.apiclient.Api;
import by.evgen.android.apiclient.auth.VkOAuthHelper;
import by.evgen.android.apiclient.bo.Category;
import by.evgen.android.apiclient.processing.NoteProcessor;
import by.evgen.android.apiclient.processing.NotesAllProcessor;
import by.evgen.android.apiclient.source.HttpDataSource;
import by.evgen.android.apiclient.utils.Log;

import java.util.List;

/**
 * Created by evgen on 11.01.2015.
 */
public class SentsVkNotes implements ManagerDownload.Callback<List<Category>>{

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
        ManagerDownload.load(this,
                Api.VKNOTES_ALL_GET + VkOAuthHelper.mAccessToken,
                new HttpDataSource(),
                new NotesAllProcessor());
        Log.text(this.getClass(), "Url " + VkOAuthHelper.mAccessToken);

    }


    @Override
    public void onPreExecute() {

    }

    @Override
    public void onPostExecute(List<Category> data) {

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
            ManagerDownload.load(new ManagerDownload.Callback<Long>() {
                                     @Override
                                     public void onPreExecute() {
                                     }

                                     @Override
                                     public void onPostExecute(Long data) {
                                         mCallbacks.onReturnId(data);
                                         Toast.makeText(mContext, "Note added", Toast.LENGTH_SHORT).show();
                                     }

                                     @Override
                                     public void onError(Exception e) {
                                         onError(e);
                                     }
                                 },
                    Api.VKNOTES_GET + mBaseUrl + "&text=" + Api.MAIN_URL + mBaseUrl + "&access_token=" + VkOAuthHelper.mAccessToken,
                    new HttpDataSource(),
                    new NoteProcessor());

        }
    }

    @Override
    public void onError(Exception e) {

    }





}
