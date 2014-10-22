package com.example.evgen.apiclient;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import com.example.evgen.apiclient.bo.NoteGsonModel;

/**
 * Created by User on 22.10.2014.
 */
public class DetailsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        NoteGsonModel noteGsonModel = (NoteGsonModel) getIntent().getParcelableExtra("item");
        ((TextView)findViewById(android.R.id.text1)).setText(noteGsonModel.getTitle());
        ((TextView)findViewById(android.R.id.text2)).setText(noteGsonModel.getContent());
    }

}