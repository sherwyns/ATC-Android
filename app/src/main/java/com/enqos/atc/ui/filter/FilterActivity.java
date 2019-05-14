package com.enqos.atc.ui.filter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.enqos.atc.R;
import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.base.BaseActivity;
import com.enqos.atc.data.response.CategoryEntity;
import com.enqos.atc.data.response.CategoryResponse;
import com.enqos.atc.data.response.Neighbourhood;
import com.enqos.atc.data.response.NeighbourhoodResponse;
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
    @BindView(R.id.filter_count)
    TextView filterCount;
    @BindView(R.id.tv_neighbourhood)
    TextView tvNeighbourhood;
    @BindView(R.id.top_layout)
    LinearLayout topLayout;
    @Inject
    FilterPresenter filterPresenter;
    public static List<CategoryEntity> categories;
    private List<Neighbourhood> neighbourhoods;
    private boolean isNeighbourSelected;
    private ParentFiterAdapter categoryAdapter;
    private NeibourHoodFilterAdapter neighbourhoodAdapter;

    public static FilterActivity getInstance() {
        return new FilterActivity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isProduct = getIntent().getBooleanExtra("isProduct", false);
        ivLeft.setVisibility(View.GONE);
        filterCount.setVisibility(View.GONE);
        ivRight.setImageResource(R.drawable.ic_close_black_24dp);
        tvTitle.setText(R.string.filters);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        showLoading();
        filterPresenter.getNeibhourhoods();
        if (isProduct)
            filterPresenter.getProductCategories(this);
        else
            filterPresenter.getCategories(this);

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
                if (categoryAdapter != null)
                    categoryAdapter.clearAllCategories();
                if (neighbourhoodAdapter != null)
                    neighbourhoodAdapter.clearAllNeighbourhood();
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
                    if (categories != null) {
                        selectedCategories = categories.toString().replace("[", "").replace("]", "");
                        Log.i("CATEGORIES", selectedCategories);
                    }
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
            if (!categoryResponse.getCategoryEntities().isEmpty()) {
                categories = categoryResponse.getCategoryEntities();
                categoryAdapter = new ParentFiterAdapter(this, categories);
                recyclerView.setAdapter(categoryAdapter);
            }
        }
    }

    @Override
    public void onError(String message) {
        hideLoading();
    }

    @Override
    public void onSuccess(NeighbourhoodResponse neighbourhoodResponse) {

        neighbourhoods = neighbourhoodResponse.getCategoryEntities();
        neighbourhoodAdapter = new NeibourHoodFilterAdapter(neighbourhoods, this);

    }

    private void tabClick(int id) {
        switch (id) {
            case R.id.tv_category:
                isNeighbourSelected = false;
                categoryAdapter = new ParentFiterAdapter(this, categories);
                recyclerView.setAdapter(categoryAdapter);
                tvCategory.setTextColor(ContextCompat.getColor(Objects.requireNonNull(this), android.R.color.white));
                tvCategory.setBackgroundResource(R.drawable.gradient_blue);
                tvNeighbourhood.setTextColor(ContextCompat.getColor(Objects.requireNonNull(this), R.color.cateoryTextColor));
                tvNeighbourhood.setBackgroundColor(ContextCompat.getColor(Objects.requireNonNull(this), android.R.color.transparent));
                break;
            case R.id.tv_neighbourhood:

                isNeighbourSelected = true;

                neighbourhoodAdapter = new NeibourHoodFilterAdapter(neighbourhoods, this);
                recyclerView.setAdapter(neighbourhoodAdapter);

                tvNeighbourhood.setTextColor(ContextCompat.getColor(Objects.requireNonNull(this), android.R.color.white));
                tvNeighbourhood.setBackgroundResource(R.drawable.gradient_blue);
                tvCategory.setTextColor(ContextCompat.getColor(Objects.requireNonNull(this), R.color.cateoryTextColor));
                tvCategory.setBackgroundColor(ContextCompat.getColor(Objects.requireNonNull(this), android.R.color.transparent));
                break;
        }
    }
}
