package com.enqos.atc.ui.filter.multifilter;

import android.os.Bundle;
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

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class MultiFilterActivity extends BaseActivity implements MultiFilterView {
    @BindView(R.id.lv_expandable)
    ExpandableListView expandableListView;
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
    private List<CategoryEntity> parentCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showLoading();
        multiFilterPresenter.getProductCategories(this, "0");
        expandableListView.setOnGroupClickListener((parent, v, groupPosition, id) -> {
            if (expandableListView.isGroupExpanded(groupPosition))
                expandableListView.collapseGroup(groupPosition);
            else
                expandableListView.expandGroup(groupPosition);
            return true;
        });
    }

    @Override
    public void injectDependency() {
        AtcApplication.getAppComponents().inject(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_multi_filter;
    }

    @OnClick({R.id.apply, R.id.clear_all})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.apply:
                finish();
                Log.i("******", "CATEGORIES-->" + ConstantManager.parentItems.size());
                break;
            case R.id.clear_all:
                break;
        }
    }

    @Override
    public void onSuccess(CategoryResponse categoryResponse) {
        multiFilterPresenter.setParentCategories(0, categoryResponse.getCategoryEntities());
    }

    @Override
    public void onError(String message) {
        hideLoading();
    }

    @Override
    public void setFinalCategories(List<CategoryEntity> parentCategories) {
        hideLoading();
        this.parentCategories = parentCategories;
        MultiFilterAdapter adapter = new MultiFilterAdapter(this, parentCategories);
        expandableListView.setAdapter(adapter);
    }
}
