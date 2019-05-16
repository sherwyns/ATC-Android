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
import com.enqos.atc.data.response.Neighbourhood;
import com.enqos.atc.ui.filter.multifilter.ConstantManager;

import java.util.List;

public class NeibourHoodFilterAdapter extends RecyclerView.Adapter<NeibourHoodFilterAdapter.ViewHolder> {
    private List<Neighbourhood> categoryEntities;
    private boolean isProduct;

    public NeibourHoodFilterAdapter(List<Neighbourhood> categoryEntities, boolean isProduct) {
        this.categoryEntities = categoryEntities;
        this.isProduct = isProduct;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.filter_row_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Neighbourhood categoryEntity = categoryEntities.get(i);
        viewHolder.tvCategoryName.setText(categoryEntity.getNeighbourhood());
        viewHolder.ivExpand.setVisibility(View.GONE);
        viewHolder.checkBox.setChecked(categoryEntity.isSelected());
        viewHolder.checkBox.setOnCheckedChangeListener((compoundButton, checked) -> categoryEntity.setSelected(checked));
        if (isProduct)
            ConstantManager.neighbourhoods = categoryEntities;
        else
            ConstantManager.storeNeighbours = categoryEntities;
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
