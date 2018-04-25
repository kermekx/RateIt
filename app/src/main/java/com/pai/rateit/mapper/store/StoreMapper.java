package com.pai.rateit.mapper.store;

import com.google.firebase.firestore.DocumentSnapshot;
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
    private static final String ADDRESS = "address";
    private static final String LAT = "lat";
    private static final String LON = "lon";

    public static StoreMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public Map<String, Object> serialize(Store model) {
        Map<String, Object> data = new HashMap<>();

        data.put(NAME, model.getName());
        data.put(ADDRESS, model.getAddress());
        data.put(LAT, model.getLat());
        data.put(LON, model.getLon());

        return data;
    }

    @Override
    public Store parse(DocumentSnapshot documentSnapshot) {
        return documentSnapshot.toObject(Store.class);
    }
}
