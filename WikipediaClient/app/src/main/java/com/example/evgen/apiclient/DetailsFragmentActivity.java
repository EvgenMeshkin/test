package com.example.evgen.apiclient;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.example.evgen.apiclient.bo.NoteGsonModel;
import com.example.evgen.apiclient.fragments.DetailsFragment;

/**
 * Created by User on 13.11.2014.
 */
public class DetailsFragmentActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }
       ActionBar actionBar = getSupportActionBar();
    }


}
