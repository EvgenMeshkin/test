package com.example.evgen.apiclient.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.evgen.apiclient.Api;
import com.example.evgen.apiclient.R;
import com.example.evgen.apiclient.adapters.DateAdapter;
import com.example.evgen.apiclient.bo.Category;
import com.example.evgen.apiclient.bo.NoteGsonModel;
import com.example.evgen.apiclient.helper.DataManager;
import com.example.evgen.imageloader.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by evgen on 06.01.2015.
 */
public class WatchListFragment extends Fragment {
    private TextView mTitle;
    private TextView mDate;
    private View content;
    private TextView empty;
    private DateAdapter mAdapter;
    private ImageLoader imageLoader;
    private Cursor mCursor;
    final Uri WIKI_URI = Uri
            .parse("content://com.example.evgenmeshkin.GeoData/geodata");
    final static String LOG_TAG = WatchListFragment.class.getSimpleName();

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
        void onErrorA(Exception e);
    }

    void showDetails(int index, NoteGsonModel note) {
        Callbacks callbacks = getCallbacks();
        callbacks.onShowDetails(index, note);
    }

    private Callbacks getCallbacks() {
        return findFirstResponderFor(this, Callbacks.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        content = inflater.inflate(R.layout.fragment_wiki, null);
        empty = (TextView) content.findViewById(android.R.id.empty);
        empty.setVisibility(View.GONE);
        mCursor = getActivity().getContentResolver().query(WIKI_URI, null, null,
                null, null);
        mCursor.moveToFirst();
        final List<String> list = new ArrayList<String>();
        if (mCursor.moveToFirst()) {
            do {
                list.add(mCursor.getString(mCursor.getColumnIndex("name")));

            } while (mCursor.moveToNext());
        }

        mCursor.moveToFirst();
        final List<Long> listData = new ArrayList<Long>();
        if (mCursor.moveToFirst()) {
            do {
                listData.add(mCursor.getLong(mCursor.getColumnIndex("wikidate")));

            } while (mCursor.moveToNext());
        }

//        if (mCursor != null && !mCursor.isClosed()) {
//            mCursor.close();
//        }

        ListView listView = (ListView) content.findViewById(android.R.id.list);
        imageLoader = ImageLoader.get(getActivity());
        String[] from = new String[] { "name", "wikidate" };
        int[] to = new int[] { R.id.text1, R.id.text2 };
        mCursor.moveToFirst();
        if (mAdapter == null) {
            mAdapter = new DateAdapter(getActivity(), R.layout.adapter_item, mCursor, from, to);

        }
            listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor)mAdapter.getItem(position);
                NoteGsonModel note = new NoteGsonModel(cursor.getLong(cursor.getColumnIndex("_id")), cursor.getString(cursor.getColumnIndex("name")), cursor.getString(cursor.getColumnIndex("wikidate")));
                showDetails(position, note);
            }
        });
            return content;
   }

}
