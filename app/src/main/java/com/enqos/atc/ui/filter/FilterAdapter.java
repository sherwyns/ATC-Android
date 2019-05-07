package com.enqos.atc.ui.filter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.enqos.atc.R;
import com.enqos.atc.data.response.CategoryEntity;
import com.enqos.atc.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {
    private List<CategoryEntity> categoryEntities;
    private FilterView filterView;
    private static List<Integer> categories = new ArrayList<>();
    private boolean isBind;

    FilterAdapter(List<CategoryEntity> categoryEntities, FilterView filterView) {
        this.categoryEntities = categoryEntities;
        this.filterView = filterView;
        checkSelectedCategoryFilters();
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
        Glide.with(viewHolder.itemView.getContext()).load(categoryEntity.getImage_url())
                .apply(new RequestOptions().override(30, 30)
                        .error(R.drawable.ic_photo_size_select_actual_black_24dp)
                        .placeholder(R.drawable.ic_photo_size_select_actual_black_24dp)
                        .centerCrop())
                .into(viewHolder.ivCategory);


        viewHolder.checkBox.setOnCheckedChangeListener((compoundButton, checked) -> {
            if (checked) {


                categories.add(Integer.valueOf(categoryEntity.getId()));

            } else {
                categoryEntity.setSelected(false);
                categories.remove(Integer.valueOf(categoryEntity.getId()));

            }

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
        CheckBox checkBox;

        ViewHolder(@NonNull View view) {
            super(view);
            tvCategoryName = view.findViewById(R.id.tv_category);
            ivCategory = view.findViewById(R.id.iv_category);
            checkBox = view.findViewById(R.id.check_box);
        }
    }
}
