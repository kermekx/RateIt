package com.pai.rateit.factory.marker;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.pai.rateit.model.store.Store;
import com.pai.rateit.utils.DistanceUtils;

/**
 * Created by kevin on 26/04/2018.
 */

public class MarkerFactory implements GoogleMap.OnMarkerClickListener {

    private Context mContext;
    private GoogleMap mMap;
    private LocationManager mLocationManager;
    private Criteria mCriteria;

    public MarkerFactory() {

    }

    public MarkerFactory context(Context context) {
        mContext = context;
        return this;
    }

    public MarkerFactory map(GoogleMap map) {
        mMap = map;
        mMap.setOnMarkerClickListener(this);
        return this;
    }

    public MarkerFactory locationManager(LocationManager locationManager) {
        mLocationManager = locationManager;
        mCriteria = new Criteria();
        return this;
    }

    public void mark(DocumentSnapshot documentSnapshot) {
        mark(documentSnapshot.toObject(Store.class));
    }

    public void mark(Store store) {
        if (mMap == null)
            return;

        LatLng userLatlng = getUserLoc();

        Marker marker;
        if (userLatlng == null)
            marker = mMap.addMarker(new MarkerOptions()
                    .position(store.getLatLng())
                    .title(store.getName())
                    .snippet(store.getAddress()));
        else
            marker = mMap.addMarker(new MarkerOptions()
                    .position(store.getLatLng())
                    .title(store.getName())
                    .snippet(store.getAddress() + " (" + DistanceUtils.metersToString(
                            store.getLatLng(), userLatlng, mContext) + ")"));

        marker.setTag(store);
    }

    private LatLng getUserLoc() {
        if (mLocationManager == null || mContext == null || mCriteria == null ||
                ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED)
            return null;

        Location userPos = mLocationManager.getLastKnownLocation(mLocationManager
                .getBestProvider(mCriteria, false));

        return (userPos != null) ? new LatLng(userPos.getLatitude(), userPos.getLongitude()) : null;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Object tag = marker.getTag();
        if (tag instanceof Store) {
            Store store = (Store) tag;

            // Update distance
            LatLng userLatlng = getUserLoc();
            if (userLatlng != null) {
                marker.setTitle(store.getName());
                marker.setSnippet(store.getAddress() + " (" + DistanceUtils.metersToString(
                        store.getLatLng(), userLatlng, mContext) + ")");
            }
        }
        return false;
    }
}
