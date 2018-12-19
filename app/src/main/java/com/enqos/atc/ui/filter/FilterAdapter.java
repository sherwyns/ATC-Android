package com.enqos.atc.ui.filter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.enqos.atc.R;
import com.enqos.atc.data.response.CategoryEntity;

import java.util.List;

public class FilterAdapter extends BaseAdapter {
    private List<CategoryEntity> categoryEntities;

    FilterAdapter(List<CategoryEntity> categoryEntities) {
        this.categoryEntities = categoryEntities;
    }

    @Override
    public int getCount() {
        return categoryEntities == null ? 0 : categoryEntities.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.filter_row_layout, viewGroup, false);
            viewHolder.tvCategoryName = view.findViewById(R.id.tv_category);
            viewHolder.ivCategory = view.findViewById(R.id.iv_category);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if (i == 1)
            viewHolder.ivCategory.setImageResource(R.drawable.ic_card_giftcard_black_24dp);
        else if (i == 3)
            viewHolder.ivCategory.setImageResource(R.drawable.ic_directions_run_black_24dp);
        viewHolder.tvCategoryName.setText(categoryEntities.get(i).getName());
        return view;
    }

    static class ViewHolder {

        TextView tvCategoryName;
        ImageView ivCategory;
    }
}
