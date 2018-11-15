package com.enqos.atc.storeList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.enqos.atc.R;
import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.data.response.ProductFavoriteEntity;
import com.enqos.atc.data.response.StoreFavoriteEntity;
import com.enqos.atc.data.response.StoreResponse;
import com.enqos.atc.listener.StoreListener;
import com.enqos.atc.login.LoginActivity;
import com.enqos.atc.utils.SharedPreferenceManager;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ShopListFragment extends Fragment implements StoreListView, StoreListener {


    @BindView(R.id.gridview)
    GridView gridView;

    @Inject
    StoreListPresenter storeListPresenter;
    @Inject
    SharedPreferenceManager sharedPreferenceManager;
    private Unbinder unbinder;

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

        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

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
        ShopListAdapter shopListAdapter = new ShopListAdapter(getActivity(), storeResponse.getData());
        shopListAdapter.setListener(this);
        gridView.setAdapter(shopListAdapter);
    }

    @Override
    public void navigateLogin() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSaveStoreFavorite(StoreFavoriteEntity storeFavoriteEntity) {
        String id = (String) sharedPreferenceManager.getPreferenceValue(SharedPreferenceManager.STRING, SharedPreferenceManager.USER_ID);
        storeListPresenter.saveFavorite(id, storeFavoriteEntity);
    }

    @Override
    public void onSaveProductFavorite(ProductFavoriteEntity productFavoriteEntity) {

    }
}
