package com.enqos.atc.ui.search;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.enqos.atc.listener.RecyclerViewItemClickListner;
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

public class SearchFragment extends Fragment implements SearchView, RecyclerViewItemClickListener {

    @BindView(R.id.nested_scrollview)
    NestedScrollView nestedScrollView;
    @BindView(R.id.recycler_view1)
    RecyclerView recyclerView1;
    @BindView(R.id.recycler_view2)
    RecyclerView recyclerView2;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.tv_product)
    TextView tvProducts;
    @BindView(R.id.tv_store)
    TextView tvStores;
    @Inject
    SearchPresenter presenter;
    private Unbinder unbinder;
    private StoreActivityListener listener;
    private Timer timer;
    private List<StoreEntity> allStores = new ArrayList<>();
    private List<ProductEntity> allProducts = new ArrayList<>();
    private SearchStoreAdapter shopAdapter;
    private SearchProductAdapter productsAdapter;


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
        View rootView = inflater.inflate(R.layout.fragment_search_layout, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        etSearch.addTextChangedListener(searchWatcher);
        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(getActivity(), 2);
        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(getActivity(), 2);
        recyclerView1.setLayoutManager(gridLayoutManager1);
        recyclerView2.setLayoutManager(gridLayoutManager2);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.dimen_10dp);
        recyclerView1.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        recyclerView2.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
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
            listener.changeHeader(R.drawable.ic_menu_black_24dp, getString(R.string.search), R.drawable.ic_filter_outline, true);
        }
        if (!allStores.isEmpty() || !allProducts.isEmpty())
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


    @Override
    public void onError(String error) {
        allStores.clear();
        allStores.clear();
        if (shopAdapter != null)
            shopAdapter.notifyDataSetChanged();
        if (productsAdapter != null)
            productsAdapter.notifyDataSetChanged();
    }

    @Override
    public void searchResponse(SearchResponse searchResponse) {
        allStores.clear();
        allProducts.clear();
        allStores.addAll(searchResponse.getData().get(0).getStores());
        allProducts.addAll(searchResponse.getData().get(0).getProducts());
        if (allStores.isEmpty())
            tvStores.setVisibility(View.GONE);
        else
            tvStores.setVisibility(View.VISIBLE);
        if (allProducts.isEmpty())
            tvProducts.setVisibility(View.GONE);
        else
            tvProducts.setVisibility(View.VISIBLE);
        if (shopAdapter == null) {
            shopAdapter = new SearchStoreAdapter(allStores, this);
            recyclerView1.setAdapter(shopAdapter);
        } else
            shopAdapter.notifyDataSetChanged();
        if (productsAdapter == null) {
            productsAdapter = new SearchProductAdapter(allProducts, this);
            recyclerView2.setAdapter(productsAdapter);
        } else
            productsAdapter.notifyDataSetChanged();

    }

    private void showSearch() {

        shopAdapter = new SearchStoreAdapter(allStores, this);
        recyclerView1.setAdapter(shopAdapter);

        productsAdapter = new SearchProductAdapter(allProducts, this);
        recyclerView2.setAdapter(productsAdapter);

    }


    @Override
    public void onItemClick(Object object) {
        if (listener != null)
            listener.getToolbar().setVisibility(View.VISIBLE);
        if (object instanceof StoreEntity) {
            if (listener != null) {
                StorePageFragment storePageFragment = StorePageFragment.newInstance();
                storePageFragment.storeEntity = (StoreEntity) object;
                listener.replaceFragment(storePageFragment);
            }
        } else if (object instanceof ProductEntity) {
            ProductEntity product = (ProductEntity) object;
            ProductDetailFragment productDetailFragment = ProductDetailFragment.newInstance();
            productDetailFragment.productEntity = product;
            productDetailFragment.isFromSearch = true;
            listener.replaceFragment(productDetailFragment);
        }
    }
}
