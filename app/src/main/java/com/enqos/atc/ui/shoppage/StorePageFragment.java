package com.enqos.atc.ui.shoppage;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.enqos.atc.R;
import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.data.response.ProductEntity;
import com.enqos.atc.data.response.StorePageResponse;
import com.enqos.atc.listener.StoreActivityListener;
import com.enqos.atc.ui.shopdetail.ShopDetailFragment;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class StorePageFragment extends Fragment implements ShopPageView, AdapterView.OnItemClickListener {
    private static final String STORE_ID = "storeId";
    private String mStoreId;
    @BindView(R.id.gridview)
    GridView gridView;
    @Inject
    ShopPagePresenter presenter;
    private Unbinder unbinder;
    private List<ProductEntity> allProducts;
    StoreActivityListener listener;

    @Inject
    public StorePageFragment() {
        AtcApplication.getAppComponents().inject(this);
    }

    public static StorePageFragment newInstance(String storeId) {
        StorePageFragment fragment = new StorePageFragment();
        Bundle args = new Bundle();
        args.putString(STORE_ID, storeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStoreId = getArguments().getString(STORE_ID);
            presenter.callShopPageDetailApi(this, mStoreId);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (StoreActivityListener) context;
    }

    @OnClick(R.id.card_view)
    public void OnClick(View view) {
        if (listener != null)
            listener.replaceFragment(ShopDetailFragment.newInstance(mStoreId));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_page, container, false);
        unbinder = ButterKnife.bind(this, view);
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

    @Override
    public void onSuccess(StorePageResponse response) {
        if (response != null) {
            allProducts = response.getData();
            StorePageAdapter storePageAdapter = new StorePageAdapter(getActivity(), allProducts);
            gridView.setAdapter(storePageAdapter);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
