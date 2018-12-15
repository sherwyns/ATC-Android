package com.enqos.atc.ui.filter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.enqos.atc.R;

public class FilterAdapter extends BaseAdapter {
    @Override
    public int getCount() {
        return 10;
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

        viewHolder.tvCategoryName.setText("Category name " + i + 1);
        return view;
    }

    static class ViewHolder {

        TextView tvCategoryName;
        ImageView ivCategory;
    }
}
