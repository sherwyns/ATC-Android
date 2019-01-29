package com.enqos.atc.ui.shopdetail;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.enqos.atc.R;
import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.data.response.StoreDetailResponse;
import com.enqos.atc.data.response.StoreEntity;
import com.enqos.atc.listener.StoreActivityListener;
import com.enqos.atc.ui.home.HomeActivity;
import com.enqos.atc.ui.shoppage.StorePageFragment;
import com.enqos.atc.utils.FavouriteUtility;
import com.enqos.atc.utils.SharedPreferenceManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.facebook.FacebookSdk.getApplicationContext;

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
    @BindView(R.id.img_fav)
    ImageView ivFav;
    @Inject
    ShopDetailPresenter presenter;
    @Inject
    SharedPreferenceManager sharedPreferenceManager;
    private Unbinder unbinder;
    private StoreActivityListener listener;
    private StoreDetailResponse storeDetailResponse;
    private GoogleMap googleMap;
    private static final String STORE_ID = "storeId";
    private static final String IS_FAVOURITE = "isFavourite";
    private String storeid;
    private boolean isFavourite;

    @Inject
    public ShopDetailFragment() {

        AtcApplication.getAppComponents().inject(this);
    }


    public static ShopDetailFragment newInstance(String storeId, boolean isFav) {
        ShopDetailFragment shopDetailFragment = new ShopDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(STORE_ID, storeId);
        bundle.putBoolean(IS_FAVOURITE, isFav);
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
            storeid = bundle.getString(STORE_ID);
            isFavourite = bundle.getBoolean(IS_FAVOURITE);

        }
    }

    public boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (Objects.requireNonNull(getActivity()).checkSelfPermission(android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted");
                return true;
            } else {

                Log.v("TAG", "Permission is revoked");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                    callPhone();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void callPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + storeDetailResponse.getData().get(0).getPhonenumber()));
        startActivity(intent);
    }

    @OnClick({R.id.tv_view_products, R.id.iv_call, R.id.iv_email, R.id.iv_website, R.id.img_fav})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_fav:
                if (storeDetailResponse != null && !storeDetailResponse.getData().isEmpty())
                    saveStoreFav(storeDetailResponse.getData().get(0));
                break;
            case R.id.tv_view_products:
                if (listener != null) {
                    StorePageFragment storePageFragment = StorePageFragment.newInstance();
                    storePageFragment.storeEntity = storeDetailResponse.getData().get(0);
                    listener.replaceFragment(storePageFragment);
                }
                break;
            case R.id.iv_call:
                if (TextUtils.isEmpty(storeDetailResponse.getData().get(0).getPhonenumber())) {
                    Toast.makeText(getActivity(), "Phone number not available", Toast.LENGTH_LONG).show();
                    return;
                }
                if (isPermissionGranted())
                    callPhone();
                break;
            case R.id.iv_email:
                if (TextUtils.isEmpty(storeDetailResponse.getData().get(0).getContact())) {
                    Toast.makeText(getActivity(), "Email id not available", Toast.LENGTH_LONG).show();
                    return;
                }
                break;
            case R.id.iv_website:
                String url = storeDetailResponse.getData().get(0).getStore_url();
                if (TextUtils.isEmpty(url)) {
                    Toast.makeText(getActivity(), "Store url not available", Toast.LENGTH_LONG).show();
                    return;

                }
                if (!url.startsWith("http://") && !url.startsWith("https://"))
                    url = "http://" + url;
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
                break;
        }
    }

    private void saveStoreFav(StoreEntity storeEntity) {

        boolean isLogin = (boolean) sharedPreferenceManager.getPreferenceValue(SharedPreferenceManager.BOOLEAN, SharedPreferenceManager.IS_LOGIN);
        if (isLogin) {
            String userId = (String) sharedPreferenceManager.getPreferenceValue(SharedPreferenceManager.STRING, SharedPreferenceManager.USER_ID);
            StoreEntity removeEnity = null;
            List<StoreEntity> prodFav = sharedPreferenceManager.getFavorites();
            if (prodFav != null) {
                if (!isFavourite) {
                    ivFav.setImageResource(R.drawable.ic_favorite_black_24dp);
                    storeEntity.setFavourite(true);
                    prodFav.add(storeEntity);
                } else {
                    ivFav.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    for (StoreEntity store :
                            prodFav) {
                        if (storeEntity.getId().equals(store.getId())) {
                            store.setFavourite(false);
                            removeEnity = store;
                        } else {
                            store.setFavourite(true);
                        }
                    }
                }
                if (removeEnity != null)
                    prodFav.remove(removeEnity);

                sharedPreferenceManager.saveFavourites(prodFav);
            } else {
                List<StoreEntity> favorite = new ArrayList<>();
                storeEntity.setFavourite(true);
                favorite.add(storeEntity);
                sharedPreferenceManager.saveFavourites(favorite);
            }
            FavouriteUtility.saveFavourite(userId, storeEntity.getId(), "store", !isFavourite ? "1" : "0");
        } else {
            startActivity(new Intent(getActivity(), HomeActivity.class));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (listener != null)
            listener.changeHeader(R.drawable.ic_keyboard_arrow_left_black_24dp, "Shop Detail", R.drawable.ic_filter_outline);
        if (!TextUtils.isEmpty(storeid))
            presenter.callStoreDetail(this, storeid);
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

        if (isFavourite)
            ivFav.setImageResource(R.drawable.ic_favorite_black_24dp);
        else
            ivFav.setImageResource(R.drawable.ic_favorite_border_black_24dp);

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
