package com.example.evgen.apiclient.fragments;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.app.Activity;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
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
import android.widget.ListView;
import android.widget.TextView;

import com.example.evgen.apiclient.Api;
import com.example.evgen.apiclient.R;
import com.example.evgen.apiclient.WikiActivity;
import com.example.evgen.apiclient.auth.VkOAuthHelper;
import com.example.evgen.apiclient.auth.secure.EncrManager;
import com.example.evgen.apiclient.bo.Category;
import com.example.evgen.apiclient.bo.Friend;
import com.example.evgen.apiclient.bo.Note;
import com.example.evgen.apiclient.bo.NoteGsonModel;
import com.example.evgen.apiclient.dialogs.ErrorDialog;
import com.example.evgen.apiclient.helper.DataManager;
import com.example.evgen.apiclient.os.AsyncTask;
import com.example.evgen.apiclient.processing.CategoryArrayProcessor;
import com.example.evgen.apiclient.processing.NoteArrayProcessor;
import com.example.evgen.apiclient.processing.ViewArrayProcessor;
import com.example.evgen.apiclient.source.HttpDataSource;
import com.example.evgen.apiclient.source.VkDataSource;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.List;

/**
 * Created by User on 30.10.2014.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class WikiFragment extends ListFragment implements DataManager.Callback<List<Category>> {
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
    final static String LOG_TAG = "WikiFragment";
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
        final HttpDataSource dataSource = getHttpDataSource();
        final CategoryArrayProcessor processor = getProcessor();
        empty = (TextView) content.findViewById(android.R.id.empty);
        empty.setVisibility(View.GONE);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                update(dataSource, processor);
            }
        });
        update(dataSource, processor);
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
        return Api.GEOSEARCH_GET;
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
            // Make sure our UI is in the correct state.
          //  showDetails(mCurCheckPosition, note);
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
                    public View getView(int position, View convertView, ViewGroup parent) {
                        if (convertView == null) {
                            convertView = View.inflate(getActivity(), R.layout.adapter_item, null);
                        }
                        Category item = getItem(position);
                        mTitle = (TextView) convertView.findViewById(android.R.id.text1);
                        mTitle.setText(item.getTITLE());
                        mContent = (TextView) convertView.findViewById(android.R.id.text2);
                        mContent.setText(item.getNS());
                        convertView.setTag(item.getId());
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
                                mPost = new HttpPost(ACCOUNT_PAS + ACCOUNT_METHOD + "?title=" + ACCOUNT_TITLE + "&text=" + item.getTITLE().replaceAll(" ", "%20") + "&privacy=3&comment_privacy=3&v=5.26&access_token=" + VkOAuthHelper.mAccessToken);//EncrManager.decrypt(getActivity(), mAm.getUserData(sAccount, "Token")));
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