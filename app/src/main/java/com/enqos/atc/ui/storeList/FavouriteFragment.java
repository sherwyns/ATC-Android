package com.enqos.atc.ui.storeList;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.enqos.atc.R;
import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.data.response.FavoriteResponse;
import com.enqos.atc.data.response.NewProductFavResponse;
import com.enqos.atc.data.response.NewStoreFavouriteEntity;
import com.enqos.atc.data.response.ProductEntity;
import com.enqos.atc.data.response.ProductFavEntity;
import com.enqos.atc.data.response.ProductFavoriteEntity;
import com.enqos.atc.data.response.StoreEntity;
import com.enqos.atc.data.response.StoreFavoriteEntity;
import com.enqos.atc.data.response.StorePageResponse;
import com.enqos.atc.data.response.StoreResponse;
import com.enqos.atc.listener.FavoriteListener;
import com.enqos.atc.listener.StoreActivityListener;
import com.enqos.atc.listener.StoreListener;
import com.enqos.atc.ui.shopdetail.ShopDetailFragment;
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

public class FavouriteFragment extends Fragment implements StoreListener, StoreListView, AdapterView.OnItemClickListener {

    @BindView(R.id.gridview)
    GridView gridView;
    @BindView(R.id.tv_product)
    TextView tvProduct;
    @BindView(R.id.tv_store)
    TextView tvStore;
    private Unbinder unbinder;
    @Inject
    SharedPreferenceManager sharedPreferenceManager;
    private ShopListAdapter adapter;
    private StorePageAdapter productAdapter;
    @Inject
    StoreListPresenter presenter;
    private List<StoreEntity> favourites;
    private List<ProductEntity> productFavourites;
    private StoreActivityListener listener;
    private int selectedTab;

    public FavouriteFragment() {
        AtcApplication.getAppComponents().inject(this);
    }

    public static FavouriteFragment newInstance() {
        return new FavouriteFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_favourite_layout, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        gridView.setOnItemClickListener(this);
        presenter.getProductFavourites("product", this);

        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (StoreActivityListener) context;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (listener != null)
            listener.changeHeader(R.drawable.ic_menu_black_24dp, getString(R.string.favourites), R.drawable.ic_filter_outline);
    }

    @OnClick({R.id.tv_store, R.id.tv_product})
    public void onClick(View view) {
        selectedTab = view.getId();
        switch (view.getId()) {
            case R.id.tv_product:
                tabClick(view.getId());
                break;
            case R.id.tv_store:
                tabClick(view.getId());
                break;
        }
    }

    private void tabClick(int id) {

        switch (id) {
            case R.id.tv_product:
                presenter.getProductFavourites("product", this);

                tvProduct.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getActivity()), android.R.color.white));
                tvProduct.setBackgroundResource(R.drawable.gradient_blue);

                tvStore.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getActivity()), R.color.cateoryTextColor));
                tvStore.setBackgroundColor(ContextCompat.getColor(Objects.requireNonNull(getActivity()), android.R.color.transparent));

                break;
            case R.id.tv_store:
                presenter.getFavourites("store", this);

                tvStore.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getActivity()), android.R.color.white));
                tvStore.setBackgroundResource(R.drawable.gradient_blue);

                tvProduct.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getActivity()), R.color.cateoryTextColor));
                tvProduct.setBackgroundColor(ContextCompat.getColor(Objects.requireNonNull(getActivity()), android.R.color.transparent));
                break;
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
    public void onSaveStoreFavorite(StoreEntity storeFavoriteEntity, boolean isFav, int pos) {

    }

    @Override
    public void onSaveProductFavorite(ProductEntity productFavoriteEntity, boolean isFav, int pos) {

    }

    @Override
    public void onRemoveFav(int index, boolean isStore) {
        if (favourites != null) {
            String userId = (String) sharedPreferenceManager.getPreferenceValue(SharedPreferenceManager.STRING, SharedPreferenceManager.USER_ID);
            if (isStore) {
                FavouriteUtility.saveFavourite(userId, favourites.get(index).getId(), "store", "0");
                favourites.remove(index);
                sharedPreferenceManager.saveFavourites(favourites);
                adapter.notifyDataSetChanged();
            } else {
                FavouriteUtility.saveFavourite(userId, productFavourites.get(index).getId(), "product", "0");
                productFavourites.remove(index);
                sharedPreferenceManager.saveProductFavourites(productFavourites);
                productAdapter.notifyDataSetChanged();
            }
        }
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (listener != null) {
            if (selectedTab == R.id.tv_store) {
                StorePageFragment storePageFragment = StorePageFragment.newInstance();
                storePageFragment.storeEntity = favourites.get(i);
                listener.replaceFragment(storePageFragment);
            }

        }
    }


    private void getFavStore(List<NewStoreFavouriteEntity> data) {
        List<StoreEntity> storeEntities = sharedPreferenceManager.getFavorites();
        if (storeEntities != null) {
            favourites = new ArrayList<>();
            for (StoreEntity store :
                    storeEntities) {
                for (NewStoreFavouriteEntity fav : data) {
                    if (store.getId().equals(fav.getStore_id()) && fav.getFavorite().equals("1")) {
                        store.setFavourite(true);
                        favourites.add(store);
                    }
                }
            }
        }
    }

    private void getFavProdcuts(List<ProductFavEntity> data) {

        List<ProductEntity> productEntities = sharedPreferenceManager.getProductFavorites();
        if (productEntities != null) {
            productFavourites = new ArrayList<>();
            for (ProductEntity product :
                    productEntities) {
                for (ProductFavEntity fav : data) {
                    if (product.getStore_id().equals(fav.getProduct_id()) && fav.getFavorite().equals("1")) {
                        product.setFavourite(true);
                        productFavourites.add(product);
                    }
                }
            }
        }
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void storeResponse(StoreResponse storeResponse, List<NewStoreFavouriteEntity> data) {


    }

    @Override
    public void favStoreResponse(List<NewStoreFavouriteEntity> data) {
        if (data != null)
            getFavStore(data);
        adapter = new ShopListAdapter(getActivity(), favourites);
        gridView.setAdapter(adapter);
        adapter.setListener(this);
    }


    @Override
    public void favProductResponse(NewProductFavResponse response) {
        if (response.getData() != null)
            getFavProdcuts(response.getData());
        productAdapter = new StorePageAdapter(getActivity(), productFavourites);
        gridView.setAdapter(productAdapter);
        productAdapter.setListener(this);
    }

    @Override
    public void productsResponse(StorePageResponse storePageResponse) {

    }
}
