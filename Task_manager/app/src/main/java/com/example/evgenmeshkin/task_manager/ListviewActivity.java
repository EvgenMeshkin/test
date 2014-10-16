package com.example.evgenmeshkin.task_manager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.evgenmeshkin.task_manager.helper.DataManager;
import com.example.evgenmeshkin.task_manager.source.DataSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by User on 09.10.2014.
 */
public class ListviewActivity extends ActionBarActivity implements DataManager.Callback{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        DataManager.loadData(this);
    }

    @Override
    public void onDataLoadStart() {
        findViewById(android.R.id.progress).setVisibility(View.VISIBLE);
        findViewById(android.R.id.empty).setVisibility(View.GONE);
    }

    @Override
    public void onDone(List<String> data) {
        findViewById(android.R.id.progress).setVisibility(View.GONE);
        if (data == null || data.isEmpty()) {
            findViewById(android.R.id.empty).setVisibility(View.VISIBLE);
        }
        AdapterView listView = (AbsListView) findViewById(android.R.id.list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.adapter_item, android.R.id.text1, data) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = View.inflate(ListviewActivity.this, R.layout.adapter_item, null);
                }
                String item = getItem(position);
                TextView textView1 = (TextView) convertView.findViewById(android.R.id.text1);

                TextView textView2 = (TextView) convertView.findViewById(android.R.id.text2);

                if (position%2==0) {
                    textView2.setVisibility(View.GONE);
                    textView1.setText(item);
                }
                 else {
                    textView2.setVisibility(View.VISIBLE);
                    textView1.setText(item);
                    textView2.setText(item);
                }
                return convertView;
            }

        };
        listView.setAdapter(adapter);
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
        DataManager.AddData(this,"Add items in listView");
        DataManager.loadData(this);
    }

}







