package com.pai.rateit.fragment.maps;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pai.rateit.R;
import com.pai.rateit.model.store.Store;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class StoreMarkerInfoFragment extends Fragment {

    private static final String ARG_STORE = "store";
    public static String FRAGMENT_TAG = "StoreMarkerInfo";
    @BindView(R.id.text_store_name)
    TextView storeNameTextView;
    @BindView(R.id.text_store_subtitle)
    TextView storeSubtitleTextView;
    @BindView(R.id.text_store_distance)
    TextView storeDistanceTextView;
    private Unbinder unbinder;
    private Store mStore;

    private OnFragmentInteractionListener mListener;

    public StoreMarkerInfoFragment() {
        // Required empty public constructor
    }

    public static StoreMarkerInfoFragment newInstance(Store store) {
        StoreMarkerInfoFragment fragment = new StoreMarkerInfoFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_STORE, store);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStore = (Store) getArguments().getSerializable(ARG_STORE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_overview, container, false);
        unbinder = ButterKnife.bind(this, view);

        if (mStore != null)
            setStore(mStore);

        return view;
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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void setStore(Store store) {
        storeNameTextView.setText(store.getName());

        if (store.getDistance() == null)
            storeDistanceTextView.setVisibility(View.GONE);
        else {
            storeDistanceTextView.setVisibility(View.VISIBLE);
            storeDistanceTextView.setText(" - " + store.getDistance());
        }

        if (store.getSubtitle() == null)
            storeSubtitleTextView.setVisibility(View.GONE);
        else {
            storeSubtitleTextView.setVisibility(View.VISIBLE);
            storeSubtitleTextView.setText(store.getSubtitle());
        }
    }

    public interface OnFragmentInteractionListener {

    }
}
