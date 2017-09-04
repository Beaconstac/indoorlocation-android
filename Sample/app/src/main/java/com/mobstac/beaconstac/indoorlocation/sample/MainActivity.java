package com.mobstac.beaconstac.indoorlocation.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mobstac.beaconstac.indoorlocation.Beacon;
import com.mobstac.beaconstac.indoorlocation.BeaconstacIndoorLocation;
import com.mobstac.beaconstac.indoorlocation.Place;
import com.mobstac.beaconstac.indoorlocation.core.BeaconstacNavException;
import com.mobstac.beaconstac.indoorlocation.interfaces.AuthListener;
import com.mobstac.beaconstac.indoorlocation.interfaces.LocationCallBack;

public class MainActivity extends AppCompatActivity implements LocationCallBack {

    TextView logView;
    Button startNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BeaconstacIndoorLocation.initialise(this, "YOUR_API_KEY", new AuthListener() {
            @Override
            public void onComplete(BeaconstacNavException e) {
                if (e == null)
                    BeaconstacIndoorLocation.getInstance().startTracking(MainActivity.this, MainActivity.this);
            }
        });
        logView = (TextView) findViewById(R.id.callback_log);
        startNavigation = (Button) findViewById(R.id.start_navigation);
        startNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NavigationActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (logView != null) {
            logView.setText("");
            BeaconstacIndoorLocation.getInstance().startTracking(this, this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        BeaconstacIndoorLocation.getInstance().stopTracking();
    }

    @Override
    public void onBeaconProximity(Beacon beacon) {
        if (logView != null) {
            logView.setText("");
            logView.append("You are now at " + beacon.getName());
            logView.append("\n\n" + beacon.getLat() + ", " + beacon.getLng());
            logView.append("\n\n~ +/- " + beacon.calculateDistance() + " metres");
        }
    }

    @Override
    public void onRegionExit() {
        if (logView != null)
            logView.setText("Out of range");
    }

    @Override
    public void onError(BeaconstacNavException exception) {
        if (logView != null)
            logView.setText("Error: " + exception.getMessage());
    }

    @Override
    public void onRegionEntry(Place place) {
        if (logView != null)
            logView.setText("Entering " + place.getName());
    }
}
