package com.example.evgen.apiclient.fragments;

import android.annotation.TargetApi;


import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.evgen.apiclient.Api;
import com.example.evgen.apiclient.R;
import com.example.evgen.apiclient.auth.VkOAuthHelper;
import com.example.evgen.apiclient.bo.Category;
import com.example.evgen.apiclient.bo.NoteGsonModel;
import com.example.evgen.apiclient.helper.DataManager;
import com.example.evgen.imageloader.ImageLoader;
import com.example.evgen.apiclient.os.AsyncTask;
import com.example.evgen.apiclient.processing.CategoryArrayProcessor;
import com.example.evgen.apiclient.service.GpsLocation;
import com.example.evgen.apiclient.source.HttpDataSource;
import com.example.evgen.apiclient.source.VkDataSource;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.List;

/**
 * Created by User on 30.10.2014.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class WikiFragment extends ListFragment implements DataManager.Callback<List<Category>>, GpsLocation.Callbacks {
    private ArrayAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<Category> mData;
    private HttpClient mClient;
    private HttpPost mPost;
    private TextView mTitle;
    private TextView mContent;
    private View content;
    private TextView empty;
    private static String mKor;
    private HttpDataSource dataSource;
    private CategoryArrayProcessor processor;
    private ImageLoader imageLoader;
    Cursor mCursor;

    final Uri WIKI_URI = Uri
            .parse("content://com.example.evgenmeshkin.GeoData/geodata");

    final String WIKI_NAME = "name";
    final String WIKI_KOR = "koordinaty";
    int mCurCheckPosition = 0;
    final static String LOG_TAG = WikiFragment.class.getSimpleName();
    private CategoryArrayProcessor mCategoryArrayProcessor = new CategoryArrayProcessor();

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

    @Override
    public void onShowKor(String latitude) {
        mKor = latitude;
        Log.d(LOG_TAG, "latitude="+mKor);
        update(dataSource, processor);
    }

    public interface Callbacks {
        void onShowDetails(int index, NoteGsonModel note);
        boolean isDualPane();
        void onErrorA(Exception e);
    }

    void showDetails(int index, NoteGsonModel note) {
        mCurCheckPosition = index;
        Callbacks callbacks = getCallbacks();
        if (callbacks.isDualPane()) {
            getListView().setItemChecked(index, true);
        }
        callbacks.onShowDetails(index, note);
    }

    private Callbacks getCallbacks() {
        return findFirstResponderFor(this, Callbacks.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        content = inflater.inflate(R.layout.fragment_wiki, null);
        mSwipeRefreshLayout = (SwipeRefreshLayout) content.findViewById(R.id.swipe_container);
        dataSource = getHttpDataSource();
        processor = getProcessor();
        empty = (TextView) content.findViewById(android.R.id.empty);
        empty.setVisibility(View.GONE);
        GpsLocation gpsLocation = new GpsLocation();
        gpsLocation.getloc(this,getActivity());
        imageLoader = ImageLoader.get(getActivity());
        update(dataSource,processor);
        ListView listView = (ListView) content.findViewById(android.R.id.list);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        imageLoader.resume();
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                        imageLoader.pause();
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                        imageLoader.pause();
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                update(dataSource, processor);
            }
        });
        return content;
    }

    private CategoryArrayProcessor getProcessor() {
        return mCategoryArrayProcessor;
    }

    private HttpDataSource getHttpDataSource() {
        return VkDataSource.get(getActivity());
    }

    private void update(HttpDataSource dataSource, CategoryArrayProcessor processor) {
        DataManager.loadData(this,
                getUrl(),
                dataSource,
                processor);
    }

    private String getUrl() {
        mKor = Api.GEOSEARCH_GET + "53.677226|23.8489383";//mKor;
        Log.d(LOG_TAG, "mKor="+mKor);
        return mKor;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
         super.onActivityCreated(savedInstanceState);
        // Populate list with our static array of titles.
        if (savedInstanceState != null) {
            // Restore last state for checked position.
            mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
        }
        if (getCallbacks().isDualPane()) {
            // In dual-pane mode, the list view highlights the selected item.
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }
    }

    @Override
    public void onDataLoadStart() {
        if (!mSwipeRefreshLayout.isRefreshing()) {
            content.findViewById(android.R.id.progress).setVisibility(View.VISIBLE);
        }
            empty.setVisibility(View.GONE);
    }

    @Override
    public void onDone(List<Category> data) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        content.findViewById(android.R.id.progress).setVisibility(View.GONE);
        //data = null;
        if (data == null || data.isEmpty()) {
            content.findViewById(android.R.id.empty).setVisibility(View.VISIBLE);
            onError(new NullPointerException("No data"));
        }else {
            AdapterView listView = (AbsListView) content.findViewById(android.R.id.list);
            if (mAdapter == null) {
                mData = data;
                mAdapter = new ArrayAdapter<Category>(getActivity(), R.layout.adapter_item, android.R.id.text1, data) {
                    @Override
                    public View getView(final int position, View convertView, ViewGroup parent) {
                        if (convertView == null) {
                            convertView = View.inflate(getActivity(), R.layout.adapter_item, null);
                        }
                        Category item = getItem(position);
                        ContentValues cv = new ContentValues();
                        cv.put(WIKI_NAME, item.getTITLE());
                        cv.put(WIKI_KOR, item.getDIST());
                        Uri newUri = getActivity().getContentResolver().insert(WIKI_URI, cv);
                        Log.d(LOG_TAG, "insert, count : " + newUri.toString());
                        mCursor = getActivity().getContentResolver().query(newUri, null, null,
                                null, null);
                        mCursor.moveToFirst();
                        mTitle = (TextView) convertView.findViewById(android.R.id.text1);
                        mTitle.setText(mCursor.getString(mCursor.getColumnIndex("name")));
                        mContent = (TextView) convertView.findViewById(android.R.id.text2);
                        mContent.setText(mCursor.getString(mCursor.getColumnIndex("koordinaty")) + " м.");
                        mCursor.close();
                        final String urlImage = Api.IMAGEVIEW_GET + item.getTITLE().replaceAll(" ","%20");
                        convertView.setTag(item.getId());
                        final ImageView imageView = (ImageView) convertView.findViewById(android.R.id.icon);
                        final ProgressBar mProgress = (ProgressBar) convertView.findViewById(android.R.id.progress);
                        imageView.setImageBitmap(null);
                        imageView.setTag(urlImage);
                        imageLoader.displayImage(urlImage, imageView);
                        return convertView;
                    }
                };
                listView.setAdapter(mAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        final Category item = (Category) mAdapter.getItem(position);
                        NoteGsonModel note = new NoteGsonModel(item.getId(), item.getTITLE(), item.getNS());
                        new AsyncTask() {
                            @Override
                            protected void onPostExecute(Object processingResult) {
                                super.onPostExecute(processingResult);
                                content.findViewById(android.R.id.progress).setVisibility(View.GONE);
                            }
                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                                content.findViewById(android.R.id.progress).setVisibility(View.VISIBLE);
                                mClient = new DefaultHttpClient();
                            }
                            @Override
                            protected Object doInBackground(Object[] params) throws Exception {
                                mPost = new HttpPost(Api.VKNOTES_GET + item.getTITLE().replaceAll(" ", "%20") +"&access_token=" + VkOAuthHelper.mAccessToken);//EncrManager.decrypt(getActivity(), mAm.getUserData(sAccount, "Token")));
                                mClient.execute(mPost);
                              //  content.findViewById(android.R.id.progress).setVisibility(View.GONE);
                                return null;
                            }
                            @Override
                            protected void onPostException(Exception e) {
                              onError(e);
                            }
                        }.execute();
                        showDetails(position, note);
                    }
                });

            } else {
                mData.clear();
                mData.addAll(data);
                mAdapter.notifyDataSetChanged();
            }
        }
}
    @Override
    public void onError(Exception e) {
        Log.d(LOG_TAG, "onError");
        content.findViewById(android.R.id.progress).setVisibility(View.GONE);
        content.findViewById(android.R.id.empty).setVisibility(View.GONE);
        TextView errorView = (TextView) content.findViewById(R.id.error);
        errorView.setVisibility(View.VISIBLE);
        errorView.setText(errorView.getText() + "\n" + e.getMessage());
        Callbacks callbacks = getCallbacks();
        callbacks.onErrorA(e);
    }
}