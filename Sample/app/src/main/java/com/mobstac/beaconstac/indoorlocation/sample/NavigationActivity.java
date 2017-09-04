package com.mobstac.beaconstac.indoorlocation.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.mobstac.beaconstac.indoorlocation.Beacon;
import com.mobstac.beaconstac.indoorlocation.BeaconstacNavigation;
import com.mobstac.beaconstac.indoorlocation.Place;
import com.mobstac.beaconstac.indoorlocation.core.BeaconstacNavException;
import com.mobstac.beaconstac.indoorlocation.interfaces.LocationCallBack;

public class NavigationActivity extends AppCompatActivity implements LocationCallBack {

    BeaconstacNavigation.BeaconstacIndoorNavigation beaconstacIndoorNavigation;
    public static final String TAG = "Beaconstac Navigation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        beaconstacIndoorNavigation = ((BeaconstacNavigation) getSupportFragmentManager()
                .findFragmentById(R.id.beaconstac_navigation_fragment))
                .getIndoorNavigationInstance();
        beaconstacIndoorNavigation.stopNavigation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (beaconstacIndoorNavigation != null) {
            beaconstacIndoorNavigation.stopNavigation();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (beaconstacIndoorNavigation != null) {
            beaconstacIndoorNavigation.startNavigation();
        }
    }

    @Override
    public void onBeaconProximity(Beacon beacon) {
        Log.d(TAG, "Currently at " + beacon.getName());
    }

    @Override
    public void onRegionExit() {
        Log.d(TAG, "Exit region");
    }

    @Override
    public void onError(BeaconstacNavException exception) {
        Log.e(TAG, exception.getMessage());
    }

    @Override
    public void onRegionEntry(Place place) {
        Log.d(TAG, "Entering " + place.getName());
    }
}
