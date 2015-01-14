package by.evgen.android.apiclient.fragments;


import android.os.Bundle;

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

import by.evgen.android.apiclient.Api;
import by.evgen.android.apiclient.adapters.RecyclerWikiAdapter;
import by.evgen.android.apiclient.bo.Category;
import by.evgen.android.apiclient.bo.NoteGsonModel;
import by.evgen.android.apiclient.helper.DataManager;
import by.evgen.android.apiclient.listener.RecyclerItemClickListener;
import by.evgen.android.apiclient.utils.FindResponder;
import by.evgen.android.imageloader.ImageLoader;
import by.evgen.android.apiclient.processing.CategoryArrayProcessor;
import by.evgen.android.apiclient.service.GpsLocation;
import by.evgen.android.apiclient.source.HttpDataSource;
import by.evgen.android.apiclient.source.VkDataSource;

import java.util.List;

/**
 * Created by User on 30.10.2014.
 */
public class WikiFragment extends Fragment implements DataManager.Callback<List<Category>>, GpsLocation.Callbacks {
    private RecyclerWikiAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<Category> mData;
    private View content;
    private static String mLocation;
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
        void onErrorDialog(Exception e);
    }

    @Override
    public void onShowKor(String latitude) {
        mLocation = latitude;
        Log.d(LOG_TAG, "latitude="+ mLocation);
        update(dataSource, processor);
    }

    void showDetails(int index, NoteGsonModel note) {
        mCurCheckPosition = index;
        Callbacks callbacks = getCallbacks();
        callbacks.onShowDetails(index, note);
    }

    private Callbacks getCallbacks() {
        return FindResponder.findFirstResponder(this, Callbacks.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        content = inflater.inflate(by.evgen.android.apiclient.R.layout.fragment_wiki_recycler, null);
        mSwipeRefreshLayout = (SwipeRefreshLayout) content.findViewById(by.evgen.android.apiclient.R.id.swipe_container);
        dataSource = getHttpDataSource();
        processor = getProcessor();
        mRecyclerView = (RecyclerView)content.findViewById(by.evgen.android.apiclient.R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        mRecyclerView.setItemAnimator(itemAnimator);
        GpsLocation gpsLocation = new GpsLocation();
        gpsLocation.getLocation(this, getActivity());
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
        mLocation = Api.GEOSEARCH_GET + "53.677226|23.8489383";//mLocation;
        Log.d(LOG_TAG, "mLocation="+ mLocation);
        return mLocation;
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
            RecyclerView recyclerView = (RecyclerView)content.findViewById(by.evgen.android.apiclient.R.id.recyclerView);
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
        callbacks.onErrorDialog(e);
    }
}