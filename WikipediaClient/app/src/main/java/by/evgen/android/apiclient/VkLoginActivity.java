package by.evgen.android.apiclient;

import android.accounts.Account;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import by.evgen.android.apiclient.auth.VkOAuthHelper;

/**
 * Created by User on 30.10.2014.
 */
//TODO
public class VkLoginActivity extends ActionBarActivity implements VkOAuthHelper.Callbacks {

    private static final String TAG = VkLoginActivity.class.getSimpleName();
    private WebView mWebView;
    public static final String ACCOUNT_TYPE = "com.example.evgen.apiclient.account";

    public static final String AUTHORITY = "com.example.evgen.apiclient";

    public static Account sAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vk_login);
//        getSupportActionBar().hide();
        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.setWebViewClient(new VkWebViewClient());
        mWebView.loadUrl(VkOAuthHelper.AUTORIZATION_URL);
    }


    @Override
    public void onError(Exception e) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(e.getMessage())
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setResult(RESULT_CANCELED);
                        finish();
                    }
                })
                .create()
                .show();
    }

    @Override
    public void onSuccess() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onUserId(String id) {

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
            if (VkOAuthHelper.proceedRedirectURL(VkLoginActivity.this, url, VkLoginActivity.this)) {
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
