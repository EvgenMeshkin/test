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

import java.util.List;

/**
 * Created by evgen on 06.01.2015.
 */
public class WatchListFragment extends Fragment implements DataManager.Callback<List<Category>> {
    private TextView mTitle;
    private TextView mContent;
    private View content;
    private TextView empty;
    private View footerProgress;
    private Context mContext = getActivity();
    private ArrayAdapter mAdapter;
    private ImageLoader imageLoader;
    Cursor mCursor;


    final Uri WIKI_URI = Uri
            .parse("content://com.example.evgenmeshkin.GeoData/geodata");

    final String WIKI_NAME = "name";
    final String WIKI_KOR = "koordinaty";
    int mCurCheckPosition = 0;
    final static String LOG_TAG = WatchListFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        content = inflater.inflate(R.layout.fragment_wiki, null);
        empty = (TextView) content.findViewById(android.R.id.empty);
        empty.setVisibility(View.GONE);
        //Uri newUri = mContext.getContentResolver().insert(WIKI_URI, cv);
        Log.d(LOG_TAG, "insert, count : " + WIKI_URI.toString());
        mCursor = getActivity().getContentResolver().query(WIKI_URI, null, null,
                null, null);
        mCursor.moveToFirst();
        ListView listView = (ListView) content.findViewById(android.R.id.list);
        imageLoader = ImageLoader.get(getActivity());

        if (mAdapter == null) {
            mAdapter = new ArrayAdapter<Category>(mContext, R.layout.adapter_item, android.R.id.text1, (List<Category>) mCursor) {
                @Override
                public View getView(final int position, View convertView, ViewGroup parent) {
                    if (convertView == null) {
                        convertView = View.inflate(mContext, R.layout.adapter_item, null);
                    }


                    mTitle = (TextView) convertView.findViewById(android.R.id.text1);
                    mTitle.setText(mCursor.getString(mCursor.getColumnIndex("name")));
                    mContent = (TextView) convertView.findViewById(android.R.id.text2);
                    mContent.setText(mCursor.getString(mCursor.getColumnIndex("koordinaty")));
                    mCursor.close();
                    final String urlImage = Api.IMAGEVIEW_GET + mCursor.getString(mCursor.getColumnIndex("name")).replaceAll(" ", "%20");
                    convertView.setTag(mCursor.getColumnIndex("name"));
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



    @Override
    public void onDataLoadStart() {

    }

    @Override
    public void onDone(List<Category> data) {

    }

    @Override
    public void onError(Exception e) {

    }
}
