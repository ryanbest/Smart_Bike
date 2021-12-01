package com.example.myapplication.model;

public class LatLongModel{
    Double lat,lan;


    public LatLongModel() {
    }

    public LatLongModel(Double lat, Double lan) {
        this.lat = lat;
        this.lan = lan;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLan() {
        return lan;
    }

    public void setLan(Double lan) {
        this.lan = lan;
    }
}
