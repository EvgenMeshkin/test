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
import by.evgen.android.apiclient.bo.Category;
import by.evgen.android.apiclient.bo.NoteGsonModel;
import by.evgen.android.apiclient.helper.ManagerDownload;
import by.evgen.android.apiclient.processing.SearchPagesProcessor;
import by.evgen.android.apiclient.source.HttpDataSource;
import by.evgen.android.apiclient.source.VkDataSource;
import by.evgen.android.apiclient.utils.FindResponder;
import by.evgen.android.imageloader.ImageLoader;

import java.util.List;

/**
 * Created by User on 18.12.2014.
 */

//TODO create Base fragment with common logic of download data
public class SearchFragment extends Fragment implements ManagerDownload.Callback<List<Category>> {
    private ArrayAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<Category> mData;
    private TextView mTitle;
    private TextView mContent;
    private View content;
    private TextView empty;
    private static String mKor;
    private HttpDataSource dataSource;
    private SearchPagesProcessor processor;
    private ImageLoader imageLoader;
    Cursor mCursor;
    private String mValue;
    //TODO generate ID
    public static final int LOADER_ID = 0;
    public static final int COUNT = 50;
    private boolean isImageLoaderControlledByDataManager = false;
    private boolean isPagingEnabled = true;
    private View footerProgress;
    private Context mContext = getActivity();


    final Uri WIKI_URI = Uri
            .parse("content://com.example.evgenmeshkin.GeoData/geodata");

    final String WIKI_NAME = "name";
    final String WIKI_KOR = "koordinaty";
    int mCurCheckPosition = 0;
    final static String LOG_TAG = SearchFragment.class.getSimpleName();
    private SearchPagesProcessor mSearchPagesProcessor = new SearchPagesProcessor();

    public interface Callbacks {
        void onShowDetails(NoteGsonModel note);
        void onErrorDialog(Exception e);
    }

    void showDetails(NoteGsonModel note) {

        Callbacks callbacks = getCallbacks();
        callbacks.onShowDetails(note);
    }

    private Callbacks getCallbacks() {
        return FindResponder.findFirstResponder(this, Callbacks.class);
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
        mContext = getActivity();
        //TODO or we use m prefix or not
        //TODO do not use several getters in one method
        mValue = "";
        empty = (TextView) content.findViewById(android.R.id.empty);
        empty.setVisibility(View.GONE);
        imageLoader = ImageLoader.get(getActivity());
        if(getArguments() != null) {
            mValue = getArguments().getString("key");;
            update(dataSource, processor);
        }
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                update(dataSource, processor);
            }
        });
        return content;
    }

    private SearchPagesProcessor getProcessor() {
        return mSearchPagesProcessor;
    }

    private HttpDataSource getHttpDataSource() {
        return new VkDataSource();
    }

    private void update(HttpDataSource dataSource, SearchPagesProcessor processor) {
        ManagerDownload.load(this,
                getUrl(COUNT, 0),
                dataSource,
                processor);
    }

    private String getUrl(int count, int offset) {
        mKor = Api.SEARCH_GET + "srlimit="+count+"&sroffset="+offset + "&srsearch=" + mValue;
        Log.d(LOG_TAG, "mKor="+mKor);
        return mKor;
    }

   @Override
    public void onPreExecute() {
        if (!mSwipeRefreshLayout.isRefreshing()) {
            content.findViewById(android.R.id.progress).setVisibility(View.VISIBLE);
        }
        empty.setVisibility(View.GONE);
    }

    @Override
    public void onPostExecute(List<Category> data) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        content.findViewById(android.R.id.progress).setVisibility(View.GONE);
        if (data == null || data.isEmpty()) {
            content.findViewById(android.R.id.empty).setVisibility(View.VISIBLE);
            onError(new NullPointerException("No data"));
        } else {
            ListView listView = (ListView) content.findViewById(android.R.id.list);
            footerProgress = View.inflate(mContext, R.layout.view_footer_progress, null);
            refreshFooter();
            if (mAdapter == null) {
                mData = data;
                mAdapter = new ArrayAdapter<Category>(mContext, R.layout.adapter_item, android.R.id.text1, data) {
                    @Override
                    public View getView(final int position, View convertView, ViewGroup parent) {
                        if (convertView == null) {
                            convertView = View.inflate(mContext, R.layout.adapter_item, null);
                        }
                        Category item = getItem(position);
                        mTitle = (TextView) convertView.findViewById(android.R.id.text1);
                        mTitle.setText(item.getTitle());
                        mContent = (TextView) convertView.findViewById(android.R.id.text2);
                        mContent.setText(item.getDist());
                        final String urlImage = Api.IMAGEVIEW_GET + item.getTitle().replaceAll(" ", "%20");
                        convertView.setTag(item.getId());
                        final ImageView imageView = (ImageView) convertView.findViewById(android.R.id.icon);
                        imageView.setImageBitmap(null);
                        imageView.setTag(urlImage);
                        imageLoader.displayImage(urlImage, imageView);
                        return convertView;
                    }
                };
                listView.setFooterDividersEnabled(true);
                listView.addFooterView(footerProgress, null, false);
                listView.setAdapter(mAdapter);
                listView.setOnScrollListener(new AbsListView.OnScrollListener() {
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
                                    getHttpDataSource(),
                                    getProcessor());
                        }
                    }

                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
    }

    private void updateAdapter(List<Category> data) {
         ListView listView = (ListView) content.findViewById(android.R.id.list);
        if (data != null && data.size() == COUNT) {
            isPagingEnabled = true;
            listView.addFooterView(footerProgress, null, false);
        } else {
            isPagingEnabled = false;
            listView.removeFooterView(footerProgress);
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

    @Override
    public void onError(Exception e) {
        Log.d(LOG_TAG, "onError");
        e.printStackTrace();
        content.findViewById(android.R.id.progress).setVisibility(View.GONE);
        content.findViewById(android.R.id.empty).setVisibility(View.GONE);
        TextView errorView = (TextView) content.findViewById(R.id.error);
        errorView.setVisibility(View.VISIBLE);
        errorView.setText(errorView.getText() + "\n" + e.getMessage());
        Callbacks callbacks = getCallbacks();
        callbacks.onErrorDialog(e);
    }
}
