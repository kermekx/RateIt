package com.pai.rateit.model.store;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by kevin on 21/04/2018.
 */

public class Store {

    private String name;
    private String address;
    private double lat;
    private double lon;

    public Store() {

    }

    public Store(String name, String address, double lat, double lon) {
        this.name = name;
        this.address = address;
        this.lat = lat;
        this.lon = lon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public LatLng getLatLng() {
        return new LatLng(getLat(), getLon());
    }
}
