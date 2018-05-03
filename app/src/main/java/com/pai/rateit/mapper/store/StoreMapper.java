package com.pai.rateit.mapper.store;

import com.google.firebase.firestore.DocumentSnapshot;
import com.pai.rateit.controller.maps.MarkerController;
import com.pai.rateit.mapper.Mapper;
import com.pai.rateit.model.store.Store;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kevin on 25/04/2018.
 */

public class StoreMapper implements Mapper<Store> {

    private static StoreMapper INSTANCE = new StoreMapper();

    private static final String NAME = "name";
    private static final String SUBTITLE = "subtitle";
    private static final String ADDRESS = "address";
    public static final String LAT = "lat";
    public static final String LON = "lon";
    public static final String X = "x";
    public static final String Y = "y";

    public static StoreMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public Map<String, Object> serialize(Store model) {
        Map<String, Object> data = new HashMap<>();

        data.put(NAME, model.getName());
        if (model.getSubtitle() != null)
            data.put(SUBTITLE, model.getSubtitle());
        if (model.getAddress() != null)
            data.put(ADDRESS, model.getAddress());
        data.put(LAT, model.getLat());
        data.put(LON, model.getLon());

        data.put(X, (int) (model.getLat() / (MarkerController.ONE_MILE_LAT * MarkerController.MAX_QUERY_DISTANCE_MILES)));
        data.put(Y, (int) (model.getLon() / (MarkerController.ONE_MILE_LON * MarkerController.MAX_QUERY_DISTANCE_MILES)));

        return data;
    }

    @Override
    public Store parse(DocumentSnapshot documentSnapshot) {
        return documentSnapshot.toObject(Store.class);
    }
}
