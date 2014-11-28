package com.example.evgen.apiclient.account;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by evgen on 05.11.2014.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {

  //  public static final String KEY_FEED_ID = "sync.KEY_FEED_ID";

    public SyncAdapter(Context context) {
        super(context, true);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider,
                              SyncResult syncResult) {

    }

    private void syncFeeds(ContentProviderClient provider, SyncResult syncResult, String where, String[] whereArgs) {

    }

    private void syncFeed(String feedId, String feedUrl, ContentProviderClient provider, SyncResult syncResult) {

    }



}
