package com.example.myapplication.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.myapplication.LocationUpdateListener;
import com.example.myapplication.MyLocation;
import com.example.myapplication.R;
import com.example.myapplication.Utils;
import com.example.myapplication.model.LatLongModel;
import com.github.anastr.speedviewlib.SpeedView;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    SpeedView speedometer;
    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    String newString = "10";
    locdata prelocdata = new locdata();
    MapView mMapView;
    private static final String TAG = "HomeFragment";

    FirebaseDatabase firebaseDatabase;
    TextToSpeech tts;
    List<LatLongModel> latLongModels = new ArrayList<>();
    TextView tvAlert;
    Long lastNotifiedStamp = 0l;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        bindview(view);

        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(this);

        tts = new TextToSpeech(getActivity(), status -> {

        });
        tts.setLanguage(Locale.US);

        newString = String.valueOf(Utils.getSpeedLimit(getActivity()));

        tvAlert.setText("Speed limit is set to " + newString + " Km/h.");

        return view;
    }

    private void bindview(View view) {

        speedometer = view.findViewById(R.id.speedView);
        tvAlert = view.findViewById(R.id.alert_tv);
//        mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
        mMapView = view.findViewById(R.id.mapView);


    }


    private void getData() {
        Log.e(TAG, "getData: ");
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("test_send");



        myRef.child("1-set").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

//                Log.e(TAG, "onDataChange: "+snapshot.getValue());

                double lat =  snapshot.child("location-lat").getValue(Double.class);
                double lon =  snapshot.child("location-lon").getValue(Double.class);



                    MarkerOptions markerOptions = new MarkerOptions();
                    LatLng latLng = new LatLng(lat, lon);
                    markerOptions.position(latLng);
                    markerOptions.icon(BitmapDescriptorFactory
                            .fromResource(R.drawable.pothole));
                    mMap.addMarker(markerOptions);




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Failed to read value.", error.toException());

            }
        });


        myRef.child("2-push").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.e(TAG, "onDataChange: "+" 2PUSH");
                latLongModels.clear();
                mMap.clear();

                for (DataSnapshot child: snapshot.getChildren()) {

                    LatLongModel latLongModel = new LatLongModel();
                    latLongModel.setLocation_lat(child.child("location-lat").getValue(Double.class));
                    latLongModel.setLocation_lon(child.child("location-lon").getValue(Double.class));
                    latLongModel.setMessage(child.child("message").getValue(String.class));

                    MarkerOptions markerOptions = new MarkerOptions();
                    LatLng latLng = new LatLng(latLongModel.getLocation_lat(), latLongModel.getLocation_lon());
                    markerOptions.position(latLng);
                    markerOptions.icon(BitmapDescriptorFactory
                            .fromResource(R.drawable.pothole));
                    mMap.addMarker(markerOptions);

                    latLongModels.add(latLongModel);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


        if (ActivityCompat.checkSelfPermission(getActivity()
                , android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // TODO: Consider calling 
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.
                            ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    101);
            return;
        }
        mMap.setMyLocationEnabled(true);

        getData();
        mMap.setOnMyLocationChangeListener(myLocationChangeListener);



//        MyLocation.LocationResult lr = new MyLocation.LocationResult() {
//
//            @Override
//            public void gotLocation(final Location location) {
//              setupTheLocation(location);
//            }
//
//        };
//        MyLocation myLocation = new MyLocation();
//        myLocation.getLocation(getActivity(), lr);
//        myLocation.setLocationUpdateListener(new LocationUpdateListener() {
//            @Override
//            public void onLocationUpdate(Location location) {
//                Log.e(TAG, "onLocationUpdate: ");
//                setupTheLocation(location);
//            }
//        });

    }

    private void setupTheLocation(Location location){
        Log.e(TAG, "gotLocation: "+location);

        LatLng myLaLn = new LatLng(location.getLatitude(), location.getLongitude());

        getData();


        final CameraPosition camPos = new CameraPosition.Builder().target(myLaLn)
                .zoom(17)
                .bearing(0)
                .tilt(30)
                .build();

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);
                mMap.animateCamera(camUpd3);
            }
        });


        for (LatLongModel latLongModel : latLongModels) {
            if (isInRangeMeters(myLaLn, new LatLng(latLongModel.getLocation_lat(), latLongModel.getLocation_lon()), 100)) {
                tts.speak("Be alert! You're approaching towards a pothole", TextToSpeech.QUEUE_ADD, null);
                tvAlert.setText("Be alert! You're approaching towards a pothole");
            }

        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                double Speed = getSpeed(location);
                int nSpeed = (
                        int) Speed;

                Log.e(TAG, "run: speed: " + nSpeed);
                //mTextView.setText(""+nSpeed);

                speedometer.setSpeedAt(nSpeed);
                if (nSpeed > Integer.parseInt(newString)) {

//                            Toast.makeText(getActivity(), "CAUTION YOU HAVE EXCEEDED YOUR SPEED LIMIT", Toast.LENGTH_SHORT).show();
                    tts.speak("CAUTION YOU HAVE EXCEEDED YOUR SPEED LIMIT", TextToSpeech.QUEUE_ADD, null);
                    tvAlert.setText("CAUTION YOU HAVE EXCEEDED YOUR SPEED LIMIT");
                    // Get instance of Vibrator from current Context
                    Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

                    // Vibrate for 400 milliseconds
                    v.vibrate(1000);
                }

                Log.d("Vehicle App", "OnLocation Changed");

            }
        });




    }

    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
            if(mMap != null){
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
            }
            LatLng myLaLn = new LatLng(location.getLatitude(), location.getLongitude());

            double Speed = getSpeed(location);
            int nSpeed = (
                    int) Speed;

            Log.e(TAG, "run: speed: " + nSpeed);
            //mTextView.setText(""+nSpeed);

            speedometer.setSpeedAt(nSpeed);
            if (nSpeed > Integer.parseInt(newString)) {

//                            Toast.makeText(getActivity(), "CAUTION YOU HAVE EXCEEDED YOUR SPEED LIMIT", Toast.LENGTH_SHORT).show();
                tts.speak("CAUTION YOU HAVE EXCEEDED YOUR SPEED LIMIT", TextToSpeech.QUEUE_ADD, null);
                tvAlert.setText("CAUTION YOU HAVE EXCEEDED YOUR SPEED LIMIT");
                // Get instance of Vibrator from current Context
                Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

                // Vibrate for 400 milliseconds
                v.vibrate(1000);
            }else{
              //  tvAlert.setText("Speed limit is set to " + newString + " Km/h.");
            }

            for (LatLongModel latLongModel : latLongModels) {
                if (isInRangeMeters(myLaLn, new LatLng(latLongModel.getLocation_lat(), latLongModel.getLocation_lon()), 100)) {
                    if (System.currentTimeMillis() - lastNotifiedStamp > 10000) {
                        Log.e(TAG, "onMyLocationChange: alerting ");
                        tts.speak("Be alert! You're approaching towards a pothole", TextToSpeech.QUEUE_ADD, null);
                        tvAlert.setText("Be alert! You're approaching towards a pothole");
                        lastNotifiedStamp = System.currentTimeMillis();
                    }else {
                        tvAlert.setText("Speed limit is set to " + newString + " Km/h.");
                    }
                }else{
                  //  tvAlert.setText("Speed limit is set to " + newString + " Km/h.");
               }

            }
        }
    };


    class locdata {
        long mtime;
        double latitude;
        double longitude;
    }

    private double getSpeed(Location location) {

        Log.e(TAG, "getSpeed: "+ location.getSpeed());

        double speed=(double) ((location.getSpeed()*3600)/1000);

//        double speed = 0;
//
//        Location currLoc = location;
//        locdata data = new locdata();
//        data.latitude = currLoc.getLatitude();
//        data.longitude = currLoc.getLongitude();
//        data.mtime = currLoc.getTime();
//
//
//        Log.d("vehicle", "prelocdata.latitude" + prelocdata.latitude);
//        Log.d("vehicle", "prelocdata.longitude" + prelocdata.longitude);
//        Log.d("vehicle", "data.latitude" + data.latitude);
//        Log.d("vehicle", "data.longitude" + data.longitude);
//        Log.d("vehicle", "prelocdata.mtime" + prelocdata.mtime);
//        Log.d("vehicle", "data.mtime" + data.mtime);
//        if (prelocdata.mtime != 0) {
//            double distance = getDistance(prelocdata.latitude, prelocdata.longitude,
//                    data.latitude, data.longitude);
//            speed = (distance / (data.mtime - prelocdata.mtime)) * 1000;
//            //mTextView.setText("" + speed);
//            Log.d("vehicle", "SPEED==" + speed);
//        }
//        prelocdata.mtime = currLoc.getTime();
//        prelocdata.latitude = currLoc.getLatitude();
//        prelocdata.longitude = currLoc.getLongitude();
//        Log.d("vehicle", "SPEED==" + speed);
//        if (speed >= 100) {
//
//            return speed / 10;
//        } else {
//            return speed;
//        }
        return speed;
    }

    private static double getDistance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371000; // for haversine use R = 6372.8 km instead of 6371 km
        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        //double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return 2 * R * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    }


    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.d("vehicle", "OnrequestPermissionsUP");
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d("vehicle", "OnrequestPermissions");
        } else {


            Log.d("Vehicle", "Location Permission denied");
        }

    }

    public static boolean isInRangeMeters(LatLng pt1, LatLng pt2, double meters) {
        boolean inRange = false;
        try {
            double distance = getDistanceMeters(pt1, pt2);

            inRange = (distance <= meters);
        } catch (Exception ex) {
            Log.e("ERROR", ex.getMessage());
        }
        return inRange;
    }

    public static double getDistanceMeters(LatLng pt1, LatLng pt2) {
        double distance = 0d;
        try {
            double theta = pt1.longitude - pt2.longitude;
            double dist = Math.sin(Math.toRadians(pt1.latitude)) * Math.sin(Math.toRadians(pt2.latitude))
                    + Math.cos(Math.toRadians(pt1.latitude)) * Math.cos(Math.toRadians(pt2.latitude))
                    * Math.cos(Math.toRadians(theta));

            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            distance = dist * 60 * 1853.1596;
        } catch (Exception ex) {
            Log.e("ERROR", ex.getMessage());
        }
        return distance;
    }
}
