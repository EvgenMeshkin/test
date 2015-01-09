package com.example.evgen.apiclient.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.evgen.apiclient.Api;
import com.example.evgen.apiclient.R;
import com.example.evgen.apiclient.bo.Category;
import com.example.evgen.apiclient.helper.DataManager;
import com.example.evgen.imageloader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by evgen on 06.01.2015.
 */
public class WatchListFragment extends Fragment {
    private TextView mTitle;
    private View content;
    private TextView empty;
    private ArrayAdapter mAdapter;
    private ImageLoader imageLoader;
    Cursor mCursor;
    final Uri WIKI_URI = Uri
            .parse("content://com.example.evgenmeshkin.GeoData/geodata");
    final static String LOG_TAG = WatchListFragment.class.getSimpleName();

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
        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
        Log.d(LOG_TAG, "data watchlist : " + list.toString());
        ListView listView = (ListView) content.findViewById(android.R.id.list);
        imageLoader = ImageLoader.get(getActivity());
        if (mAdapter == null) {
            mAdapter = new ArrayAdapter<String>(getActivity(), R.layout.adapter_item, android.R.id.text1, list) {
                @Override
                public View getView(final int position, View convertView, ViewGroup parent) {
                    if (convertView == null) {
                        convertView = View.inflate(getActivity(), R.layout.adapter_item, null);
                    }
                    mTitle = (TextView) convertView.findViewById(android.R.id.text1);
                    mTitle.setText(list.get(position));
                    final String urlImage = Api.IMAGEVIEW_GET + list.get(position).replaceAll(" ", "%20");
                    convertView.setTag(position);
                    final ImageView imageView = (ImageView) convertView.findViewById(android.R.id.icon);
                    imageView.setImageBitmap(null);
                    imageView.setTag(urlImage);
                    imageLoader.displayImage(urlImage, imageView);
                    return convertView;
                }
            };
        }
            listView.setAdapter(mAdapter);
            return content;
   }

}
