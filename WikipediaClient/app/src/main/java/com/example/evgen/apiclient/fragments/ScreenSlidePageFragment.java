package com.example.evgen.apiclient.fragments;

import android.graphics.Color;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.evgen.apiclient.R;

import java.util.Random;

/**
 * Created by evgen on 10.11.2014.
 */
public class ScreenSlidePageFragment extends Fragment {
    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";

    int pageNumber;
    int backColor;

    static ScreenSlidePageFragment newInstance(int page) {
        ScreenSlidePageFragment pageFragment = new ScreenSlidePageFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        pageFragment.setArguments(arguments);
        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);
//        Random rnd = new Random();
//        backColor = Color.argb(40, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_viewpager, null);
        TextView tvPage = (TextView) view.findViewById(R.id.tvPage);
        tvPage.setText("Fragment " + pageNumber);
//        tvPage.setBackgroundColor(backColor);

        return view;
    }
}