package com.pai.rateit.factory.marker;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.pai.rateit.controller.maps.MarkerController;
import com.pai.rateit.model.store.Store;
import com.pai.rateit.utils.DistanceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 26/04/2018.
 */

public class MarkerFactory {

    private Context mContext;
    private GoogleMap mMap;
    private LocationManager mLocationManager;
    private Criteria mCriteria;
    private boolean showCursor = true;

    private List<Marker> markers = new ArrayList<>();

    public MarkerFactory() {

    }

    public MarkerFactory context(Context context) {
        mContext = context;
        return this;
    }

    public MarkerFactory map(GoogleMap map) {
        mMap = map;
        return this;
    }

    public MarkerFactory locationManager(LocationManager locationManager) {
        mLocationManager = locationManager;
        mCriteria = new Criteria();
        return this;
    }

    public MarkerFactory cameraMoveListener(GoogleMap.OnCameraMoveListener cameraMoveListener) {
        mMap.setOnCameraMoveListener(cameraMoveListener);
        return this;
    }

    public MarkerFactory markerClickListener(GoogleMap.OnMarkerClickListener markerClickListener) {
        mMap.setOnMarkerClickListener(markerClickListener);
        return this;
    }

    public void mark(DocumentSnapshot documentSnapshot) {
        mark(documentSnapshot.toObject(Store.class));
    }

    public void mark(Store store) {
        if (mMap == null)
            return;

        LatLng userLatlng = getLastKnownLocation();

        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(store.getLatLng())
                .visible(showCursor));

        if (userLatlng != null)
            store.setDistance(DistanceUtils.metersToString(
                    store.getLatLng(), userLatlng, mContext));
        else
            store.setDistance(null);

        marker.setTag(store);
        markers.add(marker);
    }

    public LatLng getLastKnownLocation() {
        if (mLocationManager == null || mContext == null || mCriteria == null ||
                ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                mContext, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {

            List<String> providers = mLocationManager.getProviders(true);
            Location bestLocation = null;
            for (String provider : providers) {
                Location l = mLocationManager.getLastKnownLocation(provider);
                if (l == null) {
                    continue;
                }
                if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                    // Found best last known location: %s", l);
                    bestLocation = l;
                }
            }
            return (bestLocation != null) ?
                    new LatLng(bestLocation.getLatitude(), bestLocation.getLongitude()) : null;
        } else {
            return null;
        }
    }

    public Context getContext() {
        return mContext;
    }

    public void autoVisible() {
        // TODO : hide by interest rate
        visible(mMap.getCameraPosition().zoom > 12);
    }

    public void visible(boolean visible) {
        if (visible == this.showCursor)
            return;

        showCursor = visible;
        for (Marker marker : markers) {
            marker.setVisible(showCursor);
        }
    }
}
