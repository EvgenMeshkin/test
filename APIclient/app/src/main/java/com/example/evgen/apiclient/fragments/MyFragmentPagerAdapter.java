package com.example.evgen.apiclient.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by evgen on 11.11.2014.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    static final int PAGE_COUNT = 150;
    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return ScreenSlidePageFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Title " + position;
    }
}


