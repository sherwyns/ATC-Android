package com.enqos.atc.storeList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.enqos.atc.R;
import com.enqos.atc.data.response.StoreEntity;

import java.util.List;

public class ShopListAdapter extends BaseAdapter {

    private Context context;
    private List<StoreEntity> data;

    public ShopListAdapter(Context context, List<StoreEntity> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
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
            viewHolder.favImg = view.findViewById(R.id.img_fav);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.favImg.setOnClickListener(view1 -> {
            view1.startAnimation(AnimationUtils.loadAnimation(context, R.anim.image_click));
            viewHolder.favImg.setImageResource(R.drawable.ic_favorite_black_24dp);
        });
        Glide.with(viewGroup.getContext()).load("https://www.excelsior.com.mt/wp-content/uploads/2011/03/spice-island-restaurant-Malta-1024x683.jpg")
                .apply(new RequestOptions().override(250, 150)
                        .error(R.drawable.ic_photo_size_select_actual_black_24dp)
                        .placeholder(R.drawable.ic_photo_size_select_actual_black_24dp)
                        .centerCrop())
                .into(viewHolder.imageView);
        return view;
    }

    private static class ViewHolder {

        ImageView imageView, favImg;

    }


}
