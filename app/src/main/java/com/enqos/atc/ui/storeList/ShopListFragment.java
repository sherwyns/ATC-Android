package com.enqos.atc.ui.storeList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.enqos.atc.R;
import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.data.response.ProductFavoriteEntity;
import com.enqos.atc.data.response.StoreEntity;
import com.enqos.atc.data.response.StoreResponse;
import com.enqos.atc.listener.StoreActivityListener;
import com.enqos.atc.listener.StoreListener;
import com.enqos.atc.ui.login.LoginActivity;
import com.enqos.atc.ui.shopdetail.ShopDetailFragment;
import com.enqos.atc.ui.shoppage.StorePageFragment;
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

    @Inject
    StoreListPresenter storeListPresenter;
    @Inject
    SharedPreferenceManager sharedPreferenceManager;
    private Unbinder unbinder;
    private ShopListAdapter shopListAdapter;
    private StoreActivityListener listener;
    private List<StoreEntity> allStores;

    public ShopListFragment() {
        AtcApplication.getAppComponents().inject(this);
    }

    public static ShopListFragment newInstance() {
        return new ShopListFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_shope_list, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        storeListPresenter.getStore(this);
        gridView.setOnItemClickListener(this);
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
    public void storeResponse(StoreResponse storeResponse) {
        List<StoreEntity> favs = sharedPreferenceManager.getFavorites();
        if (favs != null) {
            for (StoreEntity store :
                    storeResponse.getData()) {
                for (StoreEntity fav : favs) {
                    if (store.getId().equals(fav.getId()))
                        store.setFavourite(true);
                }
            }
        }
        this.allStores = storeResponse.getData();
        shopListAdapter = new ShopListAdapter(getActivity(), storeResponse.getData());
        shopListAdapter.setListener(this);
        gridView.setAdapter(shopListAdapter);
    }

    @Override
    public void navigateLogin() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSaveStoreFavorite(StoreEntity storeEntity, boolean isFav, int pos) {
        boolean isLogin = (boolean) sharedPreferenceManager.getPreferenceValue(SharedPreferenceManager.BOOLEAN, SharedPreferenceManager.IS_LOGIN);
        if (isLogin) {
            StoreEntity removeEnity = null;
            String id = (String) sharedPreferenceManager.getPreferenceValue(SharedPreferenceManager.STRING, SharedPreferenceManager.USER_ID);
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
            shopListAdapter.notifyDataSetChanged();
        } else {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }

    @Override
    public void onSaveProductFavorite(ProductFavoriteEntity productFavoriteEntity) {

    }

    @Override
    public void onRemoveFav(int index) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (listener != null)
            listener.replaceFragment(StorePageFragment.newInstance(allStores.get(i).getId()));
    }
}
