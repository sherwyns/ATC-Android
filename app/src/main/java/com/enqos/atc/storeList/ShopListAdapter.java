package com.enqos.atc.storeList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.enqos.atc.R;

public class ShopListAdapter extends BaseAdapter {
    @Override
    public int getCount() {
        return 20;
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
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shop_item_layout, viewGroup, false);
            viewHolder.imageView = view.findViewById(R.id.shop_img);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Glide.with(viewGroup.getContext()).load("https://www.excelsior.com.mt/wp-content/uploads/2011/03/spice-island-restaurant-Malta-1024x683.jpg")
                .apply(new RequestOptions().override(250, 150).centerCrop()).into(viewHolder.imageView);
        return view;
    }

    private static class ViewHolder {

        ImageView imageView;

    }


}
