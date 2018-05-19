package com.pai.rateit;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.transition.Fade;
import android.support.transition.Slide;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.pai.rateit.activity.SettingsActivity;
import com.pai.rateit.controller.account.AccountController;
import com.pai.rateit.fragment.maps.MapsFragment;
import com.pai.rateit.fragment.maps.StoreMarkerInfoFragment;
import com.pai.rateit.fragment.nearby.StoreOverviewFragment;
import com.pai.rateit.fragment.nearby.dummy.DummyContent;
import com.pai.rateit.model.store.Store;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MapsFragment.OnFragmentInteractionListener,
        StoreMarkerInfoFragment.OnFragmentInteractionListener,
        StoreOverviewFragment.OnListFragmentInteractionListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        new AccountController(this, null);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            Fragment fragment = StoreOverviewFragment.newInstance(1);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment,
                    StoreOverviewFragment.FRAGMENT_TAG).commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(StoreMarkerInfoFragment.FRAGMENT_TAG);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (fragment != null && fragment.isVisible()) {
            fragmentManager.beginTransaction().hide(fragment).commit();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        } else if (id == R.id.action_maps) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = fragmentManager.findFragmentByTag(MapsFragment.FRAGMENT_TAG);

            if (fragment != null && fragment.isVisible())
                return false;
            else if (fragment != null) {
                fragmentManager.beginTransaction().show(fragment).addToBackStack(null).commit();
                return true;
            } else {
                fragment = MapsFragment.newInstance();

                fragmentManager.beginTransaction().replace(R.id.flContent, fragment,
                        MapsFragment.FRAGMENT_TAG).addToBackStack(null).commit();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onStoreMarkerClicked(Store store) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(StoreMarkerInfoFragment.FRAGMENT_TAG);

        if (fragment != null) {
            if (fragment instanceof StoreMarkerInfoFragment) {
                ((StoreMarkerInfoFragment) fragment).setStore(store);
                fragmentManager.beginTransaction().show(fragment).commit();
                return true;
            }
            return false;
        } else {
            fragment = StoreMarkerInfoFragment.newInstance(store);

            fragment.setEnterTransition(new Slide());
            fragment.setExitTransition(new Fade());

            fragmentManager.beginTransaction().replace(R.id.flBottomContent, fragment,
                    StoreMarkerInfoFragment.FRAGMENT_TAG).commit();
            return true;
        }
    }

    @Override
    public void onMapClicked() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment fragment = fragmentManager.findFragmentByTag(StoreMarkerInfoFragment.FRAGMENT_TAG);
        if (fragment != null && fragment.isVisible()) {
            fragmentManager.beginTransaction().hide(fragment).commit();
        }
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }
}
