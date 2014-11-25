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

    GpsLocation() {}

    public static interface Callbacks {
        void onShowKor (double latitude,  double longitude);
    }
    Callbacks callbacks;
//    public void getloc (Context сontext){
//        LocationManager lm = (LocationManager) сontext.getSystemService(Context.LOCATION_SERVICE);
//        location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        Latitude=location.getLatitude();}
    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
 //       Callbacks callbacks = getCallbacks();
        callbacks.onShowKor(location.getLatitude(),location.getLongitude());
        Log.d("id", "Координаты изменены");
    }

//    private Callbacks getCallbacks() {
//        return findFirstResponderFor(null, Callbacks.class);
//    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
        Log.d("id", "Модуль отключен");
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
        Log.d("id", "Модуль включен");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
        Log.d("id", "Статус модуля изменен");
    }
}
