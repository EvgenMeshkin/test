package by.evgen.android.apiclient.fragments;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import by.evgen.android.apiclient.Api;
import by.evgen.android.apiclient.adapters.RecyclerWikiAdapter;
import by.evgen.android.apiclient.bo.Category;
import by.evgen.android.apiclient.bo.NoteGsonModel;
import by.evgen.android.apiclient.helper.ManagerDownload;
import by.evgen.android.apiclient.listener.RecyclerItemClickListener;
import by.evgen.android.apiclient.processing.Processor;
import by.evgen.android.apiclient.source.DataSource;
import by.evgen.android.apiclient.utils.FindResponder;
import by.evgen.android.apiclient.utils.Log;
import by.evgen.android.imageloader.ImageLoader;
import by.evgen.android.apiclient.processing.CategoryArrayProcessor;
import by.evgen.android.apiclient.service.GpsLocation;
import by.evgen.android.apiclient.source.HttpDataSource;
import by.evgen.android.apiclient.source.VkDataSource;

import java.util.List;

/**
 * Created by User on 30.10.2014.
 */
public class WikiFragment extends AbstractFragment implements GpsLocation.Callbacks {

    private RecyclerWikiAdapter mAdapter;
    private List<Category> mData;
    private static String mLocation;
    private CategoryArrayProcessor mCategoryArrayProcessor = new CategoryArrayProcessor();
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;

    @Override
    public void onShowKor(String latitude) {
        mLocation = latitude;
        Log.text(getClass(), "latitude=" + mLocation);
        super.load(getUrl(), getDataSource(), getProcessor());
    }

    @Override
    public View getViewLayout(LayoutInflater inflater) {
        View content = inflater.inflate(by.evgen.android.apiclient.R.layout.fragment_wiki_recycler, null);
        mRecyclerView = (RecyclerView) content.findViewById(by.evgen.android.apiclient.R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        mRecyclerView.setItemAnimator(itemAnimator);
        GpsLocation gpsLocation = new GpsLocation();
        gpsLocation.getLocation(this, getActivity());
        return content;
    }

    @Override
    public DataSource getDataSource() {
        return VkDataSource.get(getActivity());
    }

    @Override
    public Processor getProcessor() {
        return mCategoryArrayProcessor;
    }

    @Override
    public String getUrl() {
        mLocation = Api.GEOSEARCH_GET + "53.677226|23.8489383";//mLocation;
        Log.text(getClass(), "mLocation=" + mLocation);
        return mLocation;
    }

    public void showDetails(NoteGsonModel note){
        super.showDetails(note);
    }

    @Override
    public void onExecute(List data) {
        if (mAdapter == null) {
            mAdapter = new RecyclerWikiAdapter(getActivity(), data);
            mRecyclerView.setAdapter(mAdapter); //      recyclerView.setItemAnimator(itemAnimator);r(mAdapter);
            mData = data;
            mRecyclerView.addOnItemTouchListener(
                    new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                           Category item = (Category) mData.get(position);
                           NoteGsonModel note = new NoteGsonModel(item.getId(), item.getTitle(), item.getNs());
                           showDetails(note);
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
