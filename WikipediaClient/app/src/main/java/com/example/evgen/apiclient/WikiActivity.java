package com.example.evgen.apiclient;

/**
 * Created by User on 30.10.2014.
 */

import android.accounts.Account;
import android.accounts.AccountManager;


import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.Context;
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
import android.view.MenuInflater;
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
import com.example.evgen.apiclient.listener.RightDrawerItemClickListener;
import com.example.evgen.apiclient.view.SearchViewValue;
import com.example.evgen.apiclient.view.VkUserDataView;

import java.util.List;

//TODO clear unused code
public class WikiActivity extends ActionBarActivity implements WikiFragment.Callbacks, DetailsFragment.Callbacks, SearchFragment.Callbacks, SearchView.OnQueryTextListener, VkUserDataView.Callbacks {
    private DrawerLayout myDrawerLayout;
    private ListView myDrawerList;
    private ListView mDrawerListRight;
    private ActionBarDrawerToggle myDrawerToggle;
    // navigation drawer title
    private CharSequence myDrawerTitle;
    // used to store app title
    private CharSequence myTitle;
    private String[] viewsNames;
    private ArrayAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    public static final String ACCOUNT_TYPE = "com.example.evgen.apiclient.account";
    public static final String AUTHORITY = "com.example.evgen.apiclient";
    private AccountManager mAm;
    //TODO why static?
    public static Account sAccount;
    private ViewPager mPager;
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
     //   handleIntent ( getIntent ());
        if (savedInstanceState == null) {
            //TODO WTF remove and implement correct search with SearchManager + save history
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            SearchFragment fragment = new SearchFragment();
            transaction.replace(R.id.framemain, fragment);
            transaction.commit();
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        mPager = (ViewPager) findViewById(R.id.pager);
        mDetailsFrame = findViewById(R.id.frgmCont2);
        mDualPane = mDetailsFrame != null && mDetailsFrame.getVisibility() == View.VISIBLE;
        myTitle = getTitle();
        myDrawerTitle = getResources().getString(R.string.menu);
        viewsNames = getResources().getStringArray(R.array.views_array);
        myDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        myDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.primary_dark_material_light));

        headerDrawer = View.inflate(this, R.layout.view_header, null);
        myDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerListRight = (ListView) findViewById(R.id.list_right_menu);
        myDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,mDrawerListRight);
        myDrawerList.setHeaderDividersEnabled(true);
        myDrawerList.addHeaderView(headerDrawer);
        myDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item,android.R.id.text2, viewsNames));
        VkUserDataView vkUserDataView = new VkUserDataView(this);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);//setDisplayShowTitleEnabled(true);
        myDrawerToggle = new ActionBarDrawerToggle(this, myDrawerLayout,
                toolbar,
                //R.drawable.ic_action, //nav menu toggle icon
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
//        if (savedInstanceState == null) {
//            // on first time display view for first nav item
//            displayView(0);
//        }
        myDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerListRight.setOnItemClickListener(new RightDrawerItemClickListener());
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
        Log.d(LOG_TAG, "FirstName"+first);
        TextView firstname = (TextView) headerDrawer.findViewById(R.id.text1);
        TextView lastname = (TextView) headerDrawer.findViewById(R.id.text2);
        ImageView fotos = (ImageView) headerDrawer.findViewById(R.id.icon);
        fotos.setImageBitmap(foto);
        firstname.setText(first);
        lastname.setText(last);
    }

    @Override
    public void onSetContents(List data) {
        mDrawerListRight.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item,android.R.id.text2, data));
    }

    public static interface Callbacks<String> {
        void onSearchValue(String value);
    }

    //TODO it is not working
    @Override
    public void onShowDetails(int index, NoteGsonModel note) {
        if (mDualPane) {
            DetailsFragment details = (DetailsFragment)getSupportFragmentManager().findFragmentById(R.id.frgmCont2);

            if (details == null || details.getShownIndex() != index) {
                details = new DetailsFragment();
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
            myDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            DetailsFragment detailsmain = new DetailsFragment();//DetailsFragment.newInstance(index);
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


    //TODO is large...
    @Override
    public boolean isDualPane() {
        return false;
    }

    //TODO rename
    @Override
    public void onErrorA(Exception e) {
        DialogFragment newFragment = ErrorDialog.newInstance(e.getMessage());
        newFragment.show(getSupportFragmentManager(), "dialog");
    }

    //TODO
    @Override
    public boolean onQueryTextSubmit(String s) {
<<<<<<< HEAD
       // displayView(1);
       // SearchViewValue.endsearch(this, s);
        onSentSearch(s);
=======
        SearchViewValue.endsearch(s);
>>>>>>> d0ab09d8384f1298dcd7b066c5bfe80cd27f1058
        return true;
    }

    private void onSentSearch(String s){
      //  displayView(1);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        SearchFragment fragmentmain = new SearchFragment();
        Bundle bundle = new Bundle();
        bundle.putString("key", s);
        fragmentmain.setArguments(bundle);
        transaction.replace(R.id.framemain, fragmentmain);
        transaction.commit();
      //  SearchViewValue.endsearch(this, s);
    }

    @Override
    public boolean onQueryTextChange(String s) {
//       SearchViewValue.textsearch(s);
       return true;
    }

//    @Override
//    protected  void onNewIntent ( Intent intent )  {
//        setIntent ( intent );
//        handleIntent ( intent );
//    }
//
//    private  void handleIntent ( Intent intent )  {
//        if  ( Intent . ACTION_SEARCH . equals ( intent . getAction ()))  {
//            String query = intent . getStringExtra ( SearchManager. QUERY );
//     //       displayView(1);
//            SearchViewValue.endsearch(WikiActivity.this, query);
//
//
//        }
//    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(
                AdapterView<?> parent, View view, int position,  long id
        ) {
            displayView(position);
        }
    }

    //TODO change position to ENUM
    private void displayView(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:


            case 1:
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                SearchFragment fragmentmain = new SearchFragment();
                transaction.replace(R.id.framemain, fragmentmain);
                transaction.commit();

            case 2:

<<<<<<< HEAD

=======
                break;
>>>>>>> d0ab09d8384f1298dcd7b066c5bfe80cd27f1058
            case 3:
                FragmentTransaction transactionwiki = getSupportFragmentManager().beginTransaction();
                WikiFragment fragmentwiki = new WikiFragment();
                transactionwiki.replace(R.id.framemain, fragmentwiki);
                transactionwiki.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transactionwiki.commit();

            default:
                myDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,mDrawerListRight);
                myDrawerLayout.closeDrawer(myDrawerList);
                break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint("Search");
        searchView.setOnQueryTextListener(this);
<<<<<<< HEAD
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.main, menu);
//
//        // Get the SearchView and set the searchable configuration
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
//        // Assumes current activity is the searchable activity
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        return true;
=======
      return true;
>>>>>>> d0ab09d8384f1298dcd7b066c5bfe80cd27f1058
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (myDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = myDrawerLayout.isDrawerOpen(myDrawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void setTitle(CharSequence title) {
        myTitle = title;
        getSupportActionBar().setTitle(myTitle);
    }

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