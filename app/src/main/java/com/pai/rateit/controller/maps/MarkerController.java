package com.pai.rateit.controller.maps;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.pai.rateit.factory.marker.MarkerFactory;
import com.pai.rateit.fragment.maps.MapsFragment;
import com.pai.rateit.mapper.store.StoreMapper;
import com.pai.rateit.model.store.Store;
import com.pai.rateit.utils.DistanceUtils;

/**
 * Created by kevin on 03/05/2018.
 */

public class MarkerController implements GoogleMap.OnMarkerClickListener, GoogleMap.OnCameraMoveListener {

    public final static long MAX_QUERY_DISTANCE_MILES = 20;
    public final static double ONE_MILE_LAT = 0.0144927536231884;
    public final static double ONE_MILE_LON = 0.0181818181818182;

    private MarkerFactory mMarkerFactory;
    private MapsFragment.OnFragmentInteractionListener mListener;

    public MarkerController(MarkerFactory markerFactory,
                            MapsFragment.OnFragmentInteractionListener listener) {
        this.mMarkerFactory = markerFactory;
        markerFactory.markerClickListener(this);
        markerFactory.cameraMoveListener(this);

        this.mListener = listener;
    }

    public void findNearby(LatLng pos) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("stores")
                .whereEqualTo(StoreMapper.X, (int) (pos.latitude / (MarkerController.ONE_MILE_LAT
                        * MarkerController.MAX_QUERY_DISTANCE_MILES)))
                .whereEqualTo(StoreMapper.Y, (int) (pos.longitude / (MarkerController.ONE_MILE_LON
                        * MarkerController.MAX_QUERY_DISTANCE_MILES)))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                mMarkerFactory.mark(document);
                            }
                        } else {
                            Log.w("MapsFragment", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Object tag = marker.getTag();
        if (tag instanceof Store) {
            Store store = (Store) tag;

            // Update distance
            LatLng userLatlng = mMarkerFactory.getLastKnownLocation();
            if (userLatlng != null) {
                marker.setTitle(store.getName());
                marker.setSnippet(store.getAddress() + " (" + DistanceUtils.metersToString(
                        store.getLatLng(), userLatlng, mMarkerFactory.getContext()) + ")");
            }

            if (mListener != null)
                return mListener.onStoreMarkerClicked(store);
        }
        return false;
    }

    @Override
    public void onCameraMove() {
        mMarkerFactory.autoVisible();
    }
}
