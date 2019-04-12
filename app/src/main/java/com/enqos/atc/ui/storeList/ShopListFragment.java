package com.enqos.atc.ui.storeList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.enqos.atc.R;
import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.data.response.NewProductFavResponse;
import com.enqos.atc.data.response.NewStoreFavouriteEntity;
import com.enqos.atc.data.response.ProductEntity;
import com.enqos.atc.data.response.StoreEntity;
import com.enqos.atc.data.response.StoreResponse;
import com.enqos.atc.listener.StoreActivityListener;
import com.enqos.atc.listener.StoreListener;
import com.enqos.atc.ui.home.HomeActivity;
import com.enqos.atc.ui.login.LoginActivity;
import com.enqos.atc.ui.shoppage.StorePageFragment;
import com.enqos.atc.utils.FavouriteUtility;
import com.enqos.atc.utils.SharedPreferenceManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ShopListFragment extends Fragment implements StoreListView, StoreListener, AdapterView.OnItemClickListener {


    @BindView(R.id.gridview)
    GridView gridView;
    @BindView(R.id.no_result_layout)
    LinearLayout noResultLayout;
    @Inject
    StoreListPresenter storeListPresenter;
    @Inject
    SharedPreferenceManager sharedPreferenceManager;
    private Unbinder unbinder;
    private ShopListAdapter shopListAdapter;
    private StoreActivityListener listener;
    private List<StoreEntity> allStores;
    private String selecteCategoryId;
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
        if (TextUtils.isEmpty(selecteCategoryId))
            storeListPresenter.getStore(this, listener.getNeighbourhoods(), listener.getCategories(), listener.getLatitude(), listener.getLongitude());
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
        gridView.setOnItemClickListener(this);
        return rootView;
    }

    public void callStoreAPI(){
        storeListPresenter.getStore(this, listener.getNeighbourhoods(), listener.getCategories(), listener.getLatitude(), listener.getLongitude());
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


    }

    @Override
    public void onResume() {
        super.onResume();
        if (listener != null)
            listener.changeHeader(R.drawable.ic_menu_black_24dp, getString(R.string.store), R.drawable.ic_filter_outline);
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

    }

    @Override
    public void storeResponse(StoreResponse storeResponse, List<NewStoreFavouriteEntity> data) {

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
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (listener != null) {
            StorePageFragment storePageFragment = StorePageFragment.newInstance();
            storePageFragment.storeEntity = allStores.get(i);
            listener.replaceFragment(storePageFragment);
        }
    }
}
