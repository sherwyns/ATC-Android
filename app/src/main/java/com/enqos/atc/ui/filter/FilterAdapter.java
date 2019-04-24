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
    private List<String> neighbourhoods = new ArrayList<>();
    private List<Integer> categories = new ArrayList<>();
    private boolean isCategory;
    private boolean isBind;

    FilterAdapter(List<CategoryEntity> categoryEntities, FilterView filterView, boolean isCategory) {
        this.categoryEntities = categoryEntities;
        this.filterView = filterView;
        this.isCategory = isCategory;
        checkSelectedFilters();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.filter_row_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    private void checkSelectedFilters() {
        if (isCategory)
            checkSelectedCategoryFilters();
        else
            checkSelectedNeighbourFilters();
    }

    private void checkSelectedCategoryFilters() {
        if (!TextUtils.isEmpty(Constants.CATEGORIES)) {
            String[] allCategories = Constants.CATEGORIES.split(",");
            for (String category : allCategories) {

                for (CategoryEntity categoryEntity : categoryEntities) {
                    if (categoryEntity.getId().trim().equalsIgnoreCase(category.trim())) {
                        categoryEntity.setSelected(true);
                        categories.add(Integer.valueOf(category.trim()));
                    }
                }
            }
        }
    }

    private void checkSelectedNeighbourFilters() {
        if (!TextUtils.isEmpty(Constants.NEIGHBOURHOODS)) {
            String[] categories = Constants.NEIGHBOURHOODS.split(",");
            for (String category : categories) {

                for (CategoryEntity categoryEntity : categoryEntities) {
                    if (categoryEntity.getName().trim().equalsIgnoreCase(category.trim())) {
                        categoryEntity.setSelected(true);
                        neighbourhoods.add(category);
                    }
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
        if (!isCategory) {
            viewHolder.ivCategory.setVisibility(View.GONE);
        } else {
            viewHolder.ivCategory.setVisibility(View.VISIBLE);
        }

        /*if (categoryEntity.getName().equalsIgnoreCase("All")) {
            Glide.with(viewHolder.itemView.getContext()).load(R.drawable.ic_done_all_black_24dp)
                    .apply(new RequestOptions().override(30, 30)
                            .error(R.drawable.ic_photo_size_select_actual_black_24dp)
                            .placeholder(R.drawable.ic_photo_size_select_actual_black_24dp)
                            .centerCrop())
                    .into(viewHolder.ivCategory);
        } else */
        if (!TextUtils.isEmpty(categoryEntity.getImage_url())) {
            viewHolder.ivCategory.setVisibility(View.VISIBLE);
            Glide.with(viewHolder.itemView.getContext()).load(categoryEntity.getImage_url())
                    .apply(new RequestOptions().override(30, 30)
                            .error(R.drawable.ic_photo_size_select_actual_black_24dp)
                            .placeholder(R.drawable.ic_photo_size_select_actual_black_24dp)
                            .centerCrop())
                    .into(viewHolder.ivCategory);
        } else
            viewHolder.ivCategory.setVisibility(View.GONE);

        viewHolder.checkBox.setOnCheckedChangeListener((compoundButton, checked) -> {
            if (checked) {
                if (viewHolder.ivCategory.getVisibility() == View.VISIBLE) {
                    /*if (categoryEntity.getName().equals("All"))
                        selectAllCategories();
                    else*/
                    categories.add(Integer.valueOf(categoryEntity.getId()));
                } else {
                   /* if (categoryEntity.getName().equals("All"))
                        selectAllNeighbourhood();
                    else*/
                    neighbourhoods.add(categoryEntity.getName());
                }
            } else {
                if (viewHolder.ivCategory.getVisibility() == View.VISIBLE) {
                   /* if (categoryEntity.getName().equals("All"))
                        clearAllCategories();
                    else {*/
                    categoryEntity.setSelected(false);
                    /*if (categoryEntities.get(0).isSelected()) {
                        categoryEntities.get(0).setSelected(false);
                        if (!isBind)
                            notifyDataSetChanged();
                    }*/

                    categories.remove(Integer.valueOf(categoryEntity.getId()));
                    // }
                } else {
                    /*if (categoryEntity.getName().equals("All"))
                        clearAllNeighbourhood();
                    else {*/
                    categoryEntity.setSelected(false);
                    /*if (categoryEntities.get(0).isSelected()) {
                        categoryEntities.get(0).setSelected(false);
                        if (!isBind)
                            notifyDataSetChanged();
                    }*/
                    neighbourhoods.remove(categoryEntity.getName());
                    //}
                }

            }

        });

    }

    List<Integer> getCategories() {
        return categories;
    }

    List<String> getNeighbourhoods() {
        return neighbourhoods;
    }


   /* void clearAll(List<CategoryEntity> categoryEntities, List<CategoryEntity> allNeighbourHoods, FilterAdapter adapter1, FilterAdapter adapter2) {
        categories.clear();
        neighbourhoods.clear();
        for (CategoryEntity category :
                categoryEntities) {
            category.setSelected(false);
        }
        for (CategoryEntity category :
                allNeighbourHoods) {
            category.setSelected(false);
        }
        if (!isBind) {
            adapter1.notifyDataSetChanged();
            adapter2.notifyDataSetChanged();
        }
    }
*/

    void clearAllCategories() {
        Constants.CATEGORIES = "";
        categories.clear();
        for (CategoryEntity category :
                categoryEntities) {
            category.setSelected(false);
        }
        if (!isBind)
            notifyDataSetChanged();
    }

    void clearAllNeighbourhood() {
        Constants.NEIGHBOURHOODS = "";
        neighbourhoods.clear();
        for (CategoryEntity category :
                categoryEntities) {
            category.setSelected(false);
        }
        if (!isBind)
            notifyDataSetChanged();
    }

    private void selectAllCategories() {
        for (CategoryEntity category : categoryEntities) {
            category.setSelected(true);
            categories.add(Integer.valueOf(category.getId()));
        }
        if (!isBind)
            notifyDataSetChanged();
    }

    private void selectAllNeighbourhood() {
        for (CategoryEntity category : categoryEntities) {
            category.setSelected(true);
            neighbourhoods.add(category.getName());
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
