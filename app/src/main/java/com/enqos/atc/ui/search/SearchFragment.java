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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.enqos.atc.R;
import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.data.response.ProductEntity;
import com.enqos.atc.data.response.SearchResponse;
import com.enqos.atc.data.response.StoreEntity;
import com.enqos.atc.listener.StoreActivityListener;
import com.enqos.atc.ui.productdetail.ProductDetailFragment;
import com.enqos.atc.ui.shoppage.StorePageAdapter;
import com.enqos.atc.ui.shoppage.StorePageFragment;
import com.enqos.atc.ui.storeList.ShopListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SearchFragment extends Fragment implements SearchView, AdapterView.OnItemClickListener {

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
    @BindView(R.id.arrow_store)
    ImageView ivStoreArrow;
    @BindView(R.id.arrow_product)
    ImageView ivProductArrow;
    @Inject
    SearchPresenter presenter;
    private Unbinder unbinder;
    private StoreActivityListener listener;
    private Timer timer;
    private List<StoreEntity> stores = new ArrayList<>();
    private List<ProductEntity> products = new ArrayList<>();
    private ShopListAdapter shopAdapter;
    private StorePageAdapter productsAdapter;


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
        etSearch.addTextChangedListener(searchWatcher);
        gridView1.setOnItemClickListener(this);
        gridView2.setOnItemClickListener(this);
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
        if (listener != null) {
            listener.getToolbar().setVisibility(View.GONE);
            listener.changeHeader(R.drawable.ic_menu_black_24dp, getString(R.string.search), R.drawable.ic_filter_outline,true);
        }
        if (!stores.isEmpty() || !products.isEmpty())
            showSearch();
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


    @OnClick({R.id.rl_store, R.id.rl_product})
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.rl_store:
                showMoreStore();
                break;
            case R.id.rl_product:
                showMoreProducts();
                break;
        }
    }

    private TextWatcher searchWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (timer != null)
                timer.cancel();

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String searchKey = etSearch.getText().toString();
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    presenter.getSearch(SearchFragment.this, searchKey);

                }
            }, 1000);


        }
    };

    @Override
    public void onStop() {
        super.onStop();
        if (timer != null)
            timer.cancel();

    }

    private void showMoreStore() {
        if (gridView2.getVisibility() == View.VISIBLE) {
            ivStoreArrow.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
            gridView2.setVisibility(View.GONE);
        } else {
            ivStoreArrow.setImageResource(R.drawable.ic_keyboard_arrow_right_black_24dp);
            gridView2.setVisibility(View.VISIBLE);
        }
    }

    private void showMoreProducts() {
        if (gridView1.getVisibility() == View.VISIBLE) {
            ivProductArrow.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
            gridView1.setVisibility(View.GONE);
        } else {
            ivProductArrow.setImageResource(R.drawable.ic_keyboard_arrow_right_black_24dp);
            gridView1.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onError(String error) {
        rlStore.setVisibility(View.GONE);
        rlProduct.setVisibility(View.GONE);
        stores.clear();
        products.clear();
        if (shopAdapter != null)
            shopAdapter.notifyDataSetChanged();
        if (productsAdapter != null)
            productsAdapter.notifyDataSetChanged();
    }

    @Override
    public void searchResponse(SearchResponse searchResponse) {
        stores.clear();
        products.clear();
        stores.addAll(searchResponse.getData().get(0).getStores());
        products.addAll(searchResponse.getData().get(0).getProducts());

        if (!stores.isEmpty()) {
            if (rlStore.getVisibility() == View.GONE)
                rlStore.setVisibility(View.VISIBLE);
        } else if (rlStore.getVisibility() == View.VISIBLE)
            rlStore.setVisibility(View.GONE);


        if (!products.isEmpty()) {
            if (rlProduct.getVisibility() == View.GONE)
                rlProduct.setVisibility(View.VISIBLE);
        } else if (rlProduct.getVisibility() == View.VISIBLE)
            rlProduct.setVisibility(View.GONE);

        if (shopAdapter == null) {
            shopAdapter = new ShopListAdapter(getActivity(), stores);
            gridView1.setAdapter(shopAdapter);
        } else
            shopAdapter.notifyDataSetChanged();
        if (productsAdapter == null) {
            productsAdapter = new StorePageAdapter(getActivity(), products);
            gridView2.setAdapter(productsAdapter);
        } else
            productsAdapter.notifyDataSetChanged();


    }

    private void showSearch() {

        shopAdapter = new ShopListAdapter(getActivity(), stores);
        gridView1.setAdapter(shopAdapter);

        productsAdapter = new StorePageAdapter(getActivity(), products);
        gridView2.setAdapter(productsAdapter);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (listener != null)
            listener.getToolbar().setVisibility(View.VISIBLE);
        switch (adapterView.getId()) {
            case R.id.gridview1:
                if (listener != null) {
                    StorePageFragment storePageFragment = StorePageFragment.newInstance();
                    storePageFragment.storeEntity = stores.get(i);
                    listener.replaceFragment(storePageFragment);
                }
                break;
            case R.id.gridview2:
                ProductEntity product = products.get(i);
                ProductDetailFragment productDetailFragment = ProductDetailFragment.newInstance();
                productDetailFragment.productEntity = product;
                productDetailFragment.isFromSearch = true;
                listener.replaceFragment(productDetailFragment);
                break;
        }
    }
}