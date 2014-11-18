package com.example.evgen.apiclient.fragments;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
    private String[] viewsNames;
    private ArrayAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    public static final String URL = "https://en.wikipedia.org/w/api.php?action=query&prop=categories&format=json&titles=Albert%20Einstein";
    private List<Category> mData;
    private static final String TAG = VkOAuthHelper.class.getSimpleName();
    private HttpClient mClient;
    private HttpPost mPost;
    private TextView mTitle;
    private TextView mContent;
    public static final String ACCOUNT_PAS = "https://api.vk.com/method/";
    public static final String ACCOUNT_METHOD = "notes.add";
    public static final String ACCOUNT_TITLE = "Wikipedia";
    public static final String AUTHORITY = "com.example.evgen.apiclient";
    private View content;
    private TextView empty;
    int mCurCheckPosition = 0;
    final static String LOG_TAG = "DetailsFragment";
    private ViewArrayProcessor mViewArrayProcessor = new ViewArrayProcessor();
    NoteGsonModel obj;
    public DetailsFragment(){}

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
            // We have different layouts, and in one of them this
            // fragment's containing frame doesn't exist.  The fragment
            // may still be created from its saved state, but there is
            // no reason to try to create its view hierarchy because it
            // won't be displayed.  Note this is not needed -- we could
            // just run the code below, where we would create and return
            // the view hierarchy; it would just never be used.
            return null;
        }

        final View content = inflater.inflate(R.layout.fragment_details, container, false);
        if(getArguments() != null) {
            obj = (NoteGsonModel) getArguments().getParcelable("key");
            ((TextView)content.findViewById(android.R.id.text1)).setText(obj.getTitle());
            ((TextView)content.findViewById(android.R.id.text2)).setText(obj.getContent());
        }
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
        DataManager.loadData(this,
                getUrl() + obj.getTitle(),
                dataSource,
                processor);
    }

    private String getUrl() {
        return Api.URLVIEW_GET;
    }

    @Override
    public void onDataLoadStart() {

    }

    @Override
    public void onDone(List<Category> data) {
        if (data == null || data.isEmpty()) {
         //   content.findViewById(android.R.id.empty).setVisibility(View.VISIBLE);
            onError(new NullPointerException("No data"));
        }else {
            WebView mWebView = (WebView) content.findViewById(R.id.webView);
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            mWebView.loadUrl(data.toString());
            Log.d(LOG_TAG, data.toString());
//            final Category item = (Category) mAdapter.getItem(position);
//            NoteGsonModel note = new NoteGsonModel(item.getId(), item.getTITLE(), item.getNS());
//            AdapterView listView = (AbsListView) content.findViewById(android.R.id.list);
//            if (mAdapter == null) {
//                mData = data;
//                mAdapter = new ArrayAdapter<Category>(getActivity(), R.layout.adapter_item, android.R.id.text1, data) {
//                    @Override
//                    public View getView(int position, View convertView, ViewGroup parent) {
//                        if (convertView == null) {
//                            convertView = View.inflate(getActivity(), R.layout.adapter_item, null);
//                        }
//                        Category item = getItem(position);
//                        mTitle = (TextView) convertView.findViewById(android.R.id.text1);
//                        mTitle.setText(item.getTITLE());
//                        mContent = (TextView) convertView.findViewById(android.R.id.text2);
//                        mContent.setText(item.getNS());
//                        convertView.setTag(item.getId());
//                        return convertView;
//                    }
//                };
//            } else {
//                mData.clear();
//                mData.addAll(data);
//                mAdapter.notifyDataSetChanged();
//            }
        }

    }

    @Override
    public void onError(Exception e) {
        Log.d(LOG_TAG, "onError");
//        content.findViewById(android.R.id.progress).setVisibility(View.GONE);
//        content.findViewById(android.R.id.empty).setVisibility(View.GONE);
//        TextView errorView = (TextView) content.findViewById(R.id.error);
//        errorView.setVisibility(View.VISIBLE);
//        errorView.setText(errorView.getText() + "\n" + e.getMessage());
//        Callbacks callbacks = getCallbacks();
//        callbacks.onErrorA(e);
    }
}