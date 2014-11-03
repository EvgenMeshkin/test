package com.example.evgen.apiclient.auth;

import android.app.Activity;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by User on 30.10.2014.
 */
public class VkOAuthHelper {

    public static final String REDIRECT_URL = "https://oauth.vk.com/blank.html";
    public static final String AUTORIZATION_URL = "https://oauth.vk.com/authorize?client_id=4613222&scope=offline,wall,photos,status,messages,notes&redirect_uri=" + REDIRECT_URL + "&display=touch&response_type=token";
    private static final String TAG = VkOAuthHelper.class.getSimpleName();
    public static String mAccessToken;

    public static boolean proceedRedirectURL(Activity activity, String url) {
        //https://oauth.vk.com/blank.html#
        //fragment: access_token=token&expires_in=0&user_id=308327
        //https://oauth.vk.com/blank.html#error=
        if (url.startsWith(REDIRECT_URL)) {
            Uri uri = Uri.parse(url);
            String fragment = uri.getFragment();
            Uri parsedFragment = Uri.parse("http://temp.com?" + fragment);
            mAccessToken = parsedFragment.getQueryParameter("access_token");
            if (!TextUtils.isEmpty(mAccessToken)) {
                Log.d(TAG, "token " + mAccessToken);

                return true;
            } else {
                //TODO check access denied/finish
                //#error=access_denied&error_reason=user_denied&error_description=User denied your request
            }

        }
        return false;
    }
}