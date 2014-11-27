package com.example.evgen.apiclient.auth;

import android.app.Activity;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import org.apache.http.auth.AuthenticationException;


/**
 * Created by User on 30.10.2014.
 */
public class VkOAuthHelper {

    public static interface Callbacks {

        void onError(Exception e);

        void onSuccess();

    }

    //TODO don't do it in your project
    private static String sToken;

    public static final String REDIRECT_URL = "https://oauth.vk.com/blank.html";
    public static final String AUTORIZATION_URL = "https://oauth.vk.com/authorize?client_id=4613222&scope=offline,wall,photos,status,messages,notes&redirect_uri=" + REDIRECT_URL + "&display=touch&response_type=token";
    private static final String TAG = VkOAuthHelper.class.getSimpleName();
    public static String mAccessToken;

    public static String sign(String url) {
        if (url.contains("?")) {
            return url + "&access_token="+sToken;
        } else {
            return url + "?access_token="+sToken;
        }
    }

    public static boolean isLogged() {
        return !TextUtils.isEmpty(sToken);
    }

    public static boolean proceedRedirectURL(Activity activity, String url, Callbacks callbacks) {
         if (url.startsWith(REDIRECT_URL)) {
            Uri uri = Uri.parse(url);
            String fragment = uri.getFragment();
            Uri parsedFragment = Uri.parse("http://temp.com?" + fragment);
            String accessToken = parsedFragment.getQueryParameter("access_token");
            mAccessToken = parsedFragment.getQueryParameter("access_token");
            if (!TextUtils.isEmpty(mAccessToken)) {
                Log.d(TAG, "token " + mAccessToken);
                sToken = accessToken;
                callbacks.onSuccess();
                return true;
            } else {
                String error = parsedFragment.getQueryParameter("error");
                String errorDescription = parsedFragment.getQueryParameter("error_description");
                String errorReason = parsedFragment.getQueryParameter("error_reason");
                if (!TextUtils.isEmpty(error)) {
                    callbacks.onError(new AuthenticationException(error+", reason : " + errorReason +"("+errorDescription+")"));
                    return false;
                } else {
                    //WTF?
                }
            }
        }
        return false;



    }
}