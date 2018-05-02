package com.pai.rateit.controller.maps;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.pai.rateit.factory.marker.MarkerFactory;
import com.pai.rateit.fragment.maps.MapsFragment;

import java.util.List;

/**
 * Created by kevin on 02/05/2018.
 */

public class MapsController implements OnMapReadyCallback, LocationListener {

    private Activity mActivity;
    private GoogleMap mMap;
    private LocationManager mLocationManager;
    private boolean seekingPosition = true;

    public MapsController(Activity activity, SupportMapFragment mapFragment) {
        mActivity = activity;
        mLocationManager = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        askPermissionIfRequired(Manifest.permission.ACCESS_FINE_LOCATION,
                MapsFragment.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

        askPermissionIfRequired(Manifest.permission.ACCESS_COARSE_LOCATION,
                MapsFragment.PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);

        if (checkAnyPermission(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)) {
            onPermissionGranted();
        } else {
            centerMapToDefaultLocation();
        }

        //TODO change to realtime data seeking
        testDrawPoints();
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("LocationChanged", "seeking : " + seekingPosition);
        if (seekingPosition && mMap != null)
            centerMapToLastKnowLocation();
    }

    public void onPermissionGranted() {
        if (checkAnyPermission(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)) {
            mMap.setMyLocationEnabled(true);

            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
                    0, this);

            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                    0, this);

            if (seekingPosition)
                centerMapToLastKnowLocation();
        }
    }

    private void centerMapToLastKnowLocation() {
        Location userPos = getLastKnownLocation();

        if (userPos != null) {
            Log.e("LocationChanged", "move : " + userPos);
            seekingPosition = false;
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(userPos.getLatitude(), userPos.getLongitude())));
            mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        } else {
            centerMapToDefaultLocation();
        }
    }

    private void centerMapToDefaultLocation() {
        LatLng lille = new LatLng(50.6292, 3.0573);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(lille));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
    }

    private Location getLastKnownLocation() {
        if (checkAnyPermission(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)) {
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
            return bestLocation;
        } else {
            return null;
        }
    }

    private void askPermissionIfRequired(String permission, int permissionCode) {
        if (ContextCompat.checkSelfPermission(mActivity, permission)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permission)) {
                // TODO
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                // Add a marker in Sydney and move the camera
            } else {
                ActivityCompat.requestPermissions(mActivity, new String[]{permission},
                        permissionCode);
            }
        }
    }

    private boolean checkAnyPermission(String... permissions) {
        for (String permission : permissions)
            if (ContextCompat.checkSelfPermission(mActivity, permission)
                    == PackageManager.PERMISSION_GRANTED)
                return true;
        return false;
    }

    private void testDrawPoints() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("testData")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            MarkerFactory markerFactory = new MarkerFactory()
                                    .context(mActivity)
                                    .map(mMap)
                                    .locationManager(mLocationManager);
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                markerFactory.mark(document);
                            }
                        } else {
                            Log.w("MapsFragment", "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}
