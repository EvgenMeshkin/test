package com.example.evgen.apiclient.account;

import android.accounts.AccountAuthenticatorActivity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.evgen.apiclient.R;
import com.example.evgen.apiclient.VkLoginActivity;
import com.example.evgen.apiclient.auth.VkOAuthHelper;

/**
 * Created by evgen on 04.11.2014.
 */
public class AuthenticatorActivity extends AccountAuthenticatorActivity {


//    @Override
//    public void onCreate(Bundle icicle) {
//        //TODO login activity
//        super.onCreate(icicle);
//        finish();
//    }


    private static final String TAG = VkLoginActivity.class.getSimpleName();
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vk_login);
        //getSupportActionBar().hide();
        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.setWebViewClient(new VkWebViewClient());
        mWebView.loadUrl(VkOAuthHelper.AUTORIZATION_URL);


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
            if (!VkOAuthHelper.proceedRedirectURL(AuthenticatorActivity.this, url)) {
                //view.loadUrl(url);
                return false;
            } else {
                Log.d(TAG, "overr redr");
                view.setVisibility(View.INVISIBLE);
                Log.d(TAG, "Parsing url" + url);
                setResult(RESULT_OK);
                finish();
                return true;
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



