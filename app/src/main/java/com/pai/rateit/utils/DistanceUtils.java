package com.pai.rateit.utils;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.pai.rateit.R;

/**
 * Created by kevin on 21/04/2018.
 */

public class DistanceUtils {

    public static float getDistance(Location left, Location right) {
        return left.distanceTo(right);
    }

    public static float getDistance(double latLeft, double lonLeft, double latRight, double lonRight) {
        Location left = new Location("");
        left.setLatitude(latLeft);
        left.setLongitude(lonLeft);

        Location right = new Location("");
        right.setLatitude(latRight);
        right.setLongitude(lonRight);

        return getDistance(left, right);
    }

    public static float getDistance(LatLng left, LatLng right) {
        return getDistance(left.latitude, left.longitude, right.latitude, right.longitude);
    }

    public static String metersToString(float distance, Context context) {
        if (distance < 1000) {
            int dm = Math.round(distance / 10);
            return context.getString(R.string.meter_format, dm * 10);
        } else {
            int km = Math.round(distance / 1000);
            int hm = Math.round((distance - 1000 * km) / 100);
            float dis = km + 0.1f * hm;
            return context.getString(R.string.kilometer_format, dis);
        }
    }

    public static String metersToString(Location left, Location right, Context context) {
        return metersToString(getDistance(left, right), context);
    }

    public static String metersToString(double latLeft, double lonLeft, double latRight, double lonRight, Context context) {
        return metersToString(getDistance(latLeft, lonLeft, latRight, lonRight), context);
    }

    public static String metersToString(LatLng left, LatLng right, Context context) {
        return metersToString(getDistance(left, right), context);
    }
}
