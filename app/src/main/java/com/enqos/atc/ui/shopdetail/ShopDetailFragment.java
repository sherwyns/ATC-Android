package com.enqos.atc.ui.shopdetail;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.enqos.atc.R;
import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.data.response.StoreDetailResponse;
import com.enqos.atc.data.response.StoreEntity;
import com.enqos.atc.listener.StoreActivityListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopDetailFragment extends Fragment implements ShopDetailView, OnMapReadyCallback {


    @BindView(R.id.iv_shop_image)
    ImageView shopImg;
    @BindView(R.id.tv_shop_name)
    TextView tvStoreName;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_about)
    TextView tvAbout;
    @BindView(R.id.tv_opening_time)
    TextView tvOpeningTime;
    @BindView(R.id.error_layout)
    LinearLayout errorLayout;
    @BindView(R.id.scrollview)
    ScrollView scrollView;
    @Inject
    ShopDetailPresenter presenter;
    private Unbinder unbinder;
    private StoreActivityListener listener;
    private StoreDetailResponse storeDetailResponse;
    private GoogleMap googleMap;
    private static final String STORE_ID = "storeId";

    @Inject
    public ShopDetailFragment() {

        AtcApplication.getAppComponents().inject(this);
    }


    public static ShopDetailFragment newInstance(String storeId) {
        ShopDetailFragment shopDetailFragment = new ShopDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(STORE_ID, storeId);
        shopDetailFragment.setArguments(bundle);
        return shopDetailFragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (StoreActivityListener) context;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            String storeid = bundle.getString(STORE_ID);
            if (!TextUtils.isEmpty(storeid))
                presenter.callStoreDetail(this, storeid);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (listener != null)
            listener.changeHeader(R.drawable.ic_keyboard_arrow_left_black_24dp, "Shop Detail", R.drawable.ic_filter_outline);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shop_detail, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map_fragment);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    private void setValues(StoreEntity storeEntity) {
        Glide.with(Objects.requireNonNull(getActivity())).load(storeEntity.getImage())
                .apply(new RequestOptions()
                        .error(R.drawable.ic_photo_size_select_actual_black_24dp)
                        .placeholder(R.drawable.ic_photo_size_select_actual_black_24dp)
                        .centerCrop())
                .into(shopImg);

        if (googleMap != null) {
            googleMap.clear();
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            double lat = Double.valueOf(storeEntity.getLatitude());
            double lng = Double.valueOf(storeEntity.getLongitude());
            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            googleMap.addMarker(markerOptions);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
        }

        tvStoreName.setText(storeEntity.getShop_name());
        tvAddress.setText(storeEntity.getAddress());
        tvAbout.setText(storeEntity.getDescription());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        googleMap.clear();
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        if (storeDetailResponse != null) {
            double lat = Double.valueOf(storeDetailResponse.getData().get(0).getLatitude());
            double lng = Double.valueOf(storeDetailResponse.getData().get(0).getLongitude());
            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            googleMap.addMarker(markerOptions);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
        }


    }

    @Override
    public void showMessage(String message) {
        scrollView.setVisibility(View.GONE);
        errorLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void shopDetailResponse(StoreDetailResponse response) {

        this.storeDetailResponse = response;
        if (storeDetailResponse != null) {
            scrollView.setVisibility(View.VISIBLE);
            errorLayout.setVisibility(View.GONE);
            setValues(response.getData().get(0));
        } else {
            scrollView.setVisibility(View.GONE);
            errorLayout.setVisibility(View.VISIBLE);
        }
    }
}
