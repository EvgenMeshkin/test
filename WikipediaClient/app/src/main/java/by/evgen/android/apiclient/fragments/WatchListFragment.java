package by.evgen.android.apiclient.fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import by.evgen.android.apiclient.R;
import by.evgen.android.apiclient.adapters.DateAdapter;
import by.evgen.android.apiclient.bo.NoteGsonModel;
import by.evgen.android.apiclient.utils.FindResponder;
import by.evgen.android.imageloader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

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
    //TODO what is example
    final Uri WIKI_URI = Uri
            .parse("content://com.example.evgenmeshkin.GeoData/geodata");
    final static String LOG_TAG = WatchListFragment.class.getSimpleName();

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
                showDetails(note);
            }
        });
            return content;
   }

}
