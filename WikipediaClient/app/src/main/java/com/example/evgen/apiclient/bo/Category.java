package com.example.evgen.apiclient.bo;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

/**
 * Created by User on 17.11.2014.
 */
public class Category extends JSONObjectWrapper {
    public static final String URL = "fullurl";
    public static final String URLIMAGE = "source";
    public static final String URLFOTO = "photo_200_orig";
    private static final String ACTION = "action";
    private static final String PROP = "prop";
    private static final String FORMAT = "format";
    private static final String NS = "ns";
    private static final String TITLE = "title";
//    private static final String ONLINE = "online";
    private static final String ID = "id";
    private static final String DIST = "dist";
    private static final String FIRSTNAME = "first_name";
    private static final String LASTNAME = "last_name";
    private static final String LINE = "line";

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

    public String getLINE() {
        return getString(LINE);
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

    public String getURLFOTO() {
        return getString(URLFOTO);
    }

    public String getURLIMAGE() {
        return getString(URLIMAGE);
    }

    public String getFIRSTNAME() {
        return getString(FIRSTNAME);
    }

    public String getLASTNAME() {
        return getString(LASTNAME);
    }


    public String getDIST() {
        return getString(DIST);
    }

    public void initName() {
        set(NAME, getTITLE());
    }

    public void initUrl() {
        set(NAME, getURL());
    }

    public Long getId() {
        return getLong(ID);
    }

}