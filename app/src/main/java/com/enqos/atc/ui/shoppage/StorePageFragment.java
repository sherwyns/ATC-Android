package com.enqos.atc.ui.shoppage;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.enqos.atc.R;
import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.data.response.ProductEntity;
import com.enqos.atc.data.response.ProductFavoriteEntity;
import com.enqos.atc.data.response.StoreEntity;
import com.enqos.atc.data.response.StorePageResponse;
import com.enqos.atc.listener.CategoryItemClickListner;
import com.enqos.atc.listener.StoreActivityListener;
import com.enqos.atc.listener.StoreListener;
import com.enqos.atc.ui.home.HomeActivity;
import com.enqos.atc.ui.productdetail.ProductDetailFragment;
import com.enqos.atc.ui.shopdetail.ShopDetailFragment;
import com.enqos.atc.utils.SharedPreferenceManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class StorePageFragment extends Fragment implements ShopPageView, StoreListener, AdapterView.OnItemClickListener, CategoryItemClickListner {
    @BindView(R.id.gridview)
    GridView gridView;
    @BindView(R.id.rv_category)
    RecyclerView rvCategory;
    @BindView(R.id.tv_shop_name)
    TextView tvShopName;
    @BindView(R.id.tv_neighbourhood)
    TextView tvNeighbour;
    @BindView(R.id.img_fav)
    ImageView ivFav;
    @BindView(R.id.error_layout)
    LinearLayout errorLayout;
    @BindView(R.id.ll_content)
    LinearLayout contentLayout;
    @Inject
    ShopPagePresenter presenter;
    @Inject
    SharedPreferenceManager sharedPreferenceManager;
    private static final String STORE_ID = "storeId";
    private static final String SHOP_NAME = "shopName";
    private static final String CATEGORY_NAME = "categoryName";
    private static final String IS_FAV = "isFav";
    private String mStoreId, mShopName, mNeighbourHood;
    private boolean isFav;
    private Unbinder unbinder;
    private List<ProductEntity> allProducts = new ArrayList<>();
    private StoreActivityListener listener;
    private StorePageAdapter storePageAdapter;


    @Inject
    public StorePageFragment() {
        AtcApplication.getAppComponents().inject(this);
    }

    public static StorePageFragment newInstance(String storeId, String shopName, String neighbourHood, boolean isFav) {
        StorePageFragment fragment = new StorePageFragment();
        Bundle args = new Bundle();
        args.putString(STORE_ID, storeId);
        args.putString(SHOP_NAME, shopName);
        args.putString(CATEGORY_NAME, neighbourHood);
        args.putBoolean(IS_FAV, isFav);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            mStoreId = bundle.getString(STORE_ID);
            mShopName = bundle.getString(SHOP_NAME);
            mNeighbourHood = bundle.getString(CATEGORY_NAME);
            isFav = bundle.getBoolean(IS_FAV);

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (StoreActivityListener) context;
    }

    private void setValues() {
        listener.changeHeader(R.drawable.ic_keyboard_arrow_left_black_24dp, mShopName, R.drawable.ic_filter_outline);
        tvShopName.setText(mShopName);
        tvNeighbour.setText(mNeighbourHood);

        if (isFav) {
            Log.i("******", "true");
            ivFav.setImageResource(R.drawable.ic_favorite_black_24dp);
        } else {
            Log.i("******", "false");
            ivFav.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.callShopPageDetailApi(this, mStoreId);
    }

    @OnClick(R.id.card_view)
    public void OnClick(View view) {
        if (listener != null)
            listener.replaceFragment(ShopDetailFragment.newInstance(mStoreId));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_page, container, false);
        unbinder = ButterKnife.bind(this, view);
        gridView.setOnItemClickListener(this);
        setValues();
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
                errorLayout.setVisibility(View.VISIBLE);
                contentLayout.setVisibility(View.GONE);
            } else {

                errorLayout.setVisibility(View.GONE);
                contentLayout.setVisibility(View.VISIBLE);
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
        ProductDetailFragment productDetailFragment = ProductDetailFragment.newInstance(product.getId(), product.getProduct_image(), product.getTitle(), product.getPrice(), "");
        productDetailFragment.similiarProducts = allProducts;
        listener.replaceFragment(productDetailFragment);
    }

    @Override
    public void onCategorySelected(String name) {
        allProducts.clear();
        if (storePageAdapter == null) {
            allProducts.addAll(presenter.groupProducts.get(name));
            storePageAdapter = new StorePageAdapter(getActivity(), allProducts);
            gridView.setAdapter(storePageAdapter);
        } else {
            allProducts.addAll(presenter.groupProducts.get(name));
            storePageAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSaveStoreFavorite(StoreEntity storeEntity, boolean isFav, int pos) {

    }

    @Override
    public void onSaveProductFavorite(ProductEntity productFavoriteEntity, boolean isFav, int pos) {
        boolean isLogin = (boolean) sharedPreferenceManager.getPreferenceValue(SharedPreferenceManager.BOOLEAN, SharedPreferenceManager.IS_LOGIN);
        if (isLogin) {
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
            storePageAdapter.notifyDataSetChanged();

        } else {
            startActivity(new Intent(getActivity(), HomeActivity.class));
        }
    }

    @Override
    public void onRemoveFav(int index,boolean isStore) {

    }
}
