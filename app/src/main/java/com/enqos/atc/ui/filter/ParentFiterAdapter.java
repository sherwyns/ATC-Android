package com.enqos.atc.ui.filter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.enqos.atc.R;
import com.enqos.atc.data.CreateApiRequest;
import com.enqos.atc.data.response.BaseResponse;
import com.enqos.atc.data.response.CategoryEntity;
import com.enqos.atc.data.response.CategoryResponse;
import com.enqos.atc.data.response.NetworkApiResponse;

import java.util.HashMap;
import java.util.List;

public class ParentFiterAdapter extends RecyclerView.Adapter<ParentFiterAdapter.ViewHolder> implements NetworkApiResponse {

    private final List<CategoryEntity> categoryEntities;
    private FilterAdapter filterAdapter;
    private Context context;
    private ViewHolder viewHolder;
    private String id;
    private HashMap<String, List<CategoryEntity>> allCategories;
    private CreateApiRequest createApiRequest;
    private boolean isBind;

    public ParentFiterAdapter(Context context, List<CategoryEntity> categoryEntities) {
        this.categoryEntities = categoryEntities;
        this.context = context;
        allCategories = new HashMap<>();
        createApiRequest = new CreateApiRequest(this);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.parent_filter_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        CategoryEntity categoryEntity = categoryEntities.get(i);
        viewHolder.tvCategoryName.setText(categoryEntity.getName());
        isBind = true;
        viewHolder.checkBox.setChecked(categoryEntity.isSelected());
        isBind = false;
        viewHolder.checkBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (viewHolder.recyclerView.getVisibility() == View.VISIBLE)
                viewHolder.filterAdapter.allChecked(allCategories.get(categoryEntity.getId()), b);
        });
        viewHolder.ivCategory.setOnClickListener(view -> {
            if (viewHolder.recyclerView.getVisibility() == View.VISIBLE)
                viewHolder.recyclerView.setVisibility(View.GONE);
            else
                viewHolder.recyclerView.setVisibility(View.VISIBLE);
            id = categoryEntity.getId();
            if (allCategories.containsKey(id))
                viewHolder.filterAdapter.setData(allCategories.get(id), viewHolder.checkBox.isChecked());
            else {
                ParentFiterAdapter.this.viewHolder = viewHolder;
                createApiRequest.createProductCategoriesRequest(id);
            }
        });
    }




    @Override
    public int getItemCount() {
        return categoryEntities.size();
    }

    @Override
    public void onSuccess(BaseResponse response) {
        if (response instanceof CategoryResponse) {
            CategoryResponse categoryResponse = (CategoryResponse) response;
            if (categoryResponse.getCategoryEntities().isEmpty())
                viewHolder.ivCategory.setVisibility(View.GONE);
            else
                viewHolder.ivCategory.setVisibility(View.VISIBLE);
            allCategories.put(id, categoryResponse.getCategoryEntities());
            viewHolder.filterAdapter.setData(categoryResponse.getCategoryEntities(), viewHolder.checkBox.isChecked());
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryName;
        ImageView ivCategory;
        CheckBox checkBox;
        RecyclerView recyclerView;
        FilterAdapter filterAdapter;
        TextView tvCount;

        ViewHolder(@NonNull View view) {
            super(view);
            tvCategoryName = view.findViewById(R.id.tv_category);
            ivCategory = view.findViewById(R.id.iv_category);
            tvCount = view.findViewById(R.id.tv_count);
            checkBox = view.findViewById(R.id.check_box);
            recyclerView = view.findViewById(R.id.recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            filterAdapter = new FilterAdapter(ParentFiterAdapter.this);
            recyclerView.setAdapter(filterAdapter);
        }
    }
}
