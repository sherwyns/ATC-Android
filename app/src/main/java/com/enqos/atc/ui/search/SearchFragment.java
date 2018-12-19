package com.enqos.atc.ui.search;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.enqos.atc.R;
import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.data.response.ProductEntity;
import com.enqos.atc.data.response.StoreEntity;
import com.enqos.atc.listener.StoreActivityListener;
import com.enqos.atc.ui.shoppage.StorePageAdapter;
import com.enqos.atc.ui.storeList.ShopListAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SearchFragment extends Fragment implements TextWatcher {

    @BindView(R.id.gridview1)
    GridView gridView1;
    @BindView(R.id.gridview2)
    GridView gridView2;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.rl_store)
    RelativeLayout rlStore;
    @BindView(R.id.rl_product)
    RelativeLayout rlProduct;
    private Unbinder unbinder;
    private StoreActivityListener listener;

    @Inject
    public SearchFragment() {
        AtcApplication.getAppComponents().inject(this);
    }

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_search_layout, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        etSearch.addTextChangedListener(this);
        return rootView;
    }

    /**
     * Set the shop lists
     */
    private void setShopListData() {

        List<StoreEntity> stores = new ArrayList<>();
        for (int i = 0; i < 10; i++) {

            StoreEntity storeEntity = new StoreEntity();
            storeEntity.setFavourite(false);
            storeEntity.setAddress("Melbourne, USA");
            storeEntity.setCity("Melbourne");
            storeEntity.setDescription("Sample Store");
            storeEntity.setShop_name("Shop name :" + i);
            storeEntity.setId(String.valueOf(i));
            storeEntity.setNeighbourhood("Neighbourhood");
            storeEntity.setImage("http://34.209.125.112/images/store_1545034019399.jpg");
            stores.add(storeEntity);

        }
        gridView1.setAdapter(new ShopListAdapter(getActivity(), stores));

    }

    /**
     * Set the shop lists
     */
    private void setProductListData() {

        List<ProductEntity> products = new ArrayList<>();
        for (int i = 0; i < 10; i++) {

            ProductEntity product = new ProductEntity();
            product.setTitle("Product name: " + i);
            product.setFavourite(false);
            product.setId(String.valueOf(i));
            product.setPrice(String.valueOf(i));
            product.setStore_id(String.valueOf(i));
            product.setProduct_image("http://34.209.125.112/images/store_1544881569364.jpeg");
            products.add(product);

        }
        gridView2.setAdapter(new StorePageAdapter(getActivity(), products));

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
            listener.changeHeader(R.drawable.ic_menu_black_24dp, getString(R.string.search), R.drawable.ic_filter_outline);
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
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String searchKey = etSearch.getText().toString();
        if (searchKey.isEmpty()) {
            rlProduct.setVisibility(View.GONE);
            rlStore.setVisibility(View.GONE);
            gridView1.setAdapter(new ShopListAdapter(getActivity(), null));
            gridView2.setAdapter(new StorePageAdapter(getActivity(), null));
        } else {
            if (rlStore.getVisibility() == View.GONE)
                rlStore.setVisibility(View.VISIBLE);
            if (rlProduct.getVisibility() == View.GONE)
                rlProduct.setVisibility(View.VISIBLE);
            setShopListData();
            setProductListData();
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}