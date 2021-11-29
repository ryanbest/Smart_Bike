package com.example.myapplication;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.anastr.speedviewlib.SpeedView;
import com.google.android.gms.maps.GoogleMap;

import java.util.Timer;

public class MainActivity extends AppCompatActivity {
    private GoogleMap mMap;
    private Location prevLocaton=null;
    locdata prelocdata = new locdata();
    LocationManager mLocationManager;
    TextView mTextView;
    Timer timer;
    SpeedView speedometer;
    String newString;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    class locdata{
        long mtime;
        double latitude;
        double longitude;
    }
}
