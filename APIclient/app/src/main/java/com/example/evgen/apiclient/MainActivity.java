package com.example.evgen.apiclient;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Environment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.evgen.apiclient.callbacks.SimpleCallback;
import com.example.evgen.apiclient.helper.DataManager;
import com.example.evgen.apiclient.processing.FileReaderProcessor;
import com.example.evgen.apiclient.processing.RedirectProcessor;
import com.example.evgen.apiclient.processing.StringProcessor;
import com.example.evgen.apiclient.source.ArrayStringDataSource;
import com.example.evgen.apiclient.source.FileDataSource;
import com.example.evgen.apiclient.source.HttpDataSource;

import java.io.File;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements DataManager.Callback<ArrayList<String>> {

    private ArrayAdapter<String> mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    final String LOG_TAG = "myLogs";
    final String FILENAME = "file";
    final String DIR_SD = "MyFiles";
    final String FILENAME_SD = "fileSD";
    @Override
    protected void onCreate(Bundle pSavedInstanceState) {
        super.onCreate(pSavedInstanceState);
        setContentView(R.layout.activity_main);
        final ArrayStringDataSource dataSource = new ArrayStringDataSource();
        final RedirectProcessor<ArrayList<String>> arrayRedirectProcessor = new RedirectProcessor<ArrayList<String>>();
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                DataManager.loadData(MainActivity.this, null, dataSource, arrayRedirectProcessor);
            }
        });
        DataManager.loadData(this, null, dataSource, arrayRedirectProcessor);

        HttpDataSource httpDataSource = HttpDataSource.get(this);
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
                stringProcessor);
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
    public void onDone(ArrayList<String> data) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        findViewById(android.R.id.progress).setVisibility(View.GONE);
        if (data == null || data.isEmpty()) {
            findViewById(android.R.id.empty).setVisibility(View.VISIBLE);
        }
        AdapterView listView = (AbsListView) findViewById(android.R.id.list);
        TextView textView1 = (TextView) findViewById(R.id.rez);
        textView1.setText(data.toString());

        if (mAdapter == null) {
            mAdapter = new ArrayAdapter<String>(this, R.layout.adapter_item, android.R.id.text1, data) {

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {

                    if (convertView == null) {
                        convertView = View.inflate(MainActivity.this, R.layout.adapter_item, null);
                    }
                    String item = getItem(position);
                    TextView textView1 = (TextView) convertView.findViewById(android.R.id.text1);
                    textView1.setText(item);
                    TextView textView2 = (TextView) convertView.findViewById(android.R.id.text2);
                    textView2.setText(item.substring(5));
                    return convertView;
                }

            };
            listView.setAdapter(mAdapter);
        } else {
            //only for honeycomb
            mAdapter.addAll(data);
        }
    }

    @Override
    public void onError(Exception e) {
        findViewById(android.R.id.progress).setVisibility(View.GONE);
        findViewById(android.R.id.empty).setVisibility(View.GONE);
        TextView errorView = (TextView) findViewById(R.id.error);
        errorView.setVisibility(View.VISIBLE);
        errorView.setText(errorView.getText() + "\n" + e.getMessage());
    }

    public void onAddClick(View view) throws Exception {

        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.d(LOG_TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
            return;
        }
        // получаем путь к SD
        File sdPath = Environment.getExternalStorageDirectory();
        FileDataSource filedatasource = new FileDataSource();
        FileReaderProcessor fileprocessor = new FileReaderProcessor();
        SimpleCallback<String> callback = new SimpleCallback<String>() {

            @Override
            public void onDone(Object data) {
                setContentView(R.layout.activity_main);
                TextView textView1 = (TextView) findViewById(R.id.rez);
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
        FileReaderProcessor fileprocessor = new FileReaderProcessor();
        SimpleCallback<String> callback = new SimpleCallback<String>() {

            @Override
            public void onDone(Object data) {
                setContentView(R.layout.activity_main);
                TextView textView1 = (TextView) findViewById(R.id.rez);
                textView1.setText(data.toString());
                Log.d("MainActivity", "onDone " + data);

            }

        };
        DataManager.loadData(callback,sdPath.getAbsolutePath() + "/" + DIR_SD, filedatasource, fileprocessor);
       // View.inflate(MainActivity.this, R.layout.activity_main, null);



    }

}
