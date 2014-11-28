package com.example.evgen.apiclient;

/**
 * Created by User on 30.10.2014.
 */

import android.accounts.Account;
import android.accounts.AccountManager;


import android.content.Intent;
import android.support.v4.app.DialogFragment;
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
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.evgen.apiclient.auth.VkOAuthHelper;
import com.example.evgen.apiclient.auth.secure.EncrManager;
import com.example.evgen.apiclient.bo.Note;
import com.example.evgen.apiclient.bo.NoteGsonModel;
import com.example.evgen.apiclient.dialogs.ErrorDialog;
import com.example.evgen.apiclient.fragments.ChildFragment;
import com.example.evgen.apiclient.fragments.DetailsFragment;
import com.example.evgen.apiclient.fragments.MyFragmentPagerAdapter;
import com.example.evgen.apiclient.fragments.WikiFragment;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class WikiActivity extends FragmentActivity implements WikiFragment.Callbacks {
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
   // public static final String URL = "https://en.wikipedia.org/w/api.php?action=query&prop=categories&format=json&titles=Albert%20Einstein";
    private static final String TAG = VkOAuthHelper.class.getSimpleName();
    public static final String ACCOUNT_TYPE = "com.example.evgen.apiclient.account";
    private Fragment mFrag1;
    private Fragment mFrag2;
    private Fragment mFrag3;
    public static final String AUTHORITY = "com.example.evgen.apiclient";
    private AccountManager mAm;
    public static Account sAccount;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    boolean mDualPane;
    private View  mDetailsFrame;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wiki);
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mDetailsFrame = findViewById(R.id.frgmCont2);
        mDualPane = mDetailsFrame != null && mDetailsFrame.getVisibility() == View.VISIBLE;
        myTitle = getTitle();
        myDrawerTitle = getResources().getString(R.string.menu);
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
            DialogFragment newFragment = ErrorDialog.newInstance(e.getMessage());
            newFragment.show(getSupportFragmentManager(), "Account");
        }
    }

    @Override
    public void onShowDetails(int index, NoteGsonModel note) {
        if (mDualPane) {
            // We can display everything in-place with fragments, so update
            // the list to highlight the selected item and show the data.
            // Check what fragment is currently shown, replace if needed.
            DetailsFragment details = (DetailsFragment)
                    getSupportFragmentManager().findFragmentById(R.id.frgmCont2);
            if (details == null || details.getShownIndex() != index) {
                // Make new fragment to show this selection.
                details = DetailsFragment.newInstance(index);
                NoteGsonModel noteGsonModel = (NoteGsonModel) note;
                Bundle bundle = new Bundle();
                bundle.putParcelable("key", noteGsonModel);
                details.setArguments(bundle);
                // Execute a transaction, replacing any existing fragment
                // with this one inside the frame.
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frgmCont2, details)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
            }

        } else {
            // Otherwise we need to launch a new activity to display
            // the dialog fragment with selected text.
            NoteGsonModel noteGsonModel = (NoteGsonModel) note;
            Bundle bundle = new Bundle();
            bundle.putParcelable("key", noteGsonModel);
            Intent intent = new Intent();
            intent.setClass(this, DetailsFragmentActivity.class);
            intent.putExtra("key", bundle);
            startActivity(intent);
        }
    }


    @Override
    public boolean isDualPane() {
        return false;
    }

    @Override
    public void onErrorA(Exception e) {
        DialogFragment newFragment = ErrorDialog.newInstance(e.getMessage());
        newFragment.show(getSupportFragmentManager(), "dialog");
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(
                AdapterView<?> parent, View view, int position,  long id
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
                mDetailsFrame = findViewById(R.id.frgmCont2);
                if (mDualPane = mDetailsFrame != null){
                    mDetailsFrame.setVisibility(View.VISIBLE);
                }
                findViewById(R.id.titles).setVisibility(View.VISIBLE);
                mPager.setVisibility(View.GONE);
                myDrawerLayout.closeDrawer(myDrawerList);
                break;
            case 1:
                mDetailsFrame = findViewById(R.id.frgmCont2);
                if (mDualPane = mDetailsFrame != null){
                   mDetailsFrame.setVisibility(View.GONE);
                }
                findViewById(R.id.titles).setVisibility(View.GONE);
                mPager.setVisibility(View.VISIBLE);
                mPager.setOffscreenPageLimit(10);
                myDrawerLayout.closeDrawer(myDrawerList);
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