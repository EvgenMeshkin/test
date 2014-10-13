package com.example.evgenmeshkin.task_manager;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by evgen on 13.10.2014.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class VkfActivity extends Fragment {



    public VkfActivity(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
                  View rootView = inflater.inflate(R.layout.activity_google, container, false);




        return rootView;
    }
}

