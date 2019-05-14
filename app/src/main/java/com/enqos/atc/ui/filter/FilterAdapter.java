package com.enqos.atc.ui.filter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.enqos.atc.R;
import com.enqos.atc.data.response.CategoryEntity;

import java.util.ArrayList;
import java.util.List;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {
    private List<CategoryEntity> categoryEntities;
    public static List<Integer> categories = new ArrayList<>();
    private ParentFiterAdapter parentFiterAdapter;
    private boolean isBind;
    private TextView tvCount;

    FilterAdapter(ParentFiterAdapter parentFiterAdapter) {
        this.parentFiterAdapter = parentFiterAdapter;
    }

    public void setData(List<CategoryEntity> categoryEntities, boolean isAll, TextView tvCount) {
        for (CategoryEntity category :
                categoryEntities) {
            category.setSelected(isAll);
        }
        this.tvCount = tvCount;
        this.categoryEntities = categoryEntities;
        checkSelectedCategoryFilters();
        if (!isBind)
            notifyDataSetChanged();
    }

    void allChecked(String id, List<CategoryEntity> categoryEntities, boolean isChecked) {
        for (CategoryEntity category :
                categoryEntities) {
            category.setSelected(isChecked);
        }
        if (isChecked)
            categories.add(Integer.valueOf(id));
        else
            categories.remove(Integer.valueOf(id));
        this.categoryEntities = categoryEntities;
        if (!isBind)
            notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.filter_row_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    private void checkSelectedCategoryFilters() {
        if (categories != null) {
            for (int category : categories) {
                for (CategoryEntity categoryEntity : categoryEntities) {
                    if (Integer.valueOf(categoryEntity.getId()) == category) {
                        categoryEntity.setSelected(true);
                    }
                }
            }
            parentFiterAdapter.setCategories(categories);
        }
    }

    void clearAllCategories() {
        if (categories != null) {
            categories.clear();
            if (ParentFiterAdapter.categories != null)
                ParentFiterAdapter.categories.clear();
            for (CategoryEntity category :
                    categoryEntities) {
                category.setSelected(false);
            }
            if (!isBind)
                notifyDataSetChanged();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        CategoryEntity categoryEntity = categoryEntities.get(i);
        viewHolder.tvCategoryName.setText(categoryEntity.getName());
        isBind = true;
        viewHolder.checkBox.setChecked(categoryEntity.isSelected());
        isBind = false;
        viewHolder.ivCategory.setVisibility(View.GONE);
        viewHolder.tvCount.setVisibility(View.GONE);
        viewHolder.checkBox.setOnCheckedChangeListener((compoundButton, checked) -> {
            if (checked) {
                categories.add(Integer.valueOf(categoryEntity.getId()));
            } else {
                categoryEntity.setSelected(false);
                categories.remove(Integer.valueOf(categoryEntity.getId()));
            }
            if (categories.isEmpty())
                tvCount.setVisibility(View.GONE);
            else {
                if (categories.size() >= categoryEntities.size())
                    tvCount.setText("All");
                else
                    tvCount.setText(String.valueOf(categories.size()));
                tvCount.setVisibility(View.VISIBLE);
            }
            parentFiterAdapter.setCategories(categories);
        });
    }

    public List<Integer> getCategories() {
        return categories;
    }

    @Override
    public int getItemCount() {
        return categoryEntities == null ? 0 : categoryEntities.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryName;
        ImageView ivCategory;
        TextView tvCount;
        CheckBox checkBox;

        ViewHolder(@NonNull View view) {
            super(view);
            tvCategoryName = view.findViewById(R.id.tv_category);
            ivCategory = view.findViewById(R.id.iv_category);
            tvCount = view.findViewById(R.id.tv_count);
            checkBox = view.findViewById(R.id.check_box);
        }
    }
}
