package com.example.evgen.apiclient.fragments;



import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.evgen.apiclient.Api;
import com.example.evgen.apiclient.R;
import com.example.evgen.apiclient.auth.VkOAuthHelper;
import com.example.evgen.apiclient.bo.Category;
import com.example.evgen.apiclient.bo.NoteGsonModel;
import com.example.evgen.apiclient.helper.DataManager;
import com.example.evgen.apiclient.processing.CategoryArrayProcessor;
import com.example.evgen.apiclient.processing.ViewArrayProcessor;
import com.example.evgen.apiclient.source.HttpDataSource;
import com.example.evgen.apiclient.source.VkDataSource;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;

import java.util.List;

/**
 * Created by User on 22.10.2014.
 */
public class DetailsFragment extends Fragment implements DataManager.Callback<List<Category>>{

    private View content;
    final static String LOG_TAG = "DetailsFragment";
    private ViewArrayProcessor mViewArrayProcessor = new ViewArrayProcessor();
    private NoteGsonModel obj;
    private ProgressBar mProgress;
    public DetailsFragment(){}
    private ProgressDialog pd;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pd = new ProgressDialog(getActivity());
        pd.setMessage("Please wait Loading...");
        pd.show();
    }


    public static DetailsFragment newInstance(int index) {
        DetailsFragment f = new DetailsFragment();
        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);
        return f;
    }

    public int getShownIndex() {
        return getArguments().getInt("index", 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            if (container == null) {
            return null;
        }
       content = inflater.inflate(R.layout.fragment_details, container, false);
        if(getArguments() != null) {
            obj = (NoteGsonModel) getArguments().getParcelable("key");
        }
        content.findViewById(android.R.id.progress).setVisibility(View.VISIBLE);
        final HttpDataSource dataSource = getHttpDataSource();
        final ViewArrayProcessor processor = getProcessor();
        update(dataSource, processor);
        return content;
    }

    private ViewArrayProcessor getProcessor() {
        return mViewArrayProcessor;
    }

    private HttpDataSource getHttpDataSource() {
        return VkDataSource.get(getActivity());
    }

    private void update(HttpDataSource dataSource, ViewArrayProcessor processor) {
        Log.d(LOG_TAG,getUrl() + obj.getTitle().replaceAll(" ", "%20"));
        DataManager.loadData(this,
                getUrl() + obj.getTitle().replaceAll(" ", "%20"),
                dataSource,
                processor);
    }

    private String getUrl() {
        return Api.URLVIEW_GET;
    }

    @Override
    public void onDataLoadStart() {
        content.findViewById(android.R.id.progress).setVisibility(View.VISIBLE);
    }

    @Override
    public void onDone(List<Category> data) {
        if (data == null || data.isEmpty()) {
            onError(new NullPointerException("No data"));
        }else {
            WebView mWebView = (WebView) content.findViewById(R.id.webView);
            mWebView.setWebViewClient(new HelloWebViewClient());
            mWebView.loadUrl(data.get(0).getURL().replace("en.","en.m."));
            Log.d(LOG_TAG, data.get(0).getURL().replace("en.","en.m."));
        }
     }

    @Override
    public void onError(Exception e) {
        Log.d(LOG_TAG, "onError");
        mProgress.setVisibility(View.GONE);
    }

    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url){
            view.loadUrl(url);
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