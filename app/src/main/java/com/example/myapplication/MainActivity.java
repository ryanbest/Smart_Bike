package com.example.myapplication;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.fragments.DataFragment;
import com.example.myapplication.fragments.HomeFragment;
import com.example.myapplication.fragments.SettingsFragment;
import com.github.anastr.speedviewlib.SpeedView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Timer;

public class MainActivity extends AppCompatActivity {
    private GoogleMap mMap;
    private Location prevLocaton = null;
    locdata prelocdata = new locdata();
    LocationManager mLocationManager;
    TextView mTextView;
    Timer timer;
    SpeedView speedometer;
    String newString;

    FrameLayout container;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindview();

        loadFragment(new HomeFragment());

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        loadFragment(new HomeFragment());
                        break;
                    case R.id.data:
                        loadFragment(new DataFragment());
                        break;
                    case R.id.settings:
                        loadFragment(new SettingsFragment());
                        break;
                }


                return true;
            }
        });
    }

    class locdata {
        long mtime;
        double latitude;
        double longitude;
    }

    private void bindview() {
        container = findViewById(R.id.container);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
