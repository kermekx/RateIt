package com.pai.rateit.controller.maps;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.pai.rateit.factory.marker.MarkerFactory;
import com.pai.rateit.mapper.store.StoreMapper;

/**
 * Created by kevin on 03/05/2018.
 */

public class MarkerController {

    public final static long MAX_QUERY_DISTANCE_MILES = 20;
    public final static double ONE_MILE_LAT = 0.0144927536231884;
    public final static double ONE_MILE_LON = 0.0181818181818182;

    private MarkerFactory mMarkerFactory;

    public MarkerController(MarkerFactory markerFactory) {
        this.mMarkerFactory = markerFactory;
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
}
