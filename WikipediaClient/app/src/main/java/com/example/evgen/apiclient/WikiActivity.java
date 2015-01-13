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
import android.opengl.Visibility;
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
import com.example.evgen.apiclient.bo.NoteGsonModel;
import com.example.evgen.apiclient.dialogs.ErrorDialog;
import com.example.evgen.apiclient.fragments.DetailsFragment;
import com.example.evgen.apiclient.fragments.SearchFragment;
import com.example.evgen.apiclient.fragments.WatchListFragment;
import com.example.evgen.apiclient.fragments.WikiFragment;
import com.example.evgen.apiclient.helper.DataManager;
import com.example.evgen.apiclient.helper.LikeVkNotes;
import com.example.evgen.apiclient.helper.LoadRandomPage;
import com.example.evgen.apiclient.helper.SentsVkNotes;
import com.example.evgen.apiclient.listener.RightDrawerItemClickListener;
import com.example.evgen.apiclient.view.VkUserDataView;

import java.util.List;

import static android.view.View.VISIBLE;

//TODO clear unused code
public class WikiActivity extends ActionBarActivity implements WikiFragment.Callbacks, DetailsFragment.Callbacks, SearchFragment.Callbacks, SearchView.OnQueryTextListener, VkUserDataView.Callbacks, LoadRandomPage.Callbacks, SentsVkNotes.Callbacks, WatchListFragment.Callbacks {
    private DrawerLayout myDrawerLayout;
    private ListView myDrawerList;
    private ListView mDrawerListRight;
    private ActionBarDrawerToggle myDrawerToggle;
    NoteGsonModel mNoteGsonModel;
    // navigation drawer title
    private CharSequence myDrawerTitle;
    // used to store app title
    private CharSequence myTitle;
    private String[] viewsNames;
    private ArrayAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    public static final String ACCOUNT_TYPE = "com.example.evgen.apiclient.account";
    public static final String AUTHORITY = "com.example.evgen.apiclient";
    public static final int requestL = 0;
    private AccountManager mAm;
    //TODO why static?
    public static Account sAccount;
    private ViewPager mPager;
    private View  mDetailsFrame;
    private View headerDrawer;
    private MenuItem mLikeItem;
    private MenuItem mNoteItem;
    private Menu mMenu;
    private Integer mVisibleMenu;
    final static String LOG_TAG = WikiActivity.class.getSimpleName();

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wiki);
        if (savedInstanceState == null) {
            //TODO WTF remove and implement correct search with SearchManager + save history
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            SearchFragment fragment = new SearchFragment();
            transaction.replace(R.id.framemain, fragment);
            transaction.commit();
        }
        mVisibleMenu = 1;
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        mPager = (ViewPager) findViewById(R.id.pager);
        myTitle = getTitle();
        myDrawerTitle = getResources().getString(R.string.menu);
        viewsNames = getResources().getStringArray(R.array.views_array);
        myDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        myDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.primary_dark_material_light));
        headerDrawer = View.inflate(this, R.layout.view_header, null);
        myDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerListRight = (ListView) findViewById(R.id.list_right_menu);
        myDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, mDrawerListRight);
        myDrawerList.addHeaderView(headerDrawer);
        myDrawerList.setHeaderDividersEnabled(true);
        myDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item,android.R.id.text2, viewsNames));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);//setDisplayShowTitleEnabled(true);
        myDrawerToggle = new ActionBarDrawerToggle(this, myDrawerLayout,
                toolbar,
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(myTitle);
                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(myDrawerTitle);
                supportInvalidateOptionsMenu();
            }
        };
        myDrawerToggle.setDrawerIndicatorEnabled(true);
        myDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, Gravity.START);
        myDrawerLayout.setDrawerListener(myDrawerToggle);
        myDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerListRight.setOnItemClickListener(new RightDrawerItemClickListener());
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mAm = AccountManager.get(this);
        LoadRandomPage load = new LoadRandomPage();
        load.loadingRandomPage(this);
        if (sAccount == null) {
            sAccount = new Account(getString(R.string.news), ACCOUNT_TYPE);
        }
        if (mAm.addAccountExplicitly(sAccount, getPackageName(), new Bundle())) {
            ContentResolver.setSyncAutomatically(sAccount, AUTHORITY, true);
        }
        try {
            mAm.setUserData(sAccount, "Token", EncrManager.encrypt(this, VkOAuthHelper.mAccessToken));
            VkUserDataView vkUserDataView = new VkUserDataView(this);
            mLikeItem.setEnabled(true);

        } catch (Exception e) {
        }
    }

    @Override
    public void onUserData(Bitmap foto, String first, String last) {
        Log.d(LOG_TAG, "FirstName" + first);
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

    @Override
    public void onShowDetails(int index, NoteGsonModel note) {
            mVisibleMenu = 1;
            myDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            DetailsFragment detailsmain = new DetailsFragment();//DetailsFragment.newInstance(index);
            mNoteGsonModel = (NoteGsonModel) note;
            Bundle bundle = new Bundle();
            bundle.putParcelable("key", mNoteGsonModel);
            detailsmain.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.framemain,detailsmain)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
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
       onSentSearch(s);
       return true;
    }

    private void onSentSearch(String s){
        mVisibleMenu = 0;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        SearchFragment fragmentmain = new SearchFragment();
        Bundle bundle = new Bundle();
        bundle.putString("key", s);
        fragmentmain.setArguments(bundle);
        transaction.replace(R.id.framemain, fragmentmain);
        transaction.commit();
     }

    @Override
    public boolean onQueryTextChange(String s) {
//       SearchViewValue.textsearch(s);
       return true;
    }

    @Override
    public void onReturnId(Long id) {

    }

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
                mVisibleMenu = 0;
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                SearchFragment fragmentmain = new SearchFragment();
                transaction.replace(R.id.framemain, fragmentmain);
                transaction.commit();
                myDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,mDrawerListRight);
                myDrawerLayout.closeDrawer(myDrawerList);
                break;
            case 2:
                mVisibleMenu = 1;
                LoadRandomPage load = new LoadRandomPage();
                load.loadingRandomPage(this);
                myDrawerLayout.closeDrawer(myDrawerList);
                break;
            case 3:
                mVisibleMenu = 0;
                FragmentTransaction transactionwiki = getSupportFragmentManager().beginTransaction();
                WikiFragment fragmentwiki = new WikiFragment();
                transactionwiki.replace(R.id.framemain, fragmentwiki);
                transactionwiki.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transactionwiki.commit();
                myDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,mDrawerListRight);
                myDrawerLayout.closeDrawer(myDrawerList);
                break;
            case 4:
                myDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,mDrawerListRight);
                myDrawerLayout.closeDrawer(myDrawerList);
                break;
            case 5:
                mVisibleMenu = 0;
                FragmentTransaction transactionwatch = getSupportFragmentManager().beginTransaction();
                WatchListFragment fragmentwatch = new WatchListFragment();
                transactionwatch.replace(R.id.framemain, fragmentwatch);
                transactionwatch.commit();
                myDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,mDrawerListRight);
                myDrawerLayout.closeDrawer(myDrawerList);
                break;
            case 6:

                break;

            case 7:
                FragmentTransaction buck = getSupportFragmentManager().beginTransaction();
                SearchFragment fragmentbuck = new SearchFragment();
                buck.replace(R.id.framemain, fragmentbuck);
                buck.commit();
                startActivity(new Intent(this, StartActivity.class));
            default:
                myDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,mDrawerListRight);
                myDrawerLayout.closeDrawer(myDrawerList);
                break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        mNoteItem = menu.findItem(R.id.action_note);
        mLikeItem = menu.findItem(R.id.action_like);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint("Search");
        searchView.setOnQueryTextListener(this);
    return true;
    }

    public void sentNote(MenuItem item) {
        Log.d(LOG_TAG, "sentNote");
        if (!mNoteGsonModel.equals(null)) {
            new SentsVkNotes(this, this, mNoteGsonModel.getTitle().replaceAll(" ", "_"));
        }
    }

    public void sentLike(MenuItem item) {
        Log.d(LOG_TAG, "sentLike");
        if (!mNoteGsonModel.equals(null)) {

            new LikeVkNotes(this, mNoteGsonModel.getTitle().replaceAll(" ", "_"));
        }
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
        mNoteItem = menu.findItem(R.id.action_note);
        mLikeItem = menu.findItem(R.id.action_like);
        if (mVisibleMenu == 0) {
            mLikeItem.setVisible(false);
            mNoteItem.setVisible(false);
        } else {
            mLikeItem.setVisible(true);
            mNoteItem.setVisible(true);
        }
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
        myDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        myDrawerToggle.onConfigurationChanged(newConfig);
    }
}