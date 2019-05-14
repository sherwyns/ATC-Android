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
import com.enqos.atc.data.response.Neighbourhood;
import com.enqos.atc.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class NeibourHoodFilterAdapter extends RecyclerView.Adapter<NeibourHoodFilterAdapter.ViewHolder> {
    private List<Neighbourhood> categoryEntities;
    private FilterView filterView;
    private static List<String> neighbourhoods = new ArrayList<>();
    private boolean isBind;

    NeibourHoodFilterAdapter(List<Neighbourhood> categoryEntities, FilterView filterView) {
        this.categoryEntities = categoryEntities;
        this.filterView = filterView;
        checkSelectedNeighbourFilters();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.filter_row_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    private void checkSelectedNeighbourFilters() {
        for (String category : neighbourhoods) {
            for (Neighbourhood categoryEntity : categoryEntities) {
                if (categoryEntity.getNeighbourhood().equalsIgnoreCase(category)) {
                    categoryEntity.setSelected(true);
                }
            }
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Neighbourhood categoryEntity = categoryEntities.get(i);
        viewHolder.tvCategoryName.setText(categoryEntity.getNeighbourhood());
        isBind = true;
        viewHolder.checkBox.setChecked(categoryEntity.isSelected());
        isBind = false;
        viewHolder.checkBox.setOnCheckedChangeListener((compoundButton, checked) -> {
            if (checked) {
                neighbourhoods.add(categoryEntity.getNeighbourhood());
            } else {
                categoryEntity.setSelected(false);
                neighbourhoods.remove(categoryEntity.getNeighbourhood());
            }

        });

    }


    List<String> getNeighbourhoods() {
        return neighbourhoods;
    }

    void clearAllNeighbourhood() {
        neighbourhoods.clear();
        for (Neighbourhood category :
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
        ImageView ivExpand;
        CheckBox checkBox;

        ViewHolder(@NonNull View view) {
            super(view);
            tvCategoryName = view.findViewById(R.id.tv_category);
            ivExpand = view.findViewById(R.id.iv_category);
            checkBox = view.findViewById(R.id.check_box);
        }
    }
}
