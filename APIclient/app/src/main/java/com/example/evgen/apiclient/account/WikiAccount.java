package com.example.evgen.apiclient.account;

import android.accounts.Account;
import android.os.Parcel;

/**
 * Created by evgen on 03.11.2014.
 */
public class WikiAccount extends Account {

    public static final String TYPE = "com.github.elegion";

    public static final String TOKEN_FULL_ACCESS = "com.github.elegion.TOKEN_FULL_ACCESS";

    public static final String KEY_PASSWORD = "com.github.elegion.KEY_PASSWORD";

    public WikiAccount(Parcel in) {
        super(in);
    }

    public WikiAccount(String name) {
        super(name, TYPE);
    }

}
