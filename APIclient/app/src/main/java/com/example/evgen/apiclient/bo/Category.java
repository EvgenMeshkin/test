package com.example.evgen.apiclient.bo;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

/**
 * Created by User on 17.11.2014.
 */
public class Category extends JSONObjectWrapper {
    public static final String URL = "fullurl";
    private static final String ACTION = "action";
    private static final String PROP = "prop";
    private static final String FORMAT = "format";
    private static final String NS = "ns";
    private static final String TITLE = "title";
//    private static final String ONLINE = "online";
    private static final String ID = "id";

    //INTERNAL
    private static final String NAME = "NAME";

    public static final Parcelable.Creator<Category> CREATOR
            = new Parcelable.Creator<Category>() {
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    public Category(String jsonObject) {
        super(jsonObject);
    }

    public Category(JSONObject jsonObject) {
        super(jsonObject);
    }

    protected Category(Parcel in) {
        super(in);
    }

    public String getACTION() {
        return getString(ACTION);
    }

    public String getPROP() {
        return getString(PROP);
    }

    public String getFORMAT() {
        return getString(FORMAT);
    }

    public String getNS () {
        return getString(NS);
    }

    public String getTITLE () {
        return getString(TITLE );
    }
    public String getURL() {
        return getString(URL);
    }

    public void initName() {
        set(NAME, getTITLE());
    }
    public void initUrl() {
        set(NAME, getURL());
    }
//
//    public String getName() {
//        return getString(NAME);
//    }
//
//    public boolean isOnline() {
//        Boolean isOnline = getBoolean(ONLINE);
//        if (isOnline == null) {
//            return false;
//        }
//        return isOnline;
//    }
//
    public Long getId() {
        return getLong(ID);
    }

}