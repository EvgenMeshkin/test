package com.example.evgen.apiclient.fragments;



import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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
import com.example.evgen.apiclient.processing.ContentsArrayProcessor;
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
    private static WebView mWebView;
    private static String mUrl;
    private static List mData;
    //TODO remove
 //   public DetailsFragment(){}
    private ProgressDialog pd;

    public interface Callbacks {
        void onSetContents (List data);
    }

    //TODO refactoring
    public static <T> T findFirstResponderFor(Fragment fragment, Class<T> clazz) {
        FragmentActivity activity = fragment.getActivity();
        if (activity == null)
            return null;
        if (clazz.isInstance(activity)) {
            return clazz.cast(activity);
        }
        Fragment parentFragment = fragment.getParentFragment();
        while (parentFragment != null) {
            if (clazz.isInstance(parentFragment)) {
                return clazz.cast(parentFragment);
            }
            parentFragment = parentFragment.getParentFragment();
        }
        return null;
    }

    void setListData(List data) {
        Callbacks callbacks = getCallbacks();
        callbacks.onSetContents(data);
    }

    private Callbacks getCallbacks() {
        return findFirstResponderFor(this, Callbacks.class);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pd = new ProgressDialog(getActivity());
        pd.setMessage("Please wait Loading...");
        pd.show();


    }


//    public static DetailsFragment newInstance(int index) {
//        DetailsFragment f = new DetailsFragment();
//        // Supply index input as an argument.
//        Bundle args = new Bundle();
//        args.putInt("index", index);
//        f.setArguments(args);
//        return f;
//    }

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

        DataManager.loadData(new DataManager.Callback<List<String>>() {
                                 @Override
                                 public void onDataLoadStart() {

                                 }

                                 @Override
                                 public void onDone(List<String> data) {
                                     mData = data;
                                     setListData(data);
                                     Log.d(LOG_TAG, data.toString());
                                 }

                                 @Override
                                 public void onError(Exception e) {
//                                     onError(e);
                                 }
                             },
                Api.CONTENTS_GET + obj.getTitle().replaceAll(" ", "_"),
                getHttpDataSource(),
                new ContentsArrayProcessor());

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
        //TODO make custom log that can be disabled for release
        Log.d(LOG_TAG,getUrl() + obj.getTitle().replaceAll(" ", "%20"));
        //TODO todo string encode/decode values
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
            //TODO empty!!! this is not error!!!
            //this is not error state
            onError(new NullPointerException("No data"));
        }else {
            mUrl = data.get(0).getURL().replace("en.", "en.m.");
            mWebView = (WebView) content.findViewById(R.id.webView);
            mWebView.setWebViewClient(new HelloWebViewClient());
            //TODO data.get(0) ?!!!
            mWebView.loadUrl(mUrl);
           // mWebView.loadData("<h3><span class=\\\"mw-headline\\\" id=\\\"New_singer-era.2C_hiatus_and_first_reunion_.281991.E2.80.932003.29\\\">New singer-era, hiatus and first reunion (1991\\u20132003)</span><span class=\\\"mw-editsection\\\"><span class=\\\"mw-editsection-bracket\\\">[</span><a href=\\\"/w/index.php?title=Candlemass&amp;action=edit&amp;section=3\\\" title=\\\"Edit section: New singer-era, hiatus and first reunion (1991\\u20132003)\\\">edit</a><span class=\\\"mw-editsection-bracket\\\">]</span></span></h3>\\n<p>After Marcolin left, Candlemass recruited vocalist <a href=\\\"/wiki/Thomas_Vikstr%C3%B6m\\\" title=\\\"Thomas Vikstr\\u00f6m\\\">Thomas Vikstr\\u00f6m</a> and recorded <i><a href=\\\"/wiki/Chapter_VI_(album)\\\" title=\\\"Chapter VI (album)\\\">Chapter VI</a></i> (1992). The band then toured in support of that album. By 1994, Candlemass had called it quits, partly because <i>Chapter VI</i> was unsuccessful<sup class=\\\"noprint Inline-Template Template-Fact\\\" style=\\\"white-space:nowrap;\\\">[<i><a href=\\\"/wiki/Wikipedia:Citation_needed\\\" title=\\\"Wikipedia:Citation needed\\\"><span title=\\\"This claim needs references to reliable sources. (March 2007)\\\">citation needed</span></a></i>]</sup> and partially because Edling had formed another project under the name of <a href=\\\"/wiki/Abstrakt_Algebra\\\" title=\\\"Abstrakt Algebra\\\">Abstrakt Algebra</a>. With Abstrakt Algebra not doing well, Leif suddenly recruited a new line-up under the name of Candlemass and recorded the album <i><a href=\\\"/wiki/Dactylis_Glomerata\\\" title=\\\"Dactylis Glomerata\\\">Dactylis Glomerata</a></i>,<sup class=\\\"noprint Inline-Template Template-Fact\\\" style=\\\"white-space:nowrap;\\\">[<i><a href=\\\"/wiki/Wikipedia:Citation_needed\\\" title=\\\"Wikipedia:Citation needed\\\"><span title=\\\"Sept. 2008 (September 2008)\\\">citation needed</span></a></i>]</sup> which was a combination of songs for a new Abstrakt Algebra CD and some new material.<sup class=\\\"noprint Inline-Template Template-Fact\\\" style=\\\"white-space:nowrap;\\\">[<i><a href=\\\"/wiki/Wikipedia:Citation_needed\\\" title=\\\"Wikipedia:Citation needed\\\"><span title=\\\"Sept. 2008 (September 2008)\\\">citation needed</span></a></i>]</sup> A year later the album <i><a href=\\\"/wiki/From_the_13th_Sun\\\" title=\\\"From the 13th Sun\\\">From the 13th Sun</a></i> was released.</p>\\n<p>In 2002, the members of a past Candlemass line-up reunited. They performed some well-received live shows<sup class=\\\"noprint Inline-Template Template-Fact\\\" style=\\\"white-space:nowrap;\\\">[<i><a href=\\\"/wiki/Wikipedia:Citation_needed\\\" title=\\\"Wikipedia:Citation needed\\\"><span title=\\\"This claim needs references to reliable sources. (January 2010)\\\">citation needed</span></a></i>]</sup> and released another live album. Other albums released by the reformed band were <a href=\\\"/wiki/Remaster\\\" title=\\\"Remaster\\\">remastered</a> versions of <i>Epicus Doomicus Metallicus, Nightfall, Ancient Dreams</i>, and <i>Tales of Creation</i>. A <a href=\\\"/wiki/DVD\\\" title=\\\"DVD\\\">DVD</a> called <i><a href=\\\"/wiki/Documents_of_Doom\\\" title=\\\"Documents of Doom\\\">Documents of Doom</a></i> was released as well. The band was working on a new album and recorded some new songs while searching for a record label when differences arose again, resulting in Candlemass disbanding a second time. In the meantime, Leif Edling started a new project, <a href=\\\"/wiki/Krux\\\" title=\\\"Krux\\\">Krux</a>, with former Abstrakt Algebra singer <a href=\\\"/wiki/Mats_Lev%C3%A9n\\\" title=\\\"Mats Lev\\u00e9n\\\">Mats Lev\\u00e9n</a> and two members of <a href=\\\"/wiki/Entombed_(band)\\\" title=\\\"Entombed (band)\\\">Entombed</a>.</p>","text/html" ,  null);
            Log.d(LOG_TAG, data.get(0).getURL().replace("en.", "en.m."));
        }
     }

    public static void setListener(Integer position){
       mWebView.loadUrl(mUrl + "#" + mData.get(position));
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