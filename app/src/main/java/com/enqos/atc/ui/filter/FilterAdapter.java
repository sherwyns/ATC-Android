package com.enqos.atc.ui.filter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.enqos.atc.R;
import com.enqos.atc.data.response.CategoryEntity;

import java.util.List;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {
    private List<CategoryEntity> categoryEntities;
    private FilterView filterView;

    FilterAdapter(List<CategoryEntity> categoryEntities, FilterView filterView) {
        this.categoryEntities = categoryEntities;
        this.filterView = filterView;
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

        if (categoryEntity.getName().equalsIgnoreCase("All")) {
            Glide.with(viewHolder.itemView.getContext()).load(R.drawable.ic_done_all_black_24dp)
                    .apply(new RequestOptions().override(30, 30)
                            .error(R.drawable.ic_photo_size_select_actual_black_24dp)
                            .placeholder(R.drawable.ic_photo_size_select_actual_black_24dp)
                            .centerCrop())
                    .into(viewHolder.ivCategory);
        } else {
            Glide.with(viewHolder.itemView.getContext()).load(categoryEntity.getImage_url())
                    .apply(new RequestOptions().override(30, 30)
                            .error(R.drawable.ic_photo_size_select_actual_black_24dp)
                            .placeholder(R.drawable.ic_photo_size_select_actual_black_24dp)
                            .centerCrop())
                    .into(viewHolder.ivCategory);
        }
        viewHolder.itemView.setOnClickListener(view -> filterView.onItemClick(categoryEntity.getId()));
    }

    @Override
    public int getItemCount() {
        return categoryEntities == null ? 0 : categoryEntities.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryName;
        ImageView ivCategory;

        ViewHolder(@NonNull View view) {
            super(view);
            tvCategoryName = view.findViewById(R.id.tv_category);
            ivCategory = view.findViewById(R.id.iv_category);
        }
    }
}
