package com.example.evgen.apiclient.fragments;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.evgen.apiclient.R;
import com.example.evgen.apiclient.auth.VkOAuthHelper;
import com.example.evgen.apiclient.auth.secure.EncrManager;
import com.example.evgen.apiclient.bo.Note;
import com.example.evgen.apiclient.bo.NoteGsonModel;
import com.example.evgen.apiclient.helper.DataManager;
import com.example.evgen.apiclient.os.AsyncTask;
import com.example.evgen.apiclient.processing.NoteArrayProcessor;
import com.example.evgen.apiclient.source.HttpDataSource;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.List;

/**
 * Created by User on 30.10.2014.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class WikiFragment extends Fragment implements DataManager.Callback<List<Note>> {
    private String[] viewsNames;
    private ArrayAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    public static final String URL = "https://en.wikipedia.org/w/api.php?action=query&prop=categories&format=json&titles=Albert%20Einstein";
    private List<Note> mData;
    private static final String TAG = VkOAuthHelper.class.getSimpleName();
    private HttpClient mClient;
    private HttpPost mPost;
    private TextView mTitle;
    private TextView mContent;
    public static final String ACCOUNT_PAS = "https://api.vk.com/method/";
    public static final String ACCOUNT_METHOD = "notes.add";
    public static final String ACCOUNT_TITLE = "Wikipedia";
    Fragment frag1;
    public static final String AUTHORITY = "com.example.evgen.apiclient";
    private AccountManager mAm;
    public static Account sAccount;
    View content;
    TextView empty;

    public interface onSomeEventListener {
        public void someEvent(NoteGsonModel note);
    }
    onSomeEventListener someEventListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            someEventListener = (onSomeEventListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onSomeEventListener");
        }
    }

    public WikiFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final HttpDataSource dataSource = new HttpDataSource();//get(g)tActivity().this);
        final NoteArrayProcessor processor = new NoteArrayProcessor();
        DataManager.loadData(this, URL, dataSource, processor);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        content = inflater.inflate(R.layout.fragment_wiki, null);
        empty = (TextView) content.findViewById(android.R.id.empty);
        empty.setVisibility(View.GONE);

        return content;
    }

    @Override
    public void onDataLoadStart() {
        //       empty.setVisibility(View.GONE);
    }

    @Override
    public void onDone(List<Note> data) {
        content.findViewById(android.R.id.progress).setVisibility(View.GONE);
        if (data == null || data.isEmpty()) {
            content.findViewById(android.R.id.empty).setVisibility(View.VISIBLE);
        }
        AdapterView listView = (AbsListView) content.findViewById(android.R.id.list);
        if (mAdapter == null) {
            mData = data;
            mAdapter = new ArrayAdapter<Note>(getActivity(), R.layout.adapter_item, android.R.id.text1, data) {

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    if (convertView == null) {
                        convertView = View.inflate(getActivity(), R.layout.adapter_item, null);
                    }
                    Note item = getItem(position);
                    mTitle = (TextView) convertView.findViewById(android.R.id.text1);
                    mTitle.setText(item.getTitle());
                    mContent = (TextView) convertView.findViewById(android.R.id.text2);
                    mContent.setText(item.getContent());
                    convertView.setTag(item.getId());
                    return convertView;
                }

            };
            listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Intent intent = new Intent(WikiActivity.this, DetailsActivity.class);
                final Note item = (Note) mAdapter.getItem(position);
                NoteGsonModel note = new NoteGsonModel(item.getId(), item.getTitle(), item.getContent());
                new AsyncTask() {

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        mClient = new DefaultHttpClient();
                        try {
                            mPost = new HttpPost(ACCOUNT_PAS + ACCOUNT_METHOD + "?title=" + ACCOUNT_TITLE + "&text=" + item.getTitle().replaceAll(" ", "%20") + "&privacy=3&comment_privacy=3&v=5.26&access_token=" + VkOAuthHelper.mAccessToken);//EncrManager.decrypt(getActivity(), mAm.getUserData(sAccount, "Token")));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected Object doInBackground(Object[] params) throws Exception {
                        HttpResponse response = mClient.execute(mPost);
                        return null;
                    }

                    @Override
                    protected void onPostException(Exception e) {
                     }
                }.execute();
                someEventListener.someEvent(note);
            }
        });
    }

    else

    {
        mData.clear();
        mData.addAll(data);
        mAdapter.notifyDataSetChanged();
    }

}
    @Override
    public void onError(Exception e) {
            e.printStackTrace();
            content.findViewById(android.R.id.progress).setVisibility(View.GONE);
            content.findViewById(android.R.id.empty).setVisibility(View.GONE);
            TextView errorView = (TextView) content.findViewById(R.id.error);
            errorView.setVisibility(View.VISIBLE);
            errorView.setText(errorView.getText() + "\n" + e.getMessage());
    }
}