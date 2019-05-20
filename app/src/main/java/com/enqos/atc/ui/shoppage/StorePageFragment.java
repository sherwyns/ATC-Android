package com.enqos.atc.ui.shoppage;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.enqos.atc.R;
import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.data.CreateApiRequest;
import com.enqos.atc.data.response.BaseResponse;
import com.enqos.atc.data.response.NetworkApiResponse;
import com.enqos.atc.data.response.ProductEntity;
import com.enqos.atc.data.response.StoreEntity;
import com.enqos.atc.data.response.StorePageResponse;
import com.enqos.atc.listener.CategoryItemClickListner;
import com.enqos.atc.listener.StoreActivityListener;
import com.enqos.atc.listener.StoreListener;
import com.enqos.atc.ui.home.HomeActivity;
import com.enqos.atc.ui.productdetail.ProductDetailFragment;
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

public class StorePageFragment extends Fragment implements ShopPageView, StoreListener, AdapterView.OnItemClickListener, CategoryItemClickListner, NetworkApiResponse {
    @BindView(R.id.gridview)
    GridView gridView;
    @BindView(R.id.rv_category)
    RecyclerView rvCategory;
    @BindView(R.id.tv_shop_name)
    TextView tvShopName;
    @BindView(R.id.tv_neighbourhood)
    TextView tvNeighbour;
    @BindView(R.id.product_by_label)
    TextView prodcutLabel;
    @BindView(R.id.img_fav)
    ImageView ivFav;
    @BindView(R.id.img_category)
    ImageView ivCategory;
    @BindView(R.id.ll_content)
    LinearLayout contentLayout;
    @Inject
    ShopPagePresenter presenter;
    @Inject
    SharedPreferenceManager sharedPreferenceManager;
    public StoreEntity storeEntity;
    private Unbinder unbinder;
    private List<ProductEntity> allProducts = new ArrayList<>();
    private StoreActivityListener listener;
    private StorePageAdapter storePageAdapter;


    @Inject
    public StorePageFragment() {
        AtcApplication.getAppComponents().inject(this);
    }

    public static StorePageFragment newInstance() {

        return new StorePageFragment();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (StoreActivityListener) context;
    }

    private void setValues() {
        String storeName;
        if (!TextUtils.isEmpty(storeEntity.getShop_name()))
            storeName = storeEntity.getShop_name();
        else if (!TextUtils.isEmpty(storeEntity.getName()))
            storeName = storeEntity.getName();
        else
            storeName = "Shops";

        listener.changeHeader(R.drawable.ic_keyboard_arrow_left_black_24dp, storeName, R.drawable.ic_filter_outline);
        tvShopName.setText(storeName);
        tvNeighbour.setText(storeEntity.getNeighbourhood());

        if (storeEntity.isFavourite()) {
            ivFav.setImageResource(R.drawable.ic_favorite_black_24dp);
        } else {
            ivFav.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }
        Glide.with(Objects.requireNonNull(getActivity())).load(storeEntity.getCategory().get(0).getImage_url())
                .apply(new RequestOptions()
                        .error(R.drawable.ic_restaurant_menu_black_24dp)
                        .placeholder(R.drawable.ic_restaurant_menu_black_24dp)
                        .centerCrop())
                .into(ivCategory);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (storeEntity != null)
            presenter.callShopPageDetailApi(this, storeEntity.getId());
    }

