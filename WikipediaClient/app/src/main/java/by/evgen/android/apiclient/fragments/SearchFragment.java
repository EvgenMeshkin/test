package by.evgen.android.apiclient.fragments;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import by.evgen.android.apiclient.Api;
import by.evgen.android.apiclient.R;
import by.evgen.android.apiclient.adapters.SearchArrayAdapter;
import by.evgen.android.apiclient.bo.Category;
import by.evgen.android.apiclient.bo.NoteGsonModel;
import by.evgen.android.apiclient.helper.ManagerDownload;
import by.evgen.android.apiclient.processing.Processor;
import by.evgen.android.apiclient.processing.SearchPagesProcessor;
import by.evgen.android.apiclient.source.DataSource;
import by.evgen.android.apiclient.source.HttpDataSource;
import by.evgen.android.apiclient.source.VkDataSource;
import by.evgen.android.apiclient.utils.FindResponder;
import by.evgen.android.imageloader.ImageLoader;

import java.util.List;

/**
 * Created by User on 18.12.2014.
 */

//TODO create Base fragment with common logic of download data
public class SearchFragment extends AbstractFragment {
    private ArrayAdapter mAdapter;
    private List<Category> mData;
    private TextView mTitle;
    private TextView mContent;
    private ImageLoader imageLoader;
    private Context mContext = getActivity();
    private static String mKor;

    private String mValue;
    private ListView mListView;
    public static final int COUNT = 50;
    private boolean isImageLoaderControlledByDataManager = false;
    private boolean isPagingEnabled = true;
    private View footerProgress;

    final static String LOG_TAG = SearchFragment.class.getSimpleName();
    private SearchPagesProcessor mSearchPagesProcessor = new SearchPagesProcessor();

    @Override
    public View getViewLayout(LayoutInflater inflater) {
        View content = inflater.inflate(R.layout.fragment_wiki, null);
        mContext = getActivity();
        mListView = (ListView) content.findViewById(android.R.id.list);

        //TODO or we use m prefix or not
        //TODO do not use several getters in one method
        mValue = "";
        imageLoader = ImageLoader.get(getActivity());
        if(getArguments() != null) {
            mValue = getArguments().getString("key");;
       }
        return content;
    }

    @Override
    public DataSource getDataSource() {
        return new VkDataSource();
    }

    @Override
    public Processor getProcessor() {
        return mSearchPagesProcessor;
    }

    @Override
    public String getUrl() {
        return getUrl(COUNT, 0);
    }

    @Override
    public void onExecute(List data) {
        footerProgress = View.inflate(mContext, R.layout.view_footer_progress, null);
        refreshFooter();
        if (mAdapter == null) {
            mData = data;
            mAdapter = new SearchArrayAdapter(mContext, R.layout.adapter_item, data);
            mListView.setFooterDividersEnabled(true);
            mListView.addFooterView(footerProgress, null, false);
            mListView.setAdapter(mAdapter);
            mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                private int previousTotal = 0;
                private int visibleThreshold = 5;

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    switch (scrollState) {
                        case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                            if (!isImageLoaderControlledByDataManager) {
                                imageLoader.resume();
                            }
                            break;
                        case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                            if (!isImageLoaderControlledByDataManager) {
                                imageLoader.pause();
                            }
                            break;
                        case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                            if (!isImageLoaderControlledByDataManager) {
                                imageLoader.pause();
                            }
                            break;
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    ListAdapter adapter = view.getAdapter();
                    final int count = getRealAdapterCount(adapter);
                    if (count == 0) {
                        return;
                    }
                    if (previousTotal != totalItemCount && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                        previousTotal = totalItemCount;
                        isImageLoaderControlledByDataManager = true;

                        ManagerDownload.load(new ManagerDownload.Callback<List<Category>>() {
                                                 @Override
                                                 public void onPreExecute() {
                                                     imageLoader.pause();
                                                 }

                                                 @Override
                                                 public void onPostExecute(List<Category> data) {
                                                     updateAdapter(data);
                                                     refreshFooter();
                                                     imageLoader.resume();
                                                     isImageLoaderControlledByDataManager = false;
                                                 }

                                                 @Override
                                                 public void onError(Exception e) {
                                                     onError(e);
                                                     imageLoader.resume();
                                                     isImageLoaderControlledByDataManager = false;
                                                 }
                                             },
                                getUrl(COUNT, count),
                                getDataSource(),
                                getProcessor());
                    }
                }

            });

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final Category item = (Category) mAdapter.getItem(position);
                    NoteGsonModel note = new NoteGsonModel(item.getId(), item.getTitle(), item.getNs());
                    showDetails(note);
                }
            });

            if (data != null && data.size() == COUNT) {
                isPagingEnabled = true;
            } else {
                isPagingEnabled = false;
            }
        } else {
            mData.clear();
            updateAdapter(data);
        }
        refreshFooter();
    }

    private String getUrl(int count, int offset) {
        mKor = Api.SEARCH_GET + "srlimit="+count+"&sroffset="+offset + "&srsearch=" + mValue;
        Log.d(LOG_TAG, "mKor="+mKor);
        return mKor;
    }

    private void updateAdapter(List<Category> data) {
        if (data != null && data.size() == COUNT) {
            isPagingEnabled = true;
            mListView.addFooterView(footerProgress, null, false);
        } else {
            isPagingEnabled = false;
            mListView.removeFooterView(footerProgress);
        }
        if (data != null) {
            mData.addAll(data);
        }
        mAdapter.notifyDataSetChanged();
    }


    public static int getRealAdapterCount(ListAdapter adapter) {
        if (adapter == null) {
            return 0;
        }
        int count = adapter.getCount();
        if (adapter instanceof HeaderViewListAdapter) {
            HeaderViewListAdapter headerViewListAdapter = (HeaderViewListAdapter) adapter;
            count = count - headerViewListAdapter.getFootersCount() - headerViewListAdapter.getHeadersCount();
        }
        return count;
    }

    private void refreshFooter() {
        if (footerProgress != null) {
            if (isPagingEnabled) {
                footerProgress.setVisibility(View.VISIBLE);
            } else {
                footerProgress.setVisibility(View.GONE);
            }
        }
    }

}
