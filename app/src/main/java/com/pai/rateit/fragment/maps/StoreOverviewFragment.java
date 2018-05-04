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


public class StoreOverviewFragment extends Fragment {

    private static final String ARG_STORE = "store";
    public static String FRAGMENT_TAG = "StoreOverview";
    @BindView(R.id.text_store_name)
    TextView storeNameTextView;
    @BindView(R.id.text_store_subtitle)
    TextView storeSubtitleTextView;
    private Unbinder unbinder;
    private Store mStore;

    private OnFragmentInteractionListener mListener;

    public StoreOverviewFragment() {
        // Required empty public constructor
    }

    public static StoreOverviewFragment newInstance(Store store) {
        StoreOverviewFragment fragment = new StoreOverviewFragment();
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
