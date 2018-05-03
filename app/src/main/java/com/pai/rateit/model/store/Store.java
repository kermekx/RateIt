package com.pai.rateit.model.store;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by kevin on 21/04/2018.
 */

public class Store {

    private String name;
    private String subtitle;
    private String address;
    private double lat;
    private double lon;
    private int x;
    private int y;

    public Store() {

    }

    public Store(@NonNull String name, String subtitle, String address, double lat, double lon,
                 int x, int y) {
        this.name = name;
        this.subtitle = subtitle;
        this.address = address;
        this.lat = lat;
        this.lon = lon;
        this.x = x;
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
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

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
