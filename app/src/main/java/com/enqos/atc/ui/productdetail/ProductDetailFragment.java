package com.enqos.atc.ui.productdetail;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.enqos.atc.R;
import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.data.CreateApiRequest;
import com.enqos.atc.data.response.BaseResponse;
import com.enqos.atc.data.response.NetworkApiResponse;
import com.enqos.atc.data.response.ProductAnalyticsResponse;
import com.enqos.atc.data.response.ProductEntity;
import com.enqos.atc.listener.RecyclerViewItemClickListner;
import com.enqos.atc.listener.StoreActivityListener;
import com.enqos.atc.ui.home.HomeActivity;
import com.enqos.atc.ui.shopdetail.ShopDetailFragment;
import com.enqos.atc.utils.FavouriteUtility;
import com.enqos.atc.utils.SharedPreferenceManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class ProductDetailFragment extends Fragment implements RecyclerViewItemClickListner, NetworkApiResponse {

    @BindView(R.id.tv_product_name)
    TextView tvProductName;
    @BindView(R.id.tv_product_des)
    TextView tvProductDes;
    @BindView(R.id.iv_product)
    ImageView ivProductImg;
    @BindView(R.id.tv_product_price)
    TextView tvPrice;
    @BindView(R.id.tv_call)
    TextView tvCall;
    @BindView(R.id.tv_shop_name)
    TextView tvShopName;
    @BindView(R.id.img_fav)
    ImageView ivFav;
    @BindView(R.id.rv_products)
    RecyclerView rvProducts;
    @Inject
    SharedPreferenceManager sharedPreferenceManager;
    private StoreActivityListener listener;
    public ProductEntity productEntity;
    public boolean isFromSearch = false;
    public List<ProductEntity> similiarProducts;
    private Unbinder unbinder;


    @Inject
    public ProductDetailFragment() {
        AtcApplication.getAppComponents().inject(this);
    }


    public static ProductDetailFragment newInstance() {

        return new ProductDetailFragment();
    }


    private void setValues() {

        if (isFromSearch) {
            tvShopName.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(productEntity.getShop_name())) {
                tvShopName.setText(productEntity.getShop_name());
            }
        } else
            tvShopName.setVisibility(View.GONE);


        if (!TextUtils.isEmpty(productEntity.getTitle()))
            tvProductName.setText(productEntity.getTitle());
        if (!TextUtils.isEmpty(productEntity.getPrice())) {
            if (productEntity.getPrice().equalsIgnoreCase("0") || productEntity.getPrice().equalsIgnoreCase("0.0")) {
                tvPrice.setVisibility(View.GONE);
                tvCall.setVisibility(View.VISIBLE);
            } else {
                tvPrice.setVisibility(View.VISIBLE);
                tvCall.setVisibility(View.INVISIBLE);
                if (!TextUtils.isEmpty(productEntity.getPrice()) && productEntity.getPrice().contains("."))
                    tvPrice.setText(String.format("$ %s", productEntity.getPrice()));
                else
                    tvPrice.setText(String.format("%s.00", String.format("$ %s", productEntity.getPrice())));
            }
        } else {
            tvPrice.setVisibility(View.GONE);
            tvCall.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(productEntity.getDescription()))
            tvProductDes.setText(productEntity.getDescription());

        if (productEntity.isFavourite())
            ivFav.setImageResource(R.drawable.ic_favorite_black_24dp);
        else
            ivFav.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        String url = "";
        if (TextUtils.isEmpty(productEntity.getProduct_image()) && !TextUtils.isEmpty(productEntity.getImage()))
            url = productEntity.getImage();
        else if (!TextUtils.isEmpty(productEntity.getProduct_image()))
            url = productEntity.getProduct_image();
        Glide.with(Objects.requireNonNull(getActivity())).load(url)
                .apply(new RequestOptions()
                        .error(R.drawable.ic_photo_size_select_actual_black_24dp)
                        .placeholder(R.drawable.ic_photo_size_select_actual_black_24dp)
                        .override(Target.SIZE_ORIGINAL))
                .into(ivProductImg);

        SimiliarProductsAdapter adapter = new SimiliarProductsAdapter(getActivity(), similiarProducts);
        adapter.setListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvProducts.setLayoutManager(layoutManager);
        SnapHelper snapHelper = new LinearSnapHelper();

        snapHelper.attachToRecyclerView(rvProducts);
        rvProducts.setAdapter(adapter);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (productEntity != null) {
            String accessToken = (String) sharedPreferenceManager.getPreferenceValue(SharedPreferenceManager.STRING, SharedPreferenceManager.TOKEN);
            if (!TextUtils.isEmpty(accessToken)) {
                CreateApiRequest createApiRequest = new CreateApiRequest(this);
                createApiRequest.createProductAnalyticsRequest(accessToken, productEntity.getStore_id(), productEntity.getId(), productEntity.getTitle());
            }
            setValues();
        }
        return view;
    }

    @OnClick({R.id.img_fav, R.id.tv_shop_name})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_fav:
                saveProduct();
                break;
            case R.id.tv_shop_name:
                if (!TextUtils.isEmpty(productEntity.getStore_id()))
                    listener.replaceFragment(ShopDetailFragment.newInstance(productEntity.getStore_id(), false));
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (StoreActivityListener) context;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (listener != null && productEntity != null && productEntity.getTitle() != null)
            listener.changeHeader(R.drawable.ic_keyboard_arrow_left_black_24dp, productEntity.getTitle(), R.drawable.ic_filter_outline);
        else if (listener != null)
            listener.changeHeader(R.drawable.ic_keyboard_arrow_left_black_24dp, "Product", R.drawable.ic_filter_outline);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void saveProduct() {

        boolean isLogin = (boolean) sharedPreferenceManager.getPreferenceValue(SharedPreferenceManager.BOOLEAN, SharedPreferenceManager.IS_LOGIN);
        if (isLogin) {
            String userId = (String) sharedPreferenceManager.getPreferenceValue(SharedPreferenceManager.STRING, SharedPreferenceManager.USER_ID);
            ProductEntity removeEnity = null;
            List<ProductEntity> prodFav = sharedPreferenceManager.getProductFavorites();
            if (prodFav != null) {

                if (!productEntity.isFavourite()) {
                    ivFav.setImageResource(R.drawable.ic_favorite_black_24dp);
                    productEntity.setFavourite(true);
                    prodFav.add(productEntity);
                } else {
                    ivFav.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    for (ProductEntity product :
                            prodFav) {
                        if (productEntity.getId().equals(product.getId())) {
                            product.setFavourite(false);
                            removeEnity = product;
                        } else {
                            product.setFavourite(true);
                        }
                    }
                }
                if (removeEnity != null)
                    prodFav.remove(removeEnity);
                sharedPreferenceManager.saveProductFavourites(prodFav);
            } else {
                List<ProductEntity> favorite = new ArrayList<>();
                productEntity.setFavourite(true);
                favorite.add(productEntity);
                sharedPreferenceManager.saveProductFavourites(favorite);
            }

            FavouriteUtility.saveFavourite(userId, productEntity.getId(), "product", !productEntity.isFavourite() ? "1" : "0");
        } else {
            startActivity(new Intent(getActivity(), HomeActivity.class));
        }
    }

    @Override
    public void onItemClick(int pos) {
        ProductEntity productEntity = similiarProducts.get(pos);
        if (!TextUtils.isEmpty(productEntity.getTitle()))
            tvProductName.setText(productEntity.getTitle());


        if (!TextUtils.isEmpty(productEntity.getPrice())) {
            if (productEntity.getPrice().equalsIgnoreCase("0") || productEntity.getPrice().equalsIgnoreCase("0.0")) {
                tvPrice.setVisibility(View.GONE);
                tvCall.setVisibility(View.VISIBLE);
            } else {
                tvPrice.setVisibility(View.VISIBLE);
                tvCall.setVisibility(View.INVISIBLE);
                if (!TextUtils.isEmpty(productEntity.getPrice()) && productEntity.getPrice().contains("."))
                    tvPrice.setText(String.format("$ %s", productEntity.getPrice()));
                else
                    tvPrice.setText(String.format("%s.00", String.format("$ %s", productEntity.getPrice())));
            }
        } else {
            tvPrice.setVisibility(View.GONE);
            tvCall.setVisibility(View.VISIBLE);
        }
        String url = "";
        if (TextUtils.isEmpty(productEntity.getProduct_image()) && !TextUtils.isEmpty(productEntity.getImage()))
            url = productEntity.getImage();
        else if (!TextUtils.isEmpty(productEntity.getProduct_image()))
            url = productEntity.getProduct_image();

        Glide.with(Objects.requireNonNull(getActivity())).load(url)
                .apply(new RequestOptions()
                        .error(R.drawable.ic_photo_size_select_actual_black_24dp)
                        .placeholder(R.drawable.ic_photo_size_select_actual_black_24dp)
                        .override(Target.SIZE_ORIGINAL))
                .into(ivProductImg);
    }

    @Override
    public void onSuccess(BaseResponse response) {
        if (response instanceof ProductAnalyticsResponse) {
            ProductAnalyticsResponse productAnalyticsResponse = (ProductAnalyticsResponse) response;
            Log.i("Analytics", productAnalyticsResponse.getData().getMessage());
        }
    }

    @Override
    public void onFailure(String errorMessage, int requestCode, int statusCode) {

    }

    @Override
    public void onTimeOut(int requestCode) {

    }

    @Override
    public void onNetworkError(int requestCode) {

    }

    @Override
    public void onUnknownError(int requestCode, int statusCode, String errorMessage) {

    }
}
