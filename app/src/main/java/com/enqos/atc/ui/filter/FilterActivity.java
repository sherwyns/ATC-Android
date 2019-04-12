package com.enqos.atc.ui.filter;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.enqos.atc.R;
import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.base.BaseActivity;
import com.enqos.atc.data.response.CategoryEntity;
import com.enqos.atc.data.response.CategoryResponse;
import com.enqos.atc.ui.storeList.StoreListActivity;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class FilterActivity extends BaseActivity implements FilterView {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.tv_category)
    TextView tvCategory;
    @BindView(R.id.image_left)
    ImageView ivLeft;
    @BindView(R.id.image_right)
    ImageView ivRight;
    @BindView(R.id.title)
    TextView tvTitle;
    @BindView(R.id.tv_neighbourhood)
    TextView tvNeighbourhhod;
    @Inject
    FilterPresenter filterPresenter;
    private static List<CategoryEntity> categories;
    private List<CategoryEntity> neighbourhoods;
    private boolean isNeighbourSelected;
    private FilterAdapter categoryAdapter, neighbourhoodAdapter;


    public static FilterActivity getInstance() {
        return new FilterActivity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ivLeft.setVisibility(View.GONE);
        ivRight.setImageResource(R.drawable.ic_close_black_24dp);
        tvTitle.setText(R.string.filters);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        if (categories == null || categories.size() == 0) {
            showLoading();
            filterPresenter.getCategories(this);
        } else {
            neighbourhoods = filterPresenter.getNeibhourhoods();
            filterPresenter.addAllOptionInNeighbourhood(neighbourhoods);
            neighbourhoodAdapter = new FilterAdapter(neighbourhoods, this, false);
            categoryAdapter = new FilterAdapter(categories, this, true);
            recyclerView.setAdapter(categoryAdapter);
        }
    }

    @Override
    public void injectDependency() {
        AtcApplication.getAppComponents().inject(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.actiivity_categroy;
    }

    @OnClick({R.id.tv_category, R.id.tv_neighbourhood, R.id.image_right, R.id.clear_all, R.id.apply})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_category:
                tabClick(view.getId());
                break;
            case R.id.tv_neighbourhood:
                tabClick(view.getId());
                break;
            case R.id.image_right:
                finish();
                break;
            case R.id.clear_all:

                if (!isNeighbourSelected) {
                    if (categoryAdapter != null) {
                        categoryAdapter.clearAllCategories();
                    }
                } else {
                    if (neighbourhoodAdapter != null)
                        neighbourhoodAdapter.clearAllNeighbourhood();
                }

                break;
            case R.id.apply:
                String selectedNeighours = "", selectedCategories = "";
                if (neighbourhoodAdapter != null) {
                    List<String> neighbourhoods = neighbourhoodAdapter.getNeighbourhoods();
                    selectedNeighours = neighbourhoods.toString().replace("[", "").replace("]", "");
                    Log.i("NEIGHBOUR", selectedNeighours);
                }
                if (categoryAdapter != null) {
                    List<Integer> categories = categoryAdapter.getCategories();
                    selectedCategories = categories.toString().replace("[", "").replace("]", "");
                    Log.i("CATEGORIES", selectedCategories);
                }

                Intent intent = new Intent(this, StoreListActivity.class);
                intent.putExtra("categories", selectedCategories);
                intent.putExtra("neighbourhoods", selectedNeighours);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }

    }

    @Override
    public void onSuccess(CategoryResponse categoryResponse) {
        hideLoading();
        if (categoryResponse != null) {
            categories = categoryResponse.getCategoryEntities();
            if (categories != null) {
                CategoryEntity allCategory = new CategoryEntity();
                allCategory.setId("0");
                allCategory.setName("All");
                categories.add(0, allCategory);
            }
            neighbourhoods = filterPresenter.getNeibhourhoods();
            filterPresenter.addAllOptionInNeighbourhood(neighbourhoods);
            neighbourhoodAdapter = new FilterAdapter(neighbourhoods, this, false);
            categoryAdapter = new FilterAdapter(categories, this, true);
            recyclerView.setAdapter(categoryAdapter);
        }

    }

    @Override
    public void onError(String message) {
        hideLoading();
    }

    private void tabClick(int id) {

        switch (id) {
            case R.id.tv_category:
                isNeighbourSelected = false;
                if (categoryAdapter != null)
                    recyclerView.setAdapter(categoryAdapter);
                tvCategory.setTextColor(ContextCompat.getColor(Objects.requireNonNull(this), android.R.color.white));
                tvCategory.setBackgroundResource(R.drawable.gradient_blue);

                tvNeighbourhhod.setTextColor(ContextCompat.getColor(Objects.requireNonNull(this), R.color.cateoryTextColor));
                tvNeighbourhhod.setBackgroundColor(ContextCompat.getColor(Objects.requireNonNull(this), android.R.color.transparent));

                break;
            case R.id.tv_neighbourhood:
                isNeighbourSelected = true;
                if (neighbourhoodAdapter != null)
                    recyclerView.setAdapter(neighbourhoodAdapter);
                tvNeighbourhhod.setTextColor(ContextCompat.getColor(Objects.requireNonNull(this), android.R.color.white));
                tvNeighbourhhod.setBackgroundResource(R.drawable.gradient_blue);

                tvCategory.setTextColor(ContextCompat.getColor(Objects.requireNonNull(this), R.color.cateoryTextColor));
                tvCategory.setBackgroundColor(ContextCompat.getColor(Objects.requireNonNull(this), android.R.color.transparent));
                break;
        }
    }
}
