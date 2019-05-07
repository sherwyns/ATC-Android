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
    private static List<Integer> categories = new ArrayList<>();
    private ParentFiterAdapter parentFiterAdapter;
    private boolean isBind;


    FilterAdapter(ParentFiterAdapter parentFiterAdapter) {
        this.parentFiterAdapter = parentFiterAdapter;
        checkSelectedCategoryFilters();

    }

    public void setData(List<CategoryEntity> categoryEntities, boolean isAll) {
        for (CategoryEntity category :
                categoryEntities) {
            category.setSelected(isAll);
        }
        this.categoryEntities = categoryEntities;
        if (!isBind)
            notifyDataSetChanged();
    }

    void allChecked(List<CategoryEntity> categoryEntities, boolean isChecked) {
        for (CategoryEntity category :
                categoryEntities) {
            category.setSelected(isChecked);
        }
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
        for (int category : categories) {
            for (CategoryEntity categoryEntity : categoryEntities) {
                if (Integer.valueOf(categoryEntity.getId()) == category) {
                    categoryEntity.setSelected(true);
                }
            }

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

        });

        viewHolder.ivCategory.setOnClickListener(view -> {

        });

    }

    List<Integer> getCategories() {
        return categories;
    }


    void clearAllCategories() {
        categories.clear();
        for (CategoryEntity category :
                categoryEntities) {
            category.setSelected(false);
        }
        if (!isBind)
            notifyDataSetChanged();
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
