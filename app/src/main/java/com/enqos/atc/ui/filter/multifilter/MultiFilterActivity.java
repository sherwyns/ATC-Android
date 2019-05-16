package com.enqos.atc.ui.filter.multifilter;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.enqos.atc.R;
import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.base.BaseActivity;
import com.enqos.atc.data.response.CategoryEntity;
import com.enqos.atc.data.response.CategoryResponse;
import com.enqos.atc.data.response.NeighbourhoodResponse;
import com.enqos.atc.ui.filter.NeibourHoodFilterAdapter;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class MultiFilterActivity extends BaseActivity implements MultiFilterView {
    @BindView(R.id.lv_expandable)
    ExpandableListView expandableListView;
    @BindView(R.id.recycler_view)
    RecyclerView rvNeighbourhoods;
    @BindView(R.id.tv_category)
    TextView tvCategory;
    @BindView(R.id.image_left)
    ImageView ivLeft;
    @BindView(R.id.image_right)
    ImageView ivRight;
    @BindView(R.id.title)
    TextView tvTitle;
    @BindView(R.id.filter_count)
    TextView filterCount;
    @BindView(R.id.tv_neighbourhood)
    TextView tvNeighbourhood;
    @Inject
    MultiFilterPresenter multiFilterPresenter;
    private boolean isNeighbourSelected;
    private NeibourHoodFilterAdapter neighbourhoodAdapter;
    private StoreCategoriesAdapter storeCategoriesAdapter;
    private boolean isProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isProduct = getIntent().getBooleanExtra("isProduct", false);
        showLoading();
        ivLeft.setVisibility(View.GONE);
        filterCount.setVisibility(View.GONE);
        ivRight.setImageResource(R.drawable.ic_close_black_24dp);
        tvTitle.setText(R.string.filters);
        multiFilterPresenter.setFilterView(this);
        setRecyclerView();
        if (isProduct) {
            if (ConstantManager.categories.isEmpty())
                multiFilterPresenter.getProductCategories("0");
            else
                setFinalCategories(ConstantManager.categories);
            expandableListView.setOnGroupClickListener((parent, v, groupPosition, id) -> {
                if (expandableListView.isGroupExpanded(groupPosition))
                    expandableListView.collapseGroup(groupPosition);
                else
                    expandableListView.expandGroup(groupPosition);
                return true;
            });
        } else {
            if (ConstantManager.storeCategories.isEmpty())
                multiFilterPresenter.getCategories();
            else {
                hideLoading();
                rvNeighbourhoods.setVisibility(View.VISIBLE);
                storeCategoriesAdapter = new StoreCategoriesAdapter(ConstantManager.storeCategories);
                rvNeighbourhoods.setAdapter(storeCategoriesAdapter);
            }
        }
    }

    private void setRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvNeighbourhoods.setLayoutManager(layoutManager);
        if (isProduct) {
            if (ConstantManager.neighbourhoods.isEmpty())
                multiFilterPresenter.getNeibhourhoods();
            else {
                neighbourhoodAdapter = new NeibourHoodFilterAdapter(ConstantManager.neighbourhoods, true);
                if (isNeighbourSelected)
                    rvNeighbourhoods.setAdapter(neighbourhoodAdapter);
            }
        } else {
            if (ConstantManager.storeNeighbours.isEmpty())
                multiFilterPresenter.getNeibhourhoods();
            else {
                neighbourhoodAdapter = new NeibourHoodFilterAdapter(ConstantManager.storeNeighbours, false);
                if (isNeighbourSelected)
                    rvNeighbourhoods.setAdapter(neighbourhoodAdapter);
            }
        }
    }

    @Override
    public void injectDependency() {
        AtcApplication.getAppComponents().inject(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_multi_filter;
    }

    @OnClick({R.id.apply, R.id.clear_all, R.id.tv_category, R.id.tv_neighbourhood, R.id.image_right})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_category:
                if (isNeighbourSelected) {
                    tabClick(v.getId());
                }
                break;
            case R.id.tv_neighbourhood:
                if (!isNeighbourSelected)
                    tabClick(v.getId());
                break;
            case R.id.image_right:
                finish();
                break;
            case R.id.apply:
                finish();
                Log.i("******", "CATEGORIES-->" + ConstantManager.categories.size());
                break;
            case R.id.clear_all:
                ConstantManager.categories.clear();
                ConstantManager.neighbourhoods.clear();
                ConstantManager.storeCategories.clear();
                ConstantManager.storeNeighbours.clear();
                finish();
                break;
        }
    }

    @Override
    public void onSuccess(CategoryResponse categoryResponse) {
        if (isProduct)
            multiFilterPresenter.setParentCategories(0, categoryResponse.getCategoryEntities());
        else {
            hideLoading();
            rvNeighbourhoods.setVisibility(View.VISIBLE);
            storeCategoriesAdapter = new StoreCategoriesAdapter(categoryResponse.getCategoryEntities());
            rvNeighbourhoods.setAdapter(storeCategoriesAdapter);
        }
    }

    @Override
    public void onNeighbourHoodSuccess(NeighbourhoodResponse neighbourhoodResponse) {
        neighbourhoodAdapter = new NeibourHoodFilterAdapter(neighbourhoodResponse.getCategoryEntities(), isProduct);
        if (isNeighbourSelected)
            rvNeighbourhoods.setAdapter(neighbourhoodAdapter);
    }

    private void tabClick(int id) {
        switch (id) {
            case R.id.tv_category:
                isNeighbourSelected = false;
                if (isProduct) {
                    expandableListView.setVisibility(View.VISIBLE);
                    rvNeighbourhoods.setVisibility(View.GONE);
                } else {
                    expandableListView.setVisibility(View.GONE);
                    rvNeighbourhoods.setVisibility(View.VISIBLE);
                    if (storeCategoriesAdapter != null)
                        rvNeighbourhoods.setAdapter(storeCategoriesAdapter);
                }
                tvCategory.setTextColor(ContextCompat.getColor(Objects.requireNonNull(this), android.R.color.white));
                tvCategory.setBackgroundResource(R.drawable.gradient_blue);
                tvNeighbourhood.setTextColor(ContextCompat.getColor(Objects.requireNonNull(this), R.color.cateoryTextColor));
                tvNeighbourhood.setBackgroundColor(ContextCompat.getColor(Objects.requireNonNull(this), android.R.color.transparent));
                break;
            case R.id.tv_neighbourhood:
                isNeighbourSelected = true;
                if (neighbourhoodAdapter == null)
                    multiFilterPresenter.getNeibhourhoods();
                else
                    rvNeighbourhoods.setAdapter(neighbourhoodAdapter);
                expandableListView.setVisibility(View.GONE);
                rvNeighbourhoods.setVisibility(View.VISIBLE);
                tvNeighbourhood.setTextColor(ContextCompat.getColor(Objects.requireNonNull(this), android.R.color.white));
                tvNeighbourhood.setBackgroundResource(R.drawable.gradient_blue);
                tvCategory.setTextColor(ContextCompat.getColor(Objects.requireNonNull(this), R.color.cateoryTextColor));
                tvCategory.setBackgroundColor(ContextCompat.getColor(Objects.requireNonNull(this), android.R.color.transparent));
                break;
        }
    }

    @Override
    public void onError(String message) {
        hideLoading();
    }

    @Override
    public void setFinalCategories(List<CategoryEntity> parentCategories) {
        hideLoading();
        MultiFilterAdapter adapter = new MultiFilterAdapter(this, parentCategories);
        expandableListView.setAdapter(adapter);
    }
}
