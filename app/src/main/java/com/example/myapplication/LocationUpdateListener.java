package com.example.myapplication;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public interface LocationUpdateListener {
    public void onLocationUpdate(Location location);
}
