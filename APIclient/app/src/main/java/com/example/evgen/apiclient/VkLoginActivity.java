package com.example.evgen.apiclient;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.evgen.apiclient.auth.VkOAuthHelper;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

/**
 * Created by User on 30.10.2014.
 */
public class VkLoginActivity extends ActionBarActivity {

    private static final String TAG = VkLoginActivity.class.getSimpleName();
    private WebView mWebView;
    public static final String ACCOUNT_TYPE = "com.example.evgen.apiclient.account";

    public static final String AUTHORITY = "com.example.evgen.apiclient";

    public static Account sAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vk_login);
        getSupportActionBar().hide();
        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.setWebViewClient(new VkWebViewClient());
        mWebView.loadUrl(VkOAuthHelper.AUTORIZATION_URL);
//        final AccountManager am = AccountManager.get(this);
//        if (sAccount == null) {
//            sAccount = new Account(getString(R.string.news), ACCOUNT_TYPE);
//        }
//        if (am.addAccountExplicitly(sAccount, getPackageName(), new Bundle())) {
//            ContentResolver.setSyncAutomatically(sAccount, AUTHORITY, true);
//        }

    }

    private class VkWebViewClient extends WebViewClient {

        public VkWebViewClient() {
            super();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.d(TAG, "page started " + url);
            showProgress();
            view.setVisibility(View.INVISIBLE);
        }



        /* (non-Javadoc)
         * @see android.webkit.WebViewClient#shouldOverrideUrlLoading(android.webkit.WebView, java.lang.String)
         */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(TAG, "overr " + url);
            if (VkOAuthHelper.proceedRedirectURL(VkLoginActivity.this, url)) {
                Log.d(TAG, "overr redr");
                view.setVisibility(View.INVISIBLE);
                Log.d(TAG, "Parsing url" + url);
                setResult(RESULT_OK);
                finish();
                return true;
            } else {
                //view.loadUrl(url);
                return false;
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            //showProgress("Error: " + description);
            view.setVisibility(View.VISIBLE);
            dismissProgress();
            Log.d(TAG, "error " + failingUrl);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.d(TAG, "finish " + url);
            if (url.contains("&amp;")) {
                url = url.replace("&amp;", "&");
                Log.d(TAG, "overr after replace " + url);
                view.loadUrl(url);
                return;
            }
            view.setVisibility(View.VISIBLE);
            //if (!VkOAuthHelper.proceedRedirectURL(VkLoginActivity.this, url, success)) {
            dismissProgress();
            //}
        }

    }

    private void dismissProgress() {
        findViewById(android.R.id.progress).setVisibility(View.GONE);
    }

    private void showProgress() {
        findViewById(android.R.id.progress).setVisibility(View.VISIBLE);
    }

}
