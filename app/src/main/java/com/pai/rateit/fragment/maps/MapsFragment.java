package com.pai.rateit.fragment.maps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.pai.rateit.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * Created by kevin on 16/04/2018.
 */

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    public static String FRAGMENT_TAG = "MapsFragment";
    public static int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 411;

    SupportMapFragment mapFragment;
    private Unbinder unbinder;
    private OnFragmentInteractionListener mListener;
    private GoogleMap mMap;

    public MapsFragment() {
        // Required empty public constructor
    }

    public static MapsFragment newInstance() {
        return new MapsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_maps, container,
                false);
        unbinder = ButterKnife.bind(this, view);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return false;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            centerMapToCurrentLocation();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                // Add a marker in Sydney and move the camera
                LatLng lille = new LatLng(51, 3);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(lille));
                mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (permissions.length == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                    centerMapToCurrentLocation();
                }
            } else {
                // Permission was denied. Display an error message.
                // Add a marker in Sydney and move the camera
                LatLng lille = new LatLng(51, 3);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(lille));
                mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
            }
        }
    }

    @RequiresPermission(anyOf = {ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION})
    private void centerMapToCurrentLocation() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager)
                    getActivity().getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();


            Location userPos = locationManager.getLastKnownLocation(locationManager
                    .getBestProvider(criteria, false));

            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(userPos.getLatitude(), userPos.getLongitude())));
            mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        }
    }

    public interface OnFragmentInteractionListener {

    }

}
