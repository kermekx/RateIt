package com.pai.rateit.fragment.maps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.SupportMapFragment;
import com.pai.rateit.R;
import com.pai.rateit.controller.maps.MapsController;
import com.pai.rateit.model.store.Store;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by kevin on 16/04/2018.
 */

public class MapsFragment extends Fragment {

    public static String FRAGMENT_TAG = "MapsFragment";
    public static int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 411;
    public static int PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 422;

    private Unbinder unbinder;
    private OnFragmentInteractionListener mListener;
    private MapsController mMapsController;

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

        mMapsController = new MapsController(getActivity(), mListener,
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));

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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (permissions.length == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED)
                mMapsController.onPermissionGranted();
        } else if (requestCode == PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION) {
            if (permissions.length == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_COARSE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED)
                mMapsController.onPermissionGranted();
        }
    }

    public interface OnFragmentInteractionListener {
        public boolean onStoreMarkerClicked(Store store);
    }

}
