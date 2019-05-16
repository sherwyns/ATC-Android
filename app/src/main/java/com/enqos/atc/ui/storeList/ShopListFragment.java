package com.enqos.atc.ui.storeList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.enqos.atc.R;
import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.data.response.NewProductFavResponse;
import com.enqos.atc.data.response.NewStoreFavouriteEntity;
import com.enqos.atc.data.response.ProductEntity;
import com.enqos.atc.data.response.StoreEntity;
import com.enqos.atc.data.response.StorePageResponse;
import com.enqos.atc.data.response.StoreResponse;
import com.enqos.atc.listener.StoreActivityListener;
import com.enqos.atc.listener.StoreListener;
import com.enqos.atc.ui.filter.multifilter.ConstantManager;
import com.enqos.atc.ui.home.HomeActivity;
import com.enqos.atc.ui.productdetail.ProductDetailFragment;
import com.enqos.atc.ui.shoppage.StorePageAdapter;
import com.enqos.atc.ui.shoppage.StorePageFragment;
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

public class ShopListFragment extends Fragment implements StoreListView, StoreListener, AdapterView.OnItemClickListener {
    @BindView(R.id.gridview)
    GridView gridView;
    @BindView(R.id.no_result_layout)
    LinearLayout noResultLayout;
    @BindView(R.id.tv_product)
    TextView tvProduct;
    @BindView(R.id.tv_store)
    TextView tvStore;
    @Inject
    StoreListPresenter storeListPresenter;
    @Inject
    SharedPreferenceManager sharedPreferenceManager;
    private String selectedNeighbourhoods, selectedCategories;
    private Unbinder unbinder;
    private ShopListAdapter shopListAdapter;
    private StorePageAdapter productsAdapter;
    private StoreActivityListener listener;
    private List<StoreEntity> allStores;
    private List<ProductEntity> allProducts = new ArrayList<>();
    private String selecteCategoryId;
    public static boolean isProductSelected = true;
    private int limit = 10;
    private int offset;
    private static boolean isNavigated;
    private boolean isFirstTime = true;
    private boolean isLoading;
    private static final String CATEGORY_ID = "categoryId";

    public ShopListFragment() {
        AtcApplication.getAppComponents().inject(this);
    }

