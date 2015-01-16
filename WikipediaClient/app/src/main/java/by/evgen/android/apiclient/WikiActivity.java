package by.evgen.android.apiclient;

/**
 * Created by User on 30.10.2014.
 */

import android.accounts.Account;
import android.accounts.AccountManager;


import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.content.ContentResolver;
import android.os.Build;
import android.os.Bundle;

import android.content.res.Configuration;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.app.*;
import by.evgen.android.apiclient.auth.VkOAuthHelper;
import by.evgen.android.apiclient.auth.secure.EncrManager;
import by.evgen.android.apiclient.bo.NoteGsonModel;
import by.evgen.android.apiclient.dialogs.ErrorDialog;
import by.evgen.android.apiclient.fragments.AbstractFragment;
import by.evgen.android.apiclient.fragments.DetailsFragment;
import by.evgen.android.apiclient.fragments.SearchFragment;
import by.evgen.android.apiclient.fragments.WatchListFragment;
import by.evgen.android.apiclient.fragments.WikiFragment;
import by.evgen.android.apiclient.helper.LikeVkNotes;
import by.evgen.android.apiclient.helper.LoadRandomPage;
import by.evgen.android.apiclient.helper.SentsVkNotes;
import by.evgen.android.apiclient.listener.RightDrawerItemClickListener;
import by.evgen.android.apiclient.helper.LoadVkUserData;
import by.evgen.android.apiclient.utils.Log;

import java.util.List;

//TODO clear unused code
public class WikiActivity extends ActionBarActivity implements AbstractFragment.Callbacks, DetailsFragment.Callbacks, SearchView.OnQueryTextListener, LoadVkUserData.Callbacks, LoadRandomPage.Callbacks, SentsVkNotes.Callbacks, WatchListFragment.Callbacks {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ListView mDrawerListRight;
    private ActionBarDrawerToggle mDrawerToggle;
    NoteGsonModel mNoteGsonModel;
    // navigation drawer title
    private CharSequence mDrawerTitle;
    // used to store app title
    private CharSequence mTitle;
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
    private enum mMenuValue {Home, Random, Nearby, Favourites, Watchlist, Settings, Log_in};

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wiki);
//        if (savedInstanceState == null) {
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            SearchFragment fragment = new SearchFragment();
//            transaction.replace(R.id.framemain, fragment);
//            transaction.commit();
//        }
        mVisibleMenu = 1;
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        mPager = (ViewPager) findViewById(R.id.pager);
        mTitle = getTitle();
        mDrawerTitle = getResources().getString(R.string.menu);
        viewsNames = getResources().getStringArray(R.array.views_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.primary_dark_material_light));
        headerDrawer = View.inflate(this, R.layout.view_header, null);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerListRight = (ListView) findViewById(R.id.list_right_menu);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, mDrawerListRight);
        mDrawerList.addHeaderView(headerDrawer);
        mDrawerList.setHeaderDividersEnabled(true);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, android.R.id.text2, viewsNames));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);//setDisplayShowTitleEnabled(true);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar,
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                supportInvalidateOptionsMenu();
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, Gravity.START);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
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
            LoadVkUserData loadVkUserData = new LoadVkUserData(this);
            mLikeItem.setEnabled(true);

        } catch (Exception e) {
        }
    }

    @Override
    public void onUserData(Bitmap foto, String first, String last) {
        Log.text(this.getClass(), "FirstName" + first);
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
    public void onShowDetails(NoteGsonModel note) {
            mVisibleMenu = 1;
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            DetailsFragment detailsmain = new DetailsFragment();
            mNoteGsonModel = (NoteGsonModel) note;
            Bundle bundle = new Bundle();
            bundle.putParcelable("key", mNoteGsonModel);
            detailsmain.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.framemain,detailsmain)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
    }

    @Override
    public void onErrorDialog(Exception e) {
        DialogFragment newFragment = ErrorDialog.newInstance(e.getMessage());
        newFragment.show(getSupportFragmentManager(), "dialog");
    }

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

    private void displayView(int position) {
      if (position != 0) {
          switch (mMenuValue.valueOf(viewsNames[position - 1])) {
              case Home:
                  mVisibleMenu = 0;
                  FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                  SearchFragment fragmentmain = new SearchFragment();
                  transaction.replace(R.id.framemain, fragmentmain);
                  transaction.commit();
                  mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, mDrawerListRight);
                  mDrawerLayout.closeDrawer(mDrawerList);
                  break;
              case Random:
                  mVisibleMenu = 1;
                  LoadRandomPage load = new LoadRandomPage();
                  load.loadingRandomPage(this);
                  mDrawerLayout.closeDrawer(mDrawerList);
                  break;
              case Nearby:
                  mVisibleMenu = 0;
                  FragmentTransaction transactionwiki = getSupportFragmentManager().beginTransaction();
                  WikiFragment fragmentwiki = new WikiFragment();
                  transactionwiki.replace(R.id.framemain, fragmentwiki);
                  transactionwiki.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                  transactionwiki.commit();
                  mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, mDrawerListRight);
                  mDrawerLayout.closeDrawer(mDrawerList);
                  break;
              case Favourites:
                  mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, mDrawerListRight);
                  mDrawerLayout.closeDrawer(mDrawerList);
                  break;
              case Watchlist:
                  mVisibleMenu = 0;
                  FragmentTransaction transactionwatch = getSupportFragmentManager().beginTransaction();
                  WatchListFragment fragmentwatch = new WatchListFragment();
                  transactionwatch.replace(R.id.framemain, fragmentwatch);
                  transactionwatch.commit();
                  mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, mDrawerListRight);
                  mDrawerLayout.closeDrawer(mDrawerList);
                  break;
              case Settings:

                  break;

              case Log_in:
                  FragmentTransaction buck = getSupportFragmentManager().beginTransaction();
                  SearchFragment fragmentbuck = new SearchFragment();
                  buck.replace(R.id.framemain, fragmentbuck);
                  buck.commit();
                  startActivity(new Intent(this, StartActivity.class));
              default:
                  mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, mDrawerListRight);
                  mDrawerLayout.closeDrawer(mDrawerList);
                  break;
          }
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
        Log.text(getClass(), "sentNote");
        if (!mNoteGsonModel.equals(null)) {
            new SentsVkNotes(this, this, mNoteGsonModel.getTitle().replaceAll(" ", "_"));
        }
    }

    public void sentLike(MenuItem item) {
        Log.text(getClass(), "sentLike");
        if (!mNoteGsonModel.equals(null)) {
           new LikeVkNotes(this, mNoteGsonModel.getTitle().replaceAll(" ", "_"));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
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
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
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
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

}