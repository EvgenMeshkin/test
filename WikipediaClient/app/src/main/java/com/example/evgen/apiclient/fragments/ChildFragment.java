package com.example.evgen.apiclient.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.evgen.apiclient.R;

/**
 * Created by evgen on 08.11.2014.
 */
public class ChildFragment extends Fragment {


    public ChildFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View content = inflater.inflate(R.layout.fragment_child, container, false);
        return content;
    }

}