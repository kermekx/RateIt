package com.pai.rateit.mapper.store;

import com.google.firebase.firestore.DocumentSnapshot;
import com.pai.rateit.mapper.Mapper;
import com.pai.rateit.model.store.Store;
import com.pai.rateit.model.user.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kevin on 28/04/2018.
 */

public class UserMapper implements Mapper<User> {

    private static StoreMapper INSTANCE = new StoreMapper();

    private static final String NOTIFY = "notify";

    public static StoreMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public Map<String, Object> serialize(User model) {
        Map<String, Object> data = new HashMap<>();

        data.put(NOTIFY, model.isNotify());

        return data;
    }

    @Override
    public User parse(DocumentSnapshot documentSnapshot) {
        return documentSnapshot.toObject(User.class);
    }
}
