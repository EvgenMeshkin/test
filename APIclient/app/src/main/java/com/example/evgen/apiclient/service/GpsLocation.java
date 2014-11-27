package com.example.evgen.apiclient.service;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.example.evgen.apiclient.bo.NoteGsonModel;

/**
 * Created by User on 25.11.2014.
 */
public class GpsLocation implements LocationListener {
    private LocationManager lm;
    private static String mLatitude;
    private Callbacks callbacks;

    public interface Callbacks {
        void onShowKor (String latitude);
    }

    public void getloc (Callbacks callbacks, Context сontext) {
        this.callbacks = callbacks;
        lm = (LocationManager) сontext.getSystemService(Context.LOCATION_SERVICE);
        if (lm.getAllProviders().contains(LocationManager.NETWORK_PROVIDER))
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        else {
            if (lm.getAllProviders().contains(LocationManager.GPS_PROVIDER))
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        showLocation(location);
        Log.d("id", "Coordinates changed");
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
       showLocation(lm.getLastKnownLocation(provider));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    private void showLocation(Location location) {
        if (location == null)
            return;
        if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
            mLatitude = location.getLatitude()+"|"+location.getLongitude();
        } else if (location.getProvider().equals(
                LocationManager.NETWORK_PROVIDER)) {
            mLatitude = location.getLatitude()+"|"+location.getLongitude();
        }
        if (mLatitude != null) {
            lm.removeUpdates(this);
            callbacks.onShowKor(mLatitude);
        }
        Log.d("id", mLatitude);
    }
}
