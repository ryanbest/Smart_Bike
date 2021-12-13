package com.example.myapplication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LatLongModel{


    private double location_lat;
    private double location_lon;
    private String message;


    public LatLongModel() {

    }


    public LatLongModel(double location_lat, double location_lon, String message) {
        this.location_lat = location_lat;
        this.location_lon = location_lon;
        this.message = message;
    }

    public double getLocation_lat() {
        return location_lat;
    }

    public void setLocation_lat(double location_lat) {
        this.location_lat = location_lat;
    }

    public double getLocation_lon() {
        return location_lon;
    }

    public void setLocation_lon(double location_lon) {
        this.location_lon = location_lon;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
