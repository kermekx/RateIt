package com.pai.rateit.mapper;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Map;

/**
 * Created by kevin on 25/04/2018.
 */

public interface Mapper<M> {

    public Map<String, Object> serialize(M model);
    public M parse(DocumentSnapshot documentSnapshot);

}
