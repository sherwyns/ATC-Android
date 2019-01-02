package com.enqos.atc.ui.filter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
        if (i == 1)
            viewHolder.ivCategory.setImageResource(R.drawable.ic_card_giftcard_black_24dp);
        else if (i == 3)
            viewHolder.ivCategory.setImageResource(R.drawable.ic_directions_run_black_24dp);
        viewHolder.tvCategoryName.setText(categoryEntity.getName());

        viewHolder.itemView.setOnClickListener(view -> {

            filterView.onItemClick(categoryEntity.getId());
        });
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
