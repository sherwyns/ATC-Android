package com.enqos.atc.storeList;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.enqos.atc.R;
import com.enqos.atc.data.response.StoreEntity;
import com.enqos.atc.data.response.StoreFavoriteEntity;
import com.enqos.atc.listener.StoreListener;

import java.util.List;

public class ShopListAdapter extends BaseAdapter {

    private Context context;
    private List<StoreEntity> data;
    private StoreListener storeListener;

    ShopListAdapter(Context context, List<StoreEntity> data) {
        this.context = context;
        this.data = data;
    }

    void setListener(StoreListener storeListener) {
        this.storeListener = storeListener;
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
            viewHolder.shopName = view.findViewById(R.id.shop_name);
            viewHolder.neighbourhood = view.findViewById(R.id.neighbourhood);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if (data.get(i).isFavourite()) {
            viewHolder.favImg.setImageResource(R.drawable.ic_favorite_black_24dp);
        } else {
            viewHolder.favImg.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }

        if (!TextUtils.isEmpty(data.get(i).getShop_name()))
            viewHolder.shopName.setText(data.get(i).getShop_name());
        if (!TextUtils.isEmpty(data.get(i).getNeighbourhood()))
            viewHolder.neighbourhood.setText(data.get(i).getNeighbourhood());

        viewHolder.favImg.setOnClickListener(view1 -> {
            view1.startAnimation(AnimationUtils.loadAnimation(context, R.anim.image_click));
            boolean isFav = data.get(i).isFavourite();
            if (isFav) {

                if (storeListener instanceof FavouriteFragment)
                    storeListener.onRemoveFav(i);
                else
                    storeListener.onSaveStoreFavorite(new StoreFavoriteEntity(data.get(i).getId(), "0"));
            } else {
                storeListener.onSaveStoreFavorite(new StoreFavoriteEntity(data.get(i).getId(), "1"));
            }
            if (!data.isEmpty())
                data.get(i).setFavourite(!isFav);
            notifyDataSetChanged();

        });
        Glide.with(viewGroup.getContext()).load("http://www.morestore.com/images/Website/inner-banner/store-experience.jpg")
                .apply(new RequestOptions().override(250, 150)
                        .error(R.drawable.ic_photo_size_select_actual_black_24dp)
                        .placeholder(R.drawable.ic_photo_size_select_actual_black_24dp)
                        .centerCrop())
                .into(viewHolder.imageView);
        return view;
    }

    private static class ViewHolder {

        ImageView imageView, favImg;
        TextView shopName, neighbourhood;

    }


}
