package by.evgen.android.apiclient.fragments;



import android.app.ProgressDialog;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import by.evgen.android.apiclient.Api;
import by.evgen.android.apiclient.R;
import by.evgen.android.apiclient.bo.Category;
import by.evgen.android.apiclient.bo.NoteGsonModel;
import by.evgen.android.apiclient.helper.ManagerDownload;
import by.evgen.android.apiclient.processing.ContentsArrayProcessor;
import by.evgen.android.apiclient.processing.MobileViewProcessor;
import by.evgen.android.apiclient.source.HttpDataSource;
import by.evgen.android.apiclient.source.VkDataSource;
import by.evgen.android.apiclient.utils.FindResponder;

import java.util.List;

/**
 * Created by User on 22.10.2014.
 */

//TODO check with Default WebViewFragment
public class DetailsFragment extends Fragment implements ManagerDownload.Callback<List<Category>> {

    private View content;
    final static String LOG_TAG = "DetailsFragment";
    private MobileViewProcessor mMobileViewProcessor = new MobileViewProcessor();
    private NoteGsonModel obj;
    private ProgressBar mProgress;

    //TODO remove static
    private static WebView mWebView;
    private static String mUrl;
    private static List mData;
    //TODO remove static
    private static String mTextHtml;

    private String mHistory;
    private ProgressDialog pd;
    private final Uri WIKI_URI = Uri
            .parse("content://com.example.evgenmeshkin.GeoData/geodata");
    private final String WIKI_NAME = "name";
    private final String WIKI_DATE = "wikidate";
    private final String WIKI_EMAIL = "koordinaty";

    public interface Callbacks {
        void onSetContents(List data);
    }

    void setListData(List data) {
        Callbacks callbacks = getCallbacks();
        callbacks.onSetContents(data);
    }

    private Callbacks getCallbacks() {
        return FindResponder.findFirstResponder(this, Callbacks.class);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pd = new ProgressDialog(getActivity());
        pd.setMessage("Please wait Loading...");
        pd.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        content = inflater.inflate(R.layout.fragment_details, container, false);
        //TODO create variable
        if (getArguments() != null) {
            obj = (NoteGsonModel) getArguments().getParcelable("key");
        }
        mHistory = obj.getTitle().replaceAll(" ", "_");
        content.findViewById(android.R.id.progress).setVisibility(View.VISIBLE);
        final HttpDataSource dataSource = getHttpDataSource();
        final MobileViewProcessor processor = getProcessor();
        //TODO use URLEncoder, URLDecoder
        String url = Api.MOBILE_GET + obj.getTitle().replaceAll(" ", "%20");
        update(dataSource, processor, url);
        return content;
    }

    private MobileViewProcessor getProcessor() {
        return mMobileViewProcessor;
    }

    private HttpDataSource getHttpDataSource() {
        return VkDataSource.get(getActivity());
    }

    private void update(HttpDataSource dataSource, MobileViewProcessor processor, String url) {
        //TODO make custom log that can be disabled for release
        Log.d(LOG_TAG, getUrl() + obj.getTitle().replaceAll(" ", "%20"));
        //TODO todo string encode/decode values
        ManagerDownload.load(this,
                url,
                dataSource,
                processor);
    }

    private String getUrl() {
        return Api.MOBILE_GET;
    }

    @Override
    public void onPreExecute() {
        content.findViewById(android.R.id.progress).setVisibility(View.VISIBLE);
    }

    @Override
    public void onPostExecute(List<Category> data) {
        mTextHtml = "";
        ContentValues cv = new ContentValues();
        cv.put(WIKI_NAME, mHistory);
        cv.put(WIKI_DATE, new java.util.Date().getTime());
        //getActivity().getContentResolver().delete(WIKI_URI, null, null);
        if (!cv.equals(null)) {
            getActivity().getContentResolver().insert(WIKI_URI, cv);
        }
        ManagerDownload.load(new ManagerDownload.Callback<List<String>>() {
                                 @Override
                                 public void onPreExecute() {

                                 }

                                 @Override
                                 public void onPostExecute(List<String> data) {
                                     mData = data;
                                     setListData(data);
                                     Log.d(LOG_TAG, data.toString());
                                 }

                                 @Override
                                 public void onError(Exception e) {
//                                     onError(e);
                                 }
                             },
                Api.CONTENTS_GET + mHistory,
                getHttpDataSource(),
                new ContentsArrayProcessor());


        if (data == null || data.isEmpty()) {
            //TODO empty!!! this is not error!!!
            //this is not error state
            onError(new NullPointerException("No data"));
        } else {

            for (int i = 0; i < data.size(); i++) {
                mTextHtml = mTextHtml + data.get(i).getText();
            }
            Log.d(LOG_TAG, "STR =" + mTextHtml);
            mWebView = (WebView) content.findViewById(R.id.webView);
            WebSettings webSettings = mWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setBuiltInZoomControls(true);
            webSettings.setDisplayZoomControls(false);
            mWebView.setWebViewClient(new WikiWebViewClient());
            mWebView.loadDataWithBaseURL("https://en.wikipedia.org/", mTextHtml, "text/html", "utf-8", null);
        }
    }

    public static void setListener(Integer position) {
        mWebView.loadDataWithBaseURL("https://en.wikipedia.org/" + "#" + mData.get(position).toString().replaceAll(" ", "_"), mTextHtml, "text/html", "utf-8", null);
    }

    @Override
    public void onError(Exception e) {
        Log.d(LOG_TAG, "onError");
//        mProgress.setVisibility(View.GONE);
    }


    //TODO create new activity
    private class WikiWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            final HttpDataSource dataSource = getHttpDataSource();
            final MobileViewProcessor processor = getProcessor();
            Integer position = url.lastIndexOf("/");
            mHistory = url.substring(position + 1);
            String newUrl = Api.MOBILE_GET + mHistory;
            Log.d(LOG_TAG, "newurl =" + newUrl);
            update(dataSource, processor, newUrl);
            content.findViewById(android.R.id.progress).setVisibility(View.VISIBLE);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            content.findViewById(android.R.id.progress).setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            content.findViewById(android.R.id.progress).setVisibility(View.GONE);
            if (pd.isShowing()) {
                pd.dismiss();
            }
        }
    }

}