package by.evgen.android.apiclient.account;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by evgen on 05.11.2014.
 */
//TODO refactoring or read about that
public class SyncService extends Service {

    private static SyncAdapter sSyncAdapter;

    @Override
    public void onCreate() {
        super.onCreate();
        if (sSyncAdapter == null) {
            synchronized (SyncAdapter.class) {
                sSyncAdapter = new SyncAdapter(getApplicationContext());
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }


}