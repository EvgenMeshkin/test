package com.example.evgen.apiclient.bo;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by User on 22.10.2014.
 */
public class JSONObjectWrapper implements Parcelable {

    private JSONObject mJO;

    public JSONObjectWrapper(String jsonObject) {
        try {
            mJO = new JSONObject(jsonObject);
        } catch (JSONException e) {
            throw new IllegalArgumentException("invalid json string");
        }
    }

    public JSONObjectWrapper(JSONObject jsonObject) {
        mJO = jsonObject;
    }

    protected String getString(String key) {
        return mJO.optString(key);
    }

    protected Long getLong(String id) {
        return mJO.optLong(id);
    }

    @Override
    public String toString() {
        return mJO.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mJO.toString());
    }

    /**
     * Read from parcel.
     *
     * @param in
     *            the in
     */
    protected void readFromParcel(final Parcel in) {
        String string = in.readString();
        try {
            mJO = new JSONObject(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static final Parcelable.Creator<JSONObjectWrapper> CREATOR = new Creator<JSONObjectWrapper>() {

        @Override
        public JSONObjectWrapper createFromParcel(Parcel source) {
            return new JSONObjectWrapper(source);
        }

        @Override
        public JSONObjectWrapper[] newArray(int size) {
            return new JSONObjectWrapper[size];
        }
    };

    public JSONObjectWrapper(Parcel source) {
        this(source.readString());
    }

}
