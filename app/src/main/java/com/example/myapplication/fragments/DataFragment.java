package com.example.myapplication.fragments;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DataFragment extends Fragment implements OnChartValueSelectedListener {


    LineChart rideLineChart,potholeLineChart,caloriesLineChart,distanceLineChart;

    private static final String TAG = "DATAFRAG";




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data, container, false);
        bindview(view);


        Calendar c = Calendar.getInstance();
        //c.add(Calendar.MONTH, -1); // Remove this line later
        int daysInMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH);




        getData("ride_time",rideLineChart);
        getData("calories",caloriesLineChart);
        getData("distance_traveled",distanceLineChart);
        getData("num_pothole",potholeLineChart);



        return view;

    }

    private void bindview(View view) {
        rideLineChart = view.findViewById(R.id.ride_line_chart);
        potholeLineChart = view.findViewById(R.id.potholes_line_chart);
        caloriesLineChart = view.findViewById(R.id.calories_line_chart);
        distanceLineChart = view.findViewById(R.id.distance_line_chart);
    }

    private void manageLineChart(LineChart lineChart,List<Entry> entries, List<String> labels,String type) {
        LineDataSet dataSet = new LineDataSet(entries, "Dates"); // add entries to dataset
        dataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        dataSet.setLineWidth(2.0f);
        dataSet.setValueTextSize(10f);
        dataSet.setDrawFilled(true);
        dataSet.setFillFormatter(new IFillFormatter() {
            @Override
            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                return lineChart.getAxisLeft().getAxisMinimum();
            }
        });

        // set color of filled area
        if (com.github.mikephil.charting.utils.Utils.getSDKInt() >= 18) {
            // drawables only supported on api level 18 and above
            Drawable drawable = null;
            switch (type){
                case "ride_time":
                    drawable = ContextCompat.getDrawable(getActivity(),R.drawable.fade_green);
                    dataSet.setColor(ContextCompat.getColor(getActivity(), R.color.teal_700));
                    dataSet.setCircleColor(ContextCompat.getColor(getActivity(), R.color.teal_700));
                    break;
                case "num_pothole":
                    drawable = ContextCompat.getDrawable(getActivity(),R.drawable.fade_red);
                    dataSet.setColor(ContextCompat.getColor(getActivity(), R.color.red));
                    dataSet.setCircleColor(ContextCompat.getColor(getActivity(), R.color.red));
                    break;
                case "calories":
                    dataSet.setColor(ContextCompat.getColor(getActivity(), R.color.blue));
                    dataSet.setCircleColor(ContextCompat.getColor(getActivity(), R.color.blue));
                    drawable = ContextCompat.getDrawable(getActivity(),R.drawable.fade_blue);
                    break;
                case "distance_traveled":
                    dataSet.setColor(ContextCompat.getColor(getActivity(), R.color.brown));
                    dataSet.setCircleColor(ContextCompat.getColor(getActivity(), R.color.brown));
                    drawable = ContextCompat.getDrawable(getActivity(),R.drawable.fade_brown);
                    break;

            }


            dataSet.setFillDrawable(drawable);
        } else {
            dataSet.setFillColor(Color.GREEN);
        }


        LineData lineData = new LineData(dataSet);

        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                Log.e(TAG, "getFormattedValue: " + value);
                return labels.get((int) value);

            }
        });
        lineChart.getAxisRight().setDrawGridLines(false);
        lineChart.getDescription().setEnabled(false);


        YAxis leftAxis = lineChart.getAxisRight();
        leftAxis.setEnabled(false);

        lineChart.setData(lineData);

        lineChart.invalidate();
    }



    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }


    private void getData(String type,LineChart lineChart) {
        ArrayList<Entry> values = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("test_send");


        myRef.child("display").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot caloriesSnap = snapshot.child(type);

                int sr = 0;

                for (DataSnapshot child : caloriesSnap.getChildren()) {
                    sr = sr + 1;
                    double value = child.getValue(Double.class);
                    String key = child.getKey();

                    Log.e(TAG, "onDataChange:" + " SR:" + sr + " key: " + key + " value" + value);
                    values.add(new Entry(sr - 1, (float) value));
                    labels.add(key);
                }




                manageLineChart(lineChart,values, labels,type);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Failed to read value.", error.toException());

            }
        });

    }

}
