package com.example.evgen.apiclient;
import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.evgen.apiclient.bo.Note;
import com.example.evgen.apiclient.bo.NoteGsonModel;
import com.example.evgen.apiclient.callbacks.SimpleCallback;
import com.example.evgen.apiclient.helper.DataManager;
import com.example.evgen.apiclient.processing.FileProcessor;
import com.example.evgen.apiclient.processing.NoteArrayProcessor;
import com.example.evgen.apiclient.processing.Processor;
import com.example.evgen.apiclient.processing.RedirectProcessor;
import com.example.evgen.apiclient.processing.StringProcessor;
import com.example.evgen.apiclient.source.ArrayStringDataSource;
import com.example.evgen.apiclient.source.FileDataSource;
import com.example.evgen.apiclient.source.HttpDataSource;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity implements DataManager.Callback<List<Note>> {

    private ArrayAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    public static final String URL = "https://dl.dropboxusercontent.com/u/16403954/test.json";
    private List<Note> mData;

    final String LOG_TAG = "myLogs";
    final String FILENAME = "file";
    final String DIR_SD = "MyFiles";
    final String FILENAME_SD = "fileSD";

    public static final int LOADER_ID = 0;
    private Cursor mCursor;
    @Override
    protected void onCreate(Bundle pSavedInstanceState) {
        super.onCreate(pSavedInstanceState);
        setContentView(R.layout.activity_main);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        final HttpDataSource dataSource = HttpDataSource.get(MainActivity.this);
        final NoteArrayProcessor processor = new NoteArrayProcessor();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                DataManager.loadData(MainActivity.this, URL, dataSource, processor);
            }
        });
        DataManager.loadData(MainActivity.this, URL, dataSource, processor);

     /*   HttpDataSource httpDataSource = HttpDataSource.get(this);
        SimpleCallback<String> callback = new SimpleCallback<String>() {

            @Override
            public void onDone(Object data) {
                Log.d("MainActivity", "onDone " + data);
            }

        };
        StringProcessor stringProcessor = new StringProcessor();
        DataManager.loadData(
                callback,
                "https://dl.dropboxusercontent.com/u/16403954/test.json",
                httpDataSource,
                stringProcessor
        );

        DataManager.loadData(
                callback,
                "http://google.com",
                httpDataSource,
                stringProcessor);*/
        LoaderManager supportLoaderManager = getSupportLoaderManager();
        supportLoaderManager.restartLoader(LOADER_ID,
                new Bundle(),
                new LoaderManager.LoaderCallbacks<Cursor>() {

                    @Override
                    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
                        return new CursorLoader(MainActivity.this){

                            @Override
                            public Cursor loadInBackground() {
                                MatrixCursor cursor = new MatrixCursor(new String[]{"id","name"});
                                cursor.addRow(new Object[]{1l, "Vasya"});
                                return cursor;
                            }
                        };
                    }

                    @Override
                    public void onLoadFinished(Loader<Cursor> objectLoader, Cursor cursor) {
                        mCursor = cursor;
                    }

                    @Override
                    public void onLoaderReset(Loader<Cursor> objectLoader) {
                        mCursor = null;
                    }

                });
    }


    @Override
    public void onDataLoadStart() {
        if (!mSwipeRefreshLayout.isRefreshing()) {
            findViewById(android.R.id.progress).setVisibility(View.VISIBLE);
        }
        findViewById(android.R.id.empty).setVisibility(View.GONE);
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onDone(List<Note> data) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        findViewById(android.R.id.progress).setVisibility(View.GONE);
        if (data == null || data.isEmpty()) {
            findViewById(android.R.id.empty).setVisibility(View.VISIBLE);
        }
        AdapterView listView = (AbsListView) findViewById(android.R.id.list);
        if (mAdapter == null) {
            mData = data;
            mAdapter = new ArrayAdapter<Note>(this, R.layout.adapter_item, android.R.id.text1, data) {

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    if (convertView == null) {
                        convertView = View.inflate(MainActivity.this, R.layout.adapter_item, null);
                    }
                    Note item = getItem(position);
                    TextView textView1 = (TextView) convertView.findViewById(android.R.id.text1);
                    textView1.setText(item.getTitle());
                    TextView textView2 = (TextView) convertView.findViewById(android.R.id.text2);
                    textView2.setText(item.getContent());
                    convertView.setTag(item.getId());
                    return convertView;
                }

            };
            listView.setAdapter(mAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                    Note item = (Note) mAdapter.getItem(position);
                    NoteGsonModel note = new NoteGsonModel(item.getId(), item.getTitle(), item.getContent());
                    intent.putExtra("item", note);
                    startActivity(intent);
                }
            });
        } else {
            mData.clear();
            mData.addAll(data);
            mAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onError(Exception e) {
        e.printStackTrace();

        findViewById(android.R.id.progress).setVisibility(View.GONE);
        findViewById(android.R.id.empty).setVisibility(View.GONE);
        TextView errorView = (TextView) findViewById(R.id.error);
        errorView.setVisibility(View.VISIBLE);
        errorView.setText(errorView.getText() + "\n" + e.getMessage());
    }

  /*  public void onAddClick(View view) throws Exception {

        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.d(LOG_TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
            return;
        }
        // получаем путь к SD
        File sdPath = Environment.getExternalStorageDirectory();
        FileDataSource filedatasource = new FileDataSource();
        FileProcessor fileprocessor = new FileProcessor();
        SimpleCallback<String> callback = new SimpleCallback<String>() {

            @Override
            public void onDone(Object data) {
                setContentView(R.layout.activity_main);
                TextView textView1 = (TextView) findViewById(R.id.rez);
                textView1.setVisibility(View.VISIBLE);
                textView1.setText("Данные записаны в файл");
                Log.d("MainActivity", "onDone " + data);

            }

        };
        DataManager.setData(callback,sdPath.getAbsolutePath() + "/" + DIR_SD, filedatasource, fileprocessor);

    }

    public void onGetClick(View view) throws Exception {

        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.d(LOG_TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
            return;
        }
        // получаем путь к SD
        File sdPath = Environment.getExternalStorageDirectory();
        FileDataSource filedatasource = new FileDataSource();
        FileProcessor fileprocessor = new FileProcessor();
        SimpleCallback<String> callback = new SimpleCallback<String>() {

            @Override
            public void onDone(Object data) {
                setContentView(R.layout.activity_main);
                TextView textView1 = (TextView) findViewById(R.id.rez);
                textView1.setVisibility(View.VISIBLE);
                textView1.setText(data.toString());
                Log.d("MainActivity", "onDone " + data);

            }

        };
        DataManager.loadData(callback,sdPath.getAbsolutePath() + "/" + DIR_SD, filedatasource, fileprocessor);
        }

    public void onGetingClick(View view) throws Exception {
        InputStream input = getAssets().open("Proba.txt");
        int size = input.available();
        byte[] buffer = new byte[size];
        input.read(buffer);
        input.close();
        String text = new String(buffer);
        setContentView(R.layout.activity_main);
        TextView textView1 = (TextView) findViewById(R.id.rez);
        textView1.setText(new String(buffer));
    }*/


}
