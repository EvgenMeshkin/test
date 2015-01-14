package by.evgen.android.apiclient.helper;

import android.content.Context;
import android.widget.Toast;

import by.evgen.android.apiclient.Api;
import by.evgen.android.apiclient.auth.VkOAuthHelper;
import by.evgen.android.apiclient.processing.LikeIsProcessor;
import by.evgen.android.apiclient.source.HttpDataSource;
import by.evgen.android.apiclient.utils.Log;

/**
 * Created by User on 12.01.2015.
 */
public class LikeVkNotes implements SentsVkNotes.Callbacks{

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
                                     Log.text(this.getClass(), "Sent" + data);
                                     if (data.equals("0")) {
                                         Log.text(this.getClass(), "Sent like");
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