    public static ShopListFragment newInstance(String categoryId) {
        Bundle bundle = new Bundle();
        bundle.putString(CATEGORY_ID, categoryId);
        ShopListFragment shopListFragment = new ShopListFragment();
        shopListFragment.setArguments(bundle);
        return shopListFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_shope_list, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        isNavigated = true;
        setListener();
        if (isProductSelected) {
            tabClick(R.id.tv_product);
            tvProduct.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getActivity()), android.R.color.white));
            tvProduct.setBackgroundResource(R.drawable.gradient_blue);
            tvStore.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getActivity()), R.color.cateoryTextColor));
            tvStore.setBackgroundColor(ContextCompat.getColor(Objects.requireNonNull(getActivity()), android.R.color.transparent));
        } else {
            tabClick(R.id.tv_store);
            if (TextUtils.isEmpty(selecteCategoryId))
                storeListPresenter.getStore(this, selectedNeighbourhoods, selectedCategories, listener.getLatitude(), listener.getLongitude());
            else {
                allStores = StoreListPresenter.groupStores.get(selecteCategoryId);
                if (allStores == null || allStores.size() == 0) {
                    noResultLayout.setVisibility(View.VISIBLE);
                } else {
                    noResultLayout.setVisibility(View.GONE);
                    shopListAdapter = new ShopListAdapter(getActivity(), allStores);
                    shopListAdapter.setListener(this);
                    gridView.setAdapter(shopListAdapter);
                }
            }
        }
        gridView.setOnItemClickListener(this);
        return rootView;
    }

    private void callProdcuts(boolean showLoading) {
        isLoading = true;
        if (showLoading)
            ((StoreListActivity) getActivity()).showLoading();
        storeListPresenter.getProducts(this, selectedNeighbourhoods, selectedCategories, limit, offset);
    }

    private void callStore() {
        ((StoreListActivity) getActivity()).showLoading();
        storeListPresenter.getStore(this, selectedNeighbourhoods, selectedCategories, listener.getLatitude(), listener.getLongitude());
    }

    private void tabClick(int id) {
        switch (id) {
            case R.id.tv_product:
                isProductSelected = true;
                listener.changeHeader(R.drawable.ic_menu_black_24dp, "Products", R.drawable.ic_filter_outline);
                callProdcuts(true);
                tvProduct.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getActivity()), android.R.color.white));
                tvProduct.setBackgroundResource(R.drawable.gradient_blue);
                tvStore.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getActivity()), R.color.cateoryTextColor));
                tvStore.setBackgroundColor(ContextCompat.getColor(Objects.requireNonNull(getActivity()), android.R.color.transparent));
                break;
            case R.id.tv_store:
                isProductSelected = false;
                listener.changeHeader(R.drawable.ic_menu_black_24dp, "Stores", R.drawable.ic_filter_outline);
                callStore();
                tvStore.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getActivity()), android.R.color.white));
                tvStore.setBackgroundResource(R.drawable.gradient_blue);
                tvProduct.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getActivity()), R.color.cateoryTextColor));
                tvProduct.setBackgroundColor(ContextCompat.getColor(Objects.requireNonNull(getActivity()), android.R.color.transparent));
                break;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selecteCategoryId = getArguments().getString(CATEGORY_ID);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (StoreActivityListener) context;
        if (isProductSelected) {
            selectedNeighbourhoods = listener.getNeighbourhoods(ConstantManager.neighbourhoods);
            selectedCategories = listener.getCategories(ConstantManager.categories);
        } else {
            selectedNeighbourhoods = listener.getNeighbourhoods(ConstantManager.storeNeighbours);
            selectedCategories = listener.getCategories(ConstantManager.storeCategories);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (listener != null) {
            if (listener.getLatitude() == 0.0 && listener.getLongitude() == 0.0) {
                if (isProductSelected)
                    listener.changeHeader(R.drawable.ic_menu_black_24dp, getString(R.string.products), R.drawable.ic_filter_outline);
                else
                    listener.changeHeader(R.drawable.ic_menu_black_24dp, getString(R.string.store), R.drawable.ic_filter_outline);
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void showMessage(String message) {
        isLoading = false;
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void storeResponse(StoreResponse
                                      storeResponse, List<NewStoreFavouriteEntity> data) {
        ((StoreListActivity) getActivity()).hideLoading();
        boolean isLogin = (boolean) sharedPreferenceManager.getPreferenceValue(SharedPreferenceManager.BOOLEAN, SharedPreferenceManager.IS_LOGIN);
        if (isLogin) {
            List<StoreEntity> stores = null;
            if (data != null) {
                stores = storeResponse.getData();
                for (StoreEntity store :
                        stores) {
                    for (NewStoreFavouriteEntity fav : data) {
                        if (store.getId().equals(fav.getStore_id()) && fav.getFavorite().equals("1"))
                            store.setFavourite(true);
                    }
                }
            }
            this.allStores = stores;
        } else {
            this.allStores = storeResponse.getData();
        }
        if (allStores == null || allStores.size() == 0) {
            noResultLayout.setVisibility(View.VISIBLE);
        } else {
            noResultLayout.setVisibility(View.GONE);
            shopListAdapter = new ShopListAdapter(getActivity(), allStores);
            shopListAdapter.setListener(this);
            gridView.setAdapter(shopListAdapter);
        }
    }

    @Override
    public void favStoreResponse(List<NewStoreFavouriteEntity> data) {
    }

    @Override
    public void favProductResponse(NewProductFavResponse response) {
    }

    @Override
    public void productsResponse(StorePageResponse storePageResponse) {
        ((StoreListActivity) getActivity()).hideLoading();
        allProducts.addAll(storePageResponse.getData());
        if (allProducts.isEmpty()) {
            isLoading = true;
            showMessage("No products found");
        } else
            isLoading = false;
        productAdapter();
    }

    private void productAdapter() {
        if (productsAdapter == null || isNavigated && gridView != null) {
            productsAdapter = new StorePageAdapter(getActivity(), allProducts);
            gridView.setAdapter(productsAdapter);
        } else
            productsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveStoreFavorite(StoreEntity storeEntity, boolean isFav, int pos) {
        boolean isLogin = (boolean) sharedPreferenceManager.getPreferenceValue(SharedPreferenceManager.BOOLEAN, SharedPreferenceManager.IS_LOGIN);
        if (isLogin) {
            String userId = (String) sharedPreferenceManager.getPreferenceValue(SharedPreferenceManager.STRING, SharedPreferenceManager.USER_ID);
            StoreEntity removeEnity = null;
            List<StoreEntity> fav = sharedPreferenceManager.getFavorites();
            if (fav != null) {
                if (isFav) {
                    storeEntity.setFavourite(true);
                    fav.add(storeEntity);
                } else {
                    for (StoreEntity store :
                            fav) {
                        if (storeEntity.getId().equals(store.getId())) {
                            store.setFavourite(false);
                            removeEnity = store;
                        } else {
                            store.setFavourite(true);
                        }
                    }
                }
                if (removeEnity != null)
                    fav.remove(removeEnity);
                sharedPreferenceManager.saveFavourites(fav);
            } else {
                List<StoreEntity> favorite = new ArrayList<>();
                storeEntity.setFavourite(true);
                favorite.add(storeEntity);
                sharedPreferenceManager.saveFavourites(favorite);
            }
            FavouriteUtility.saveFavourite(userId, storeEntity.getId(), "store", isFav ? "1" : "0");
            shopListAdapter.notifyDataSetChanged();
        } else {
            startActivity(new Intent(getActivity(), HomeActivity.class));
        }
    }

    @Override
    public void onSaveProductFavorite(ProductEntity productFavoriteEntity, boolean isFav, int pos) {
    }

    @Override
    public void onRemoveFav(int index, boolean isStore) {
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @OnClick({R.id.tv_store, R.id.tv_product})
    public void onClick(View v) {
        isNavigated = true;
        noResultLayout.setVisibility(View.GONE);
        switch (v.getId()) {
            case R.id.tv_product:
                if (!isProductSelected) {
                    if (allProducts != null)
                        allProducts.clear();
                    offset = 0;
                    listener.setCount("");
                    selectedNeighbourhoods = listener.getNeighbourhoods(ConstantManager.neighbourhoods);
                    selectedCategories = listener.getCategories(ConstantManager.categories);
                    tabClick(v.getId());
                }
                break;
            case R.id.tv_store:
                if (isProductSelected) {
                    if (allProducts != null)
                        allProducts.clear();
                    listener.changeHeader(R.drawable.ic_menu_black_24dp, "Stores", R.drawable.ic_filter_outline);
                    listener.setCount("");
                    selectedNeighbourhoods = listener.getNeighbourhoods(ConstantManager.storeNeighbours);
                    selectedCategories = listener.getCategories(ConstantManager.storeCategories);
                    tabClick(v.getId());
                }
                break;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setListener() {
        gridView.setOnTouchListener((view, motionEvent) -> {
            isNavigated = false;
            return false;
        });
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (isProductSelected && allProducts.size() > 8) {
                    if (gridView.getLastVisiblePosition() + 1 == totalItemCount && !isLoading && !allProducts.isEmpty()) {
                        isLoading = true;
                        if (!isFirstTime)
                            offset = offset + 10;
                        isFirstTime = false;
                        callProdcuts(false);
                        Log.i("*****", "BOTTOM");
                    } else {
                        isLoading = false;
                    }
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (listener != null) {
            if (isProductSelected) {
                isNavigated = true;
                ProductEntity product = allProducts.get(i);
                ProductDetailFragment productDetailFragment = ProductDetailFragment.newInstance();
                productDetailFragment.productEntity = product;
                listener.replaceFragment(productDetailFragment);
            } else {
                StorePageFragment storePageFragment = StorePageFragment.newInstance();
                storePageFragment.storeEntity = allStores.get(i);
                listener.replaceFragment(storePageFragment);
            }
        }
    }
}
