package com.enqos.atc.ui.storeList;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.enqos.atc.R;
import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.data.response.FavoriteResponse;
import com.enqos.atc.data.response.ProductFavoriteEntity;
import com.enqos.atc.data.response.StoreEntity;
import com.enqos.atc.data.response.StoreFavoriteEntity;
import com.enqos.atc.listener.FavoriteListener;
import com.enqos.atc.listener.StoreActivityListener;
import com.enqos.atc.listener.StoreListener;
import com.enqos.atc.ui.shopdetail.ShopDetailFragment;
import com.enqos.atc.utils.SharedPreferenceManager;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FavouriteFragment extends Fragment implements StoreListener, AdapterView.OnItemClickListener {

    @BindView(R.id.gridview)
    GridView gridView;
    private Unbinder unbinder;
    @Inject
    SharedPreferenceManager sharedPreferenceManager;
    private ShopListAdapter adapter;
    @Inject
    StoreListPresenter presenter;
    private List<StoreEntity> favourites;
    private StoreActivityListener listener;

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
        favourites = sharedPreferenceManager.getFavorites();
        gridView.setOnItemClickListener(this);
        if (favourites != null) {
            adapter = new ShopListAdapter(getActivity(), favourites);
            gridView.setAdapter(adapter);
            adapter.setListener(this);
        }
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
        String id = (String) sharedPreferenceManager.getPreferenceValue(SharedPreferenceManager.STRING, SharedPreferenceManager.USER_ID);

        //presenter.saveFavorite(id, storeFavoriteEntity);
    }

    @Override
    public void onSaveProductFavorite(ProductFavoriteEntity productFavoriteEntity) {

    }

    @Override
    public void onRemoveFav(int index) {
        if (favourites != null) {
            favourites.remove(index);
            sharedPreferenceManager.saveFavourites(favourites);
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (listener != null)
            listener.replaceFragment(ShopDetailFragment.newInstance());
    }
}
