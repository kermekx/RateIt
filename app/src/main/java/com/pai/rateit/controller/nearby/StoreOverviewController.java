package com.pai.rateit.controller.nearby;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;

/**
 * Created by kevin on 19/05/2018.
 */

public class StoreOverviewController {

    private Activity mActivity;
    private LocationManager mLocationManager;

    public StoreOverviewController(Activity activity) {
        this.mActivity = activity;
        mLocationManager = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
    }
}
