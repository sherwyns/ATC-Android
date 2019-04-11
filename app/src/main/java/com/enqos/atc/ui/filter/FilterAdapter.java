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

import java.util.ArrayList;
import java.util.List;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {
    private List<CategoryEntity> categoryEntities;
    private FilterView filterView;
    private List<String> neighbourhoods = new ArrayList<>();
    private List<Integer> categories = new ArrayList<>();
    private boolean isCategory;

    FilterAdapter(List<CategoryEntity> categoryEntities, FilterView filterView, boolean isCategory) {
        this.categoryEntities = categoryEntities;
        this.filterView = filterView;
        this.isCategory = isCategory;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.filter_row_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        CategoryEntity categoryEntity = categoryEntities.get(i);
        viewHolder.tvCategoryName.setText(categoryEntity.getName());
        if (isCategory) {
            if (categories.isEmpty())
                viewHolder.checkBox.setChecked(false);
            viewHolder.ivCategory.setVisibility(View.GONE);
        } else {
            if (neighbourhoods.isEmpty())
                viewHolder.checkBox.setChecked(false);
            viewHolder.ivCategory.setVisibility(View.VISIBLE);
        }

        if (categoryEntity.getName().equalsIgnoreCase("All")) {
            Glide.with(viewHolder.itemView.getContext()).load(R.drawable.ic_done_all_black_24dp)
                    .apply(new RequestOptions().override(30, 30)
                            .error(R.drawable.ic_photo_size_select_actual_black_24dp)
                            .placeholder(R.drawable.ic_photo_size_select_actual_black_24dp)
                            .centerCrop())
                    .into(viewHolder.ivCategory);
        } else if (!TextUtils.isEmpty(categoryEntity.getImage_url())) {
            Glide.with(viewHolder.itemView.getContext()).load(categoryEntity.getImage_url())
                    .apply(new RequestOptions().override(30, 30)
                            .error(R.drawable.ic_photo_size_select_actual_black_24dp)
                            .placeholder(R.drawable.ic_photo_size_select_actual_black_24dp)
                            .centerCrop())
                    .into(viewHolder.ivCategory);
        }

        viewHolder.checkBox.setOnCheckedChangeListener((compoundButton, checked) -> {
            if (checked) {
                if (viewHolder.ivCategory.getVisibility() == View.VISIBLE)
                    categories.add(Integer.valueOf(categoryEntity.getId()));
                else
                    neighbourhoods.add(categoryEntity.getName());
            } else {
                if (viewHolder.ivCategory.getVisibility() == View.VISIBLE)
                    categories.remove(Integer.valueOf(categoryEntity.getId()));
                else
                    neighbourhoods.remove(categoryEntity.getName());
            }

        });

    }

    List<Integer> getCategories() {
        return categories;
    }

    List<String> getNeighbourhoods() {
        return neighbourhoods;
    }


    void clearAll() {
        categories.clear();
        neighbourhoods.clear();
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
