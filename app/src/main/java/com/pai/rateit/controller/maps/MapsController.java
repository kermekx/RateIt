package com.pai.rateit.controller.maps;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.pai.rateit.factory.marker.MarkerFactory;
import com.pai.rateit.fragment.maps.MapsFragment;
import com.pai.rateit.utils.DistanceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 02/05/2018.
 */

public class MapsController implements OnMapReadyCallback, GoogleMap.OnCameraIdleListener {

    private Activity mActivity;
    private GoogleMap mMap;
    private LocationManager mLocationManager;
    private MarkerController mMarkerController;

    List<Long> loadedChunk = new ArrayList<>();

    public MapsController(Activity activity, SupportMapFragment mapFragment) {
        mActivity = activity;
        mLocationManager = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMarkerController = new MarkerController(new MarkerFactory().context(mActivity).map(mMap)
                .locationManager(mLocationManager));
        mMap.setOnCameraIdleListener(this);

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
    }

    public void onPermissionGranted() {
        if (checkAnyPermission(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)) {
            mMap.setMyLocationEnabled(true);

            centerMapToLastKnowLocation();
        }
    }

    private void centerMapToLastKnowLocation() {
        Location userPos = getLastKnownLocation();

        if (userPos != null) {
            LatLng pos = new LatLng(userPos.getLatitude(), userPos.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(pos));
            mMap.moveCamera(CameraUpdateFactory.zoomTo(15));

            long chunk = DistanceUtils.getChunkId(pos);
            if (!loadedChunk.contains(chunk)) {
                mMarkerController.findNearby(pos);
                loadedChunk.add(chunk);
            }
        } else {
            centerMapToDefaultLocation();
        }
    }

    private void centerMapToDefaultLocation() {
        LatLng lille = new LatLng(50.6292, 3.0573);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(lille));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));

        long chunk = DistanceUtils.getChunkId(lille);
        if (!loadedChunk.contains(chunk)) {
            mMarkerController.findNearby(lille);
            loadedChunk.add(chunk);
        }
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

    @Override
    public void onCameraIdle() {
        if (mMap.getCameraPosition().zoom > 12)
            return;

        long chunk = DistanceUtils.getChunkId(mMap.getCameraPosition().target);
        if (!loadedChunk.contains(chunk)) {
            mMarkerController.findNearby(mMap.getCameraPosition().target);
            loadedChunk.add(chunk);
        }
    }
}
