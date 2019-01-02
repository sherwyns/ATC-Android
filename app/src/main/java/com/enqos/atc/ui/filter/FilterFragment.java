package com.enqos.atc.ui.filter;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.enqos.atc.R;
import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.data.response.CategoryEntity;
import com.enqos.atc.data.response.CategoryResponse;
import com.enqos.atc.listener.StoreActivityListener;
import com.enqos.atc.ui.storeList.ShopListFragment;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class FilterFragment extends Fragment implements FilterView {
    private Unbinder unbinder;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @Inject
    FilterPresenter filterPresenter;
    private StoreActivityListener storeActivityListener;
    private static List<CategoryEntity> categories;

    public FilterFragment() {
        AtcApplication.getAppComponents().inject(this);
    }


    public static FilterFragment getInstance() {
        return new FilterFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categroy, container, false);
        unbinder = ButterKnife.bind(this, view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        if (categories == null || categories.size() == 0)
            filterPresenter.getCategories(this);
        else {
            recyclerView.setAdapter(new FilterAdapter(categories, this));
        }
        //gridView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (storeActivityListener != null)
            storeActivityListener.changeHeader(R.drawable.ic_menu_black_24dp, getString(R.string.filter_by_category), R.drawable.ic_filter_outline);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        storeActivityListener = (StoreActivityListener) context;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onSuccess(CategoryResponse categoryResponse) {
        if (categoryResponse != null) {
            categories = categoryResponse.getCategoryEntities();
            recyclerView.setAdapter(new FilterAdapter(categories, this));
        }

    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void onItemClick(String id) {
        Log.i("ID", id);
        if (storeActivityListener != null)
            storeActivityListener.replaceFragment(ShopListFragment.newInstance(id));
    }
}
