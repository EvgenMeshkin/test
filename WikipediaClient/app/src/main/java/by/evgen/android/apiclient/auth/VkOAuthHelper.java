package by.evgen.android.apiclient.auth;

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

        void onUserId(String id);

    }

    //TODO don't do it in your project
    private static String sToken;
    private static String mUrl;
    public static final String REDIRECT_URL = "https://oauth.vk.com/blank.html";
    public static final String AUTORIZATION_URL = "https://oauth.vk.com/authorize?client_id=4613222&scope=offline,wall,photos,status,messages,notes&redirect_uri=" + REDIRECT_URL + "&display=mobile&response_type=token";
    private static final String TAG = VkOAuthHelper.class.getSimpleName();
    public static String mAccessToken;

    public static String sign(String url) {
        if (url.contains("?")) {
            return url + "&access_token=" + sToken;
        } else {
            return url + "?access_token=" + sToken;
        }
    }

    public static boolean isLogged() {
        return !TextUtils.isEmpty(sToken);
    }

    public static boolean proceedRedirectURL(Activity activity, String url, Callbacks callbacks) {
        if (url.startsWith(REDIRECT_URL)) {
            mUrl = url;
            Uri uri = Uri.parse(url);
            String fragment = uri.getFragment();
            Uri parsedFragment = Uri.parse("http://temp.com?" + fragment);
            String accessToken = parsedFragment.getQueryParameter("access_token");
            String userId = parsedFragment.getQueryParameter("user_id");
            mAccessToken = parsedFragment.getQueryParameter("access_token");
            if (!TextUtils.isEmpty(mAccessToken)) {
                Log.d(TAG, "token " + mAccessToken);
                Log.d(TAG, "UserId " + userId);
                sToken = accessToken;
                callbacks.onUserId(userId);
                callbacks.onSuccess();
                return true;
            } else {
                String error = parsedFragment.getQueryParameter("error");
                String errorDescription = parsedFragment.getQueryParameter("error_description");
                String errorReason = parsedFragment.getQueryParameter("error_reason");
                if (!TextUtils.isEmpty(error)) {
                    callbacks.onError(new AuthenticationException(error + ", reason : " + errorReason + "(" + errorDescription + ")"));
                    return false;
                } else {
                    //WTF?
                }
            }
        }
        return false;
    }

    public static boolean procedId(Activity activity, Callbacks callbacks) {
        if (!mUrl.equals(null) && mUrl.startsWith(REDIRECT_URL)) {
            Uri uri = Uri.parse(mUrl);
            String fragment = uri.getFragment();
            Uri parsedFragment = Uri.parse("http://temp.com?" + fragment);
            String accessToken = parsedFragment.getQueryParameter("access_token");
            String userId = parsedFragment.getQueryParameter("user_id");
            mAccessToken = parsedFragment.getQueryParameter("access_token");
            if (!TextUtils.isEmpty(mAccessToken)) {
                Log.d(TAG, "token " + mAccessToken);
                Log.d(TAG, "UserId " + userId);
                sToken = accessToken;
                callbacks.onUserId(userId);
                callbacks.onSuccess();
                return true;
            } else {
                String error = parsedFragment.getQueryParameter("error");
                String errorDescription = parsedFragment.getQueryParameter("error_description");
                String errorReason = parsedFragment.getQueryParameter("error_reason");
                if (!TextUtils.isEmpty(error)) {
                    callbacks.onError(new AuthenticationException(error + ", reason : " + errorReason + "(" + errorDescription + ")"));
                    return false;
                } else {
                    //WTF?
                }
            }
        }
        return false;
    }

}