    @OnClick({R.id.card_view, R.id.img_fav})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.card_view:
                if (listener != null && storeEntity != null)
                    listener.replaceFragment(ShopDetailFragment.newInstance(storeEntity.getId(), storeEntity.isFavourite()));
                break;
            case R.id.img_fav:
                saveStoreFav();
                break;
        }

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_page, container, false);
        unbinder = ButterKnife.bind(this, view);
        gridView.setOnItemClickListener(this);
        if (storeEntity != null) {
            String accessToken = (String) sharedPreferenceManager.getPreferenceValue(SharedPreferenceManager.STRING, SharedPreferenceManager.TOKEN);
            if (!TextUtils.isEmpty(accessToken)) {
                CreateApiRequest createApiRequest = new CreateApiRequest(this);
                createApiRequest.createStoreAnalyticsRequest(accessToken, storeEntity.getId());
            }
            setValues();
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void showMessage(String message) {

    }

    private void setFavourites() {
        List<ProductEntity> favs = sharedPreferenceManager.getProductFavorites();
        if (favs != null) {
            for (ProductEntity store :
                    allProducts) {
                for (ProductEntity fav : favs) {
                    if (store.getId().equals(fav.getId()))
                        store.setFavourite(true);
                }
            }
        }
    }

    @Override
    public void onSuccess(StorePageResponse response) {
        if (response != null) {

            if (response.getData().isEmpty()) {
                prodcutLabel.setVisibility(View.GONE);
            } else {
                prodcutLabel.setVisibility(View.VISIBLE);
                allProducts.clear();
                allProducts.addAll(response.getData());
                if (allProducts != null)
                    setFavourites();
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                rvCategory.setLayoutManager(layoutManager);
                CategoryAdapter categoryAdapter = new CategoryAdapter(getActivity(), presenter.categories);
                categoryAdapter.setListener(this);
                rvCategory.setAdapter(categoryAdapter);
                storePageAdapter = new StorePageAdapter(getActivity(), allProducts);
                storePageAdapter.setListener(this);
                gridView.setAdapter(storePageAdapter);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ProductEntity product = allProducts.get(i);
        ProductDetailFragment productDetailFragment = ProductDetailFragment.newInstance();
        productDetailFragment.productEntity = product;
        productDetailFragment.similiarProducts = allProducts;
        listener.replaceFragment(productDetailFragment);
    }

    @Override
    public void onCategorySelected(String name) {
        allProducts.clear();
        if (storePageAdapter == null) {
            if (name.equalsIgnoreCase("All")) {
                for (String key :
                        presenter.groupProducts.keySet()) {
                    allProducts.addAll(presenter.groupProducts.get(key));
                }
            } else
                allProducts.addAll(presenter.groupProducts.get(name));
            storePageAdapter = new StorePageAdapter(getActivity(), allProducts);
            gridView.setAdapter(storePageAdapter);
        } else {
            if (name.equalsIgnoreCase("All")) {
                for (String key :
                        presenter.groupProducts.keySet()) {
                    allProducts.addAll(presenter.groupProducts.get(key));
                }
            } else
                allProducts.addAll(presenter.groupProducts.get(name));
            storePageAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSaveStoreFavorite(StoreEntity storeEntity, boolean isFav, int pos) {

    }

    private void saveStoreFav() {

        boolean isLogin = (boolean) sharedPreferenceManager.getPreferenceValue(SharedPreferenceManager.BOOLEAN, SharedPreferenceManager.IS_LOGIN);
        if (isLogin) {
            String userId = (String) sharedPreferenceManager.getPreferenceValue(SharedPreferenceManager.STRING, SharedPreferenceManager.USER_ID);
            StoreEntity removeEnity = null;
            List<StoreEntity> prodFav = sharedPreferenceManager.getFavorites();
            if (prodFav != null) {
                if (!storeEntity.isFavourite()) {
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
                FavouriteUtility.saveFavourite(userId, storeEntity.getId(), "store", !storeEntity.isFavourite() ? "1" : "0");
            } else {
                List<StoreEntity> favorite = new ArrayList<>();
                storeEntity.setFavourite(true);
                favorite.add(storeEntity);
                sharedPreferenceManager.saveFavourites(favorite);
            }
        } else {
            startActivity(new Intent(getActivity(), HomeActivity.class));
        }
    }

    @Override
    public void onSaveProductFavorite(ProductEntity productFavoriteEntity, boolean isFav, int pos) {
        boolean isLogin = (boolean) sharedPreferenceManager.getPreferenceValue(SharedPreferenceManager.BOOLEAN, SharedPreferenceManager.IS_LOGIN);
        if (isLogin) {
            String userId = (String) sharedPreferenceManager.getPreferenceValue(SharedPreferenceManager.STRING, SharedPreferenceManager.USER_ID);
            ProductEntity removeEnity = null;
            List<ProductEntity> prodFav = sharedPreferenceManager.getProductFavorites();
            if (prodFav != null) {

                if (isFav) {
                    productFavoriteEntity.setFavourite(true);
                    prodFav.add(productFavoriteEntity);
                } else {
                    for (ProductEntity product :
                            prodFav) {
                        if (productFavoriteEntity.getId().equals(product.getId())) {
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
                productFavoriteEntity.setFavourite(true);
                favorite.add(productFavoriteEntity);
                sharedPreferenceManager.saveProductFavourites(favorite);
            }
            FavouriteUtility.saveFavourite(userId, storeEntity.getId(), "product", isFav ? "1" : "0");
            storePageAdapter.notifyDataSetChanged();

        } else {
            startActivity(new Intent(getActivity(), HomeActivity.class));
        }
    }

    @Override
    public void onRemoveFav(int index, boolean isStore) {

    }

    @Override
    public void onSuccess(BaseResponse response) {

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
