package com.example.evgen.apiclient.account;

import android.accounts.AccountAuthenticatorActivity;
import android.os.Bundle;

/**
 * Created by evgen on 04.11.2014.
 */
public class AuthenticatorActivity extends AccountAuthenticatorActivity {


    @Override
    public void onCreate(Bundle icicle) {
        //TODO login activity
        super.onCreate(icicle);
        finish();
    }

}
