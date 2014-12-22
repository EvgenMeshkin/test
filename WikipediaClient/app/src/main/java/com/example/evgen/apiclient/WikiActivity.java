package com.example.evgen.apiclient;

/**
 * Created by User on 30.10.2014.
 */

import android.accounts.Account;
import android.accounts.AccountManager;


import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.app.*;
import com.example.evgen.apiclient.auth.VkOAuthHelper;
import com.example.evgen.apiclient.auth.secure.EncrManager;
import com.example.evgen.apiclient.bo.Note;
import com.example.evgen.apiclient.bo.NoteGsonModel;
import com.example.evgen.apiclient.dialogs.ErrorDialog;
import com.example.evgen.apiclient.fragments.ChildFragment;
import com.example.evgen.apiclient.fragments.DetailsFragment;
import com.example.evgen.apiclient.fragments.MyFragmentPagerAdapter;
import com.example.evgen.apiclient.fragments.SearchFragment;
import com.example.evgen.apiclient.fragments.WikiFragment;
import com.example.evgen.apiclient.helper.DataManager;
import com.example.evgen.apiclient.view.SearchViewValue;
import com.example.evgen.apiclient.view.VkUserDataView;


public class WikiActivity extends ActionBarActivity implements WikiFragment.Callbacks, SearchFragment.Callbacks,SearchView.OnQueryTextListener, VkUserDataView.Callbacks {
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
    private SearchViewValue mSearchViewValue;
    private View headerDrawer;
    final static String LOG_TAG = WikiActivity.class.getSimpleName();

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wiki);
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            SearchFragment fragment = new SearchFragment();
            transaction.replace(R.id.framemain, fragment);
            transaction.commit();
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mDetailsFrame = findViewById(R.id.frgmCont2);
        mDualPane = mDetailsFrame != null && mDetailsFrame.getVisibility() == View.VISIBLE;
        myTitle = getTitle();
        myDrawerTitle = getResources().getString(R.string.menu);
        viewsNames = getResources().getStringArray(R.array.views_array);
        myDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        myDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.primary_dark_material_light));
        headerDrawer = View.inflate(this, R.layout.view_header, null);
        myDrawerList = (ListView) findViewById(R.id.left_drawer);
        myDrawerList.setHeaderDividersEnabled(true);
        myDrawerList.addHeaderView(headerDrawer);
        myDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, viewsNames));
        //mSearchViewValue = new SearchViewValue();
        VkUserDataView vkUserDataView = new VkUserDataView(this);
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);//setDisplayShowTitleEnabled(true);
//        actionBar . setDisplayOptions ( ActionBar . DISPLAY_SHOW_HOME |  ActionBar . DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_HOME_AS_UP );
//        actionBar . setIcon ( R . drawable . ic_launcher );
        myDrawerToggle = new ActionBarDrawerToggle(this, myDrawerLayout,
                R.drawable.ic_action, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(myTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(myDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                supportInvalidateOptionsMenu();
            }
        };
        myDrawerToggle.setDrawerIndicatorEnabled(true);
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
    public void onUserData(Bitmap foto, String first, String last) {
        //setContentView(R.layout.view_header);
        Log.d(LOG_TAG, "FirstName"+first);
        TextView firstname = (TextView) headerDrawer.findViewById(R.id.text1);
        TextView lastname = (TextView) headerDrawer.findViewById(R.id.text2);
        ImageView fotos = (ImageView) headerDrawer.findViewById(R.id.icon);
        fotos.setImageBitmap(foto);
        firstname.setText(first);
        lastname.setText(last);
    }

    public static interface Callbacks<String> {
        void onSearchValue(String value);
    }

    @Override
    public void onShowDetails(int index, NoteGsonModel note) {
        if (mDualPane) {
            DetailsFragment details = (DetailsFragment)getSupportFragmentManager().findFragmentById(R.id.frgmCont2);

            if (details == null || details.getShownIndex() != index) {
                details = DetailsFragment.newInstance(index);
                NoteGsonModel noteGsonModel = (NoteGsonModel) note;
                Bundle bundle = new Bundle();
                bundle.putParcelable("key", noteGsonModel);
                details.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frgmCont2, details)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
            }
        } else {
//             detailsmain = (DetailsFragment)getSupportFragmentManager().findFragmentById(R.id.framemain);
//            NoteGsonModel noteGsonModel = (NoteGsonModel) note;
//            Bundle bundle = new Bundle();
//            bundle.putParcelable("key", noteGsonModel);
//            Intent intent = new Intent();
//            intent.setClass(this, DetailsFragmentActivity.class);
//            intent.putExtra("key", bundle);
//            startActivity(intent);
            DetailsFragment detailsmain = DetailsFragment.newInstance(index);
            NoteGsonModel noteGsonModel = (NoteGsonModel) note;
            Bundle bundle = new Bundle();
            bundle.putParcelable("key", noteGsonModel);
            detailsmain.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.framemain,detailsmain)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();

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

    @Override
    public boolean onQueryTextSubmit(String s) {
        SearchViewValue.endsearch(s);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        SearchViewValue.textsearch(s);
       return true;
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

                break;
            case 1:
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                SearchFragment fragmentmain = new SearchFragment();
                transaction.replace(R.id.framemain, fragmentmain);
                transaction.commit();
                break;
            case 2:

                //    fragment = new ThirdFragment();
                break;
            case 3:
//                WikiFragment detailsmain = (WikiFragment)getSupportFragmentManager().findFragmentById(R.id.framemain);
//                detailsmain = new WikiFragment();
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.titles,detailsmain)
//                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
//                        .commit();
                //    fragment = new ThirdFragment();
                FragmentTransaction transactionwiki = getSupportFragmentManager().beginTransaction();
                WikiFragment fragmentwiki = new WikiFragment();
                transactionwiki.replace(R.id.framemain, fragmentwiki);
                transactionwiki.commit();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint("Поиск");
        searchView.setOnQueryTextListener(this);

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
        getSupportActionBar().setTitle(myTitle);
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