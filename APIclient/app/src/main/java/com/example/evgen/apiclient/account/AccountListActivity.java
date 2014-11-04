package com.example.evgen.apiclient.account;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.os.Bundle;

import com.example.evgen.apiclient.R;

import java.io.IOException;

/**
 * Created by evgen on 03.11.2014.
 */
public class AccountListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_single_frame);
        final AccountManager am = AccountManager.get(this);
        if (am.getAccountsByType(WikiAccount.TYPE).length == 0) {
            addNewAccount(am);
        }
        if (savedInstanceState == null) {
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.frame1, new AccountList())
                    .commit();
        }
    }

    private void addNewAccount(AccountManager am) {
        am.addAccount(WikiAccount.TYPE, WikiAccount.TOKEN_FULL_ACCESS, null, null, this,
                new AccountManagerCallback<Bundle>() {
                    @Override
                    public void run(AccountManagerFuture<Bundle> future) {
                        try {
                            try {
                                future.getResult();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (AuthenticatorException e) {
                                e.printStackTrace();
                            }
                        } catch (OperationCanceledException e) {
                            AccountListActivity.this.finish();
                        }
                    }
                }, null
        );
    }

}