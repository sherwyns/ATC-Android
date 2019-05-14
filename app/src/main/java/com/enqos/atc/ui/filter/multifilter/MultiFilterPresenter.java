package com.enqos.atc.ui.filter.multifilter;

import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.base.BasePresenter;
import com.enqos.atc.data.CreateApiRequest;
import com.enqos.atc.data.response.BaseResponse;
import com.enqos.atc.data.response.CategoryEntity;
import com.enqos.atc.data.response.CategoryResponse;
import com.enqos.atc.data.response.NetworkApiResponse;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MultiFilterPresenter extends BasePresenter implements NetworkApiResponse {
    private MultiFilterView filterView;
    private CreateApiRequest createApiRequest;
    private CategoryEntity parentCategory;
    private int size = 0;
    private List<CategoryEntity> parentCategories;
    private List<CategoryEntity> finalParentCategories = new ArrayList<>();

    @Inject
    MultiFilterPresenter() {
        AtcApplication.getAppComponents().inject(this);
    }

    void getProductCategories(MultiFilterView filterView, String id) {
        this.filterView = filterView;
        try {
            if (createApiRequest == null)
                createApiRequest = new CreateApiRequest(this);
            createApiRequest.createProductCategoriesRequest(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getProductCategories(String id) {
        try {
            if (createApiRequest == null)
                createApiRequest = new CreateApiRequest(this);
            createApiRequest.createProductCategoriesRequest(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setParentCategories(int position, List<CategoryEntity> parentCategories) {
        this.parentCategory = parentCategories.get(position);
        getProductCategories(parentCategory.getId());
    }

    @Override
    public void onSuccess(BaseResponse response) {
        if (response instanceof CategoryResponse) {
            CategoryResponse categoryResponse = (CategoryResponse) response;
            if (parentCategory == null) {
                parentCategories = categoryResponse.getCategoryEntities();
                filterView.onSuccess(categoryResponse);
            } else {
                parentCategory.setAllChildren(categoryResponse.getCategoryEntities());
                finalParentCategories.add(parentCategory);
                if (size >= parentCategories.size() - 1) {
                    filterView.setFinalCategories(finalParentCategories);
                } else {
                    size += 1;
                    setParentCategories(size, parentCategories);
                }
            }
        }
    }

    @Override
    public void onFailure(String errorMessage, int requestCode, int statusCode) {
    }

    @Override
    public void onTimeOut(int requestCode) {
    }

    @Override
    public void onNetworkError(int requestCode) {
    }

    @Override
    public void onUnknownError(int requestCode, int statusCode, String errorMessage) {
    }
}
