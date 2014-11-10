package com.example.evgen.apiclient;

/**
 * Created by User on 30.10.2014.
 */

import android.accounts.Account;
import android.accounts.AccountManager;




import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.content.ContentResolver;
import android.os.Build;
import android.os.Bundle;

import android.app.Activity;

import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.app.ActionBar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.evgen.apiclient.auth.VkOAuthHelper;
import com.example.evgen.apiclient.auth.secure.EncrManager;
import com.example.evgen.apiclient.bo.Note;
import com.example.evgen.apiclient.bo.NoteGsonModel;
import com.example.evgen.apiclient.fragments.ChildFragment;
import com.example.evgen.apiclient.fragments.DetailsFragment;
import com.example.evgen.apiclient.fragments.MyFragmentPagerAdapter;
import com.example.evgen.apiclient.fragments.ScreenSlideActivity;
import com.example.evgen.apiclient.fragments.WikiFragment;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class WikiActivity extends FragmentActivity implements WikiFragment.onSomeEventListener {
    private DrawerLayout myDrawerLayout;
    private ListView myDrawerList;
    private ActionBarDrawerToggle myDrawerToggle;
    // navigation drawer title
    private CharSequence myDrawerTitle;
    // used to store app title
    private CharSequence myTitle;
    private String[] viewsNames;
    private ArrayAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    public static final String URL = "https://en.wikipedia.org/w/api.php?action=query&prop=categories&format=json&titles=Albert%20Einstein";
    private static final String TAG = VkOAuthHelper.class.getSimpleName();
    public static final String ACCOUNT_TYPE = "com.example.evgen.apiclient.account";
    private Fragment mFrag1;
    private Fragment mFrag2;
    private Fragment mFrag3;
    public static final String AUTHORITY = "com.example.evgen.apiclient";
    private AccountManager mAm;
    public static Account sAccount;
    ViewPager pager;
    PagerAdapter pagerAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getResources().getConfiguration().orientation = Configuration.ORIENTATION_PORTRAIT;
        setContentView(R.layout.activity_wiki);
        mFrag1 = new WikiFragment();
        mFrag2 = new DetailsFragment();
        if (savedInstanceState == null) {
           // FragmentManager fragmentManager = getFragmentManager();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frgmCont, mFrag1)
                    .commit();
        }
        myTitle = getTitle();
        myDrawerTitle = getResources().getString(R.string.menu);
        // load slide menu items
        viewsNames = getResources().getStringArray(R.array.views_array);
        myDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        myDrawerList = (ListView) findViewById(R.id.left_drawer);
        myDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, viewsNames));
        // enabling action bar app icon and behaving it as toggle button
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        myDrawerToggle = new ActionBarDrawerToggle(this, myDrawerLayout,
                R.drawable.ic_drawer, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(myTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(myDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };

        myDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, Gravity.START);
        myDrawerLayout.setDrawerListener(myDrawerToggle);
        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }
        myDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mAm = AccountManager.get(this);
        if (sAccount == null) {
            sAccount = new Account(getString(R.string.news), ACCOUNT_TYPE);
        }
        if (mAm.addAccountExplicitly(sAccount, getPackageName(), new Bundle())) {
            ContentResolver.setSyncAutomatically(sAccount, AUTHORITY, true);
        }
        try {
            mAm.setUserData(sAccount, "Token", EncrManager.encrypt(this, VkOAuthHelper.mAccessToken));
        } catch (Exception e) {

        }
    }

    @Override
    public void someEvent(NoteGsonModel note) {
       // FragmentManager fragmentManager = getFragmentManager();
       //        Fragment fr3 = fr2.getChildFragmentManager().findFragmentById(R.id.frgmCont3);
        NoteGsonModel noteGsonModel = (NoteGsonModel) note;
        Bundle bundle = new Bundle();
        bundle.putParcelable("key", noteGsonModel);
        mFrag2 = new DetailsFragment();
        mFrag3 = new ChildFragment();
        mFrag2.setArguments(bundle);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            getSupportFragmentManager().beginTransaction()
                .hide(mFrag1)
                .addToBackStack(null)
                .replace(R.id.frgmCont2, mFrag2)
                .replace(R.id.frgmCont3, mFrag3)
                .commit();
        }
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
    getSupportFragmentManager().beginTransaction()
    .replace(R.id.frgmCont, mFrag1)
    .replace(R.id.frgmCont2, mFrag2)
    .replace(R.id.frgmCont3, mFrag3)
    .commit();
        }
     }



    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(
                AdapterView<?> parent, View view, int position, long id
        ) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
            case 0:
                 break;
            case 1:
                pager = (ViewPager) findViewById(R.id.pager);
                pager.setVisibility(View.VISIBLE);
                pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
                pager.setAdapter(pagerAdapter);

                pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                    @Override
                    public void onPageSelected(int position) {
                        Log.d(TAG, "onPageSelected, position = " + position);
                    }

                    @Override
                    public void onPageScrolled(int position, float positionOffset,
                                               int positionOffsetPixels) {
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                    }
                });
                break;
            case 2:
                //    fragment = new ThirdFragment();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (myDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if navigation drawer is opened, hide the action items
        boolean drawerOpen = myDrawerLayout.isDrawerOpen(myDrawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void setTitle(CharSequence title) {
        myTitle = title;
        getActionBar().setTitle(myTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        myDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        myDrawerToggle.onConfigurationChanged(newConfig);
    }
}