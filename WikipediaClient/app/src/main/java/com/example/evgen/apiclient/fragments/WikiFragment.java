package com.example.evgen.apiclient.fragments;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.example.evgen.apiclient.adapters.RecyclerWikiAdapter;
import com.example.evgen.apiclient.auth.VkOAuthHelper;
import com.example.evgen.apiclient.bo.Category;
import com.example.evgen.apiclient.bo.NoteGsonModel;
import com.example.evgen.apiclient.helper.DataManager;
import com.example.evgen.apiclient.helper.SentsVkNotes;
import com.example.evgen.apiclient.listener.RecyclerItemClickListener;
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
public class WikiFragment extends Fragment implements DataManager.Callback<List<Category>>, GpsLocation.Callbacks {
    private RecyclerWikiAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<Category> mData;
    private View content;
   //TODO already have in class related to location
    private static String mKor;
    private HttpDataSource dataSource;
    private CategoryArrayProcessor processor;
    private ImageLoader imageLoader;
    int mCurCheckPosition = 0;
    final static String LOG_TAG = WikiFragment.class.getSimpleName();
    private CategoryArrayProcessor mCategoryArrayProcessor = new CategoryArrayProcessor();
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;

    public interface Callbacks {
        void onShowDetails(int index, NoteGsonModel note);
        void onErrorA(Exception e);
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

    @Override
    public void onShowKor(String latitude) {
        mKor = latitude;
        Log.d(LOG_TAG, "latitude="+mKor);
        update(dataSource, processor);
    }

    void showDetails(int index, NoteGsonModel note) {
        mCurCheckPosition = index;
        Callbacks callbacks = getCallbacks();
        callbacks.onShowDetails(index, note);
    }

    private Callbacks getCallbacks() {
        return findFirstResponderFor(this, Callbacks.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        content = inflater.inflate(R.layout.fragment_wiki_recycler, null);
        mSwipeRefreshLayout = (SwipeRefreshLayout) content.findViewById(R.id.swipe_container);
        dataSource = getHttpDataSource();
        processor = getProcessor();
        mRecyclerView = (RecyclerView)content.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        mRecyclerView.setItemAnimator(itemAnimator);
        GpsLocation gpsLocation = new GpsLocation();
        gpsLocation.getloc(this,getActivity());
        update(dataSource,processor);
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

    //TODO remove hardcode! make customizable, create preference screen
    private String getUrl() {
        mKor = Api.GEOSEARCH_GET + "53.677226|23.8489383";//mKor;
        Log.d(LOG_TAG, "mKor="+mKor);
        return mKor;
    }

    @Override
    public void onDataLoadStart() {
        if (!mSwipeRefreshLayout.isRefreshing()) {
            content.findViewById(android.R.id.progress).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDone(List<Category> data) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        content.findViewById(android.R.id.progress).setVisibility(View.GONE);
        if (data == null || data.isEmpty()) {
            //TODO refactoring
            onError(new NullPointerException("No data"));
        }else {
            RecyclerView recyclerView = (RecyclerView)content.findViewById(R.id.recyclerView);
            if (mAdapter == null) {
                mAdapter = new RecyclerWikiAdapter(getActivity(), data);
                recyclerView.setAdapter(mAdapter); //      recyclerView.setItemAnimator(itemAnimator);r(mAdapter);
                mData = data;
                mRecyclerView.addOnItemTouchListener(
                        new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                            @Override public void onItemClick(View view, int position) {
                                final Category item = (Category) mData.get(position);
                                NoteGsonModel note = new NoteGsonModel(item.getId(), item.getTitle(), item.getNs());
                                showDetails(position, note);
                            }
                        })
                );
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
        Callbacks callbacks = getCallbacks();
        callbacks.onErrorA(e);
    }
}