package com.enqos.atc.ui.shoppage;

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
import com.enqos.atc.data.response.ProductEntity;
import com.enqos.atc.listener.StoreListener;
import com.enqos.atc.ui.storeList.FavouriteFragment;

import java.util.List;

public class StorePageAdapter extends BaseAdapter {

    private Context context;
    private List<ProductEntity> data;
    private StoreListener storeListener;

    public StorePageAdapter(Context context, List<ProductEntity> data) {
        this.context = context;
        this.data = data;
    }

    public void setListener(StoreListener storeListener) {
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
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_view_item, viewGroup, false);
            viewHolder.imageView = view.findViewById(R.id.product_img);
            viewHolder.favImg = view.findViewById(R.id.img_fav);
            viewHolder.name = view.findViewById(R.id.tv_product_name);
            viewHolder.price = view.findViewById(R.id.tv_price);
            viewHolder.tvCall = view.findViewById(R.id.tv_call);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if (data.get(i).isFavourite())
            viewHolder.favImg.setImageResource(R.drawable.ic_favorite_black_24dp);
        else
            viewHolder.favImg.setImageResource(R.drawable.ic_favorite_border_black_24dp);

        if (!TextUtils.isEmpty(data.get(i).getTitle()))
            viewHolder.name.setText(data.get(i).getTitle());
        if (!TextUtils.isEmpty(data.get(i).getPrice())) {
            if (data.get(i).getPrice().equalsIgnoreCase("0") || data.get(i).getPrice().equalsIgnoreCase("0.0")) {
                viewHolder.price.setVisibility(View.GONE);
                viewHolder.tvCall.setVisibility(View.VISIBLE);
            } else {
                viewHolder.price.setVisibility(View.VISIBLE);
                viewHolder.tvCall.setVisibility(View.INVISIBLE);
                viewHolder.price.setText(String.format("$ %s", data.get(i).getPrice()));
            }
        } else {
            viewHolder.price.setVisibility(View.GONE);
            viewHolder.tvCall.setVisibility(View.VISIBLE);
        }


        viewHolder.favImg.setOnClickListener(view1 -> {
            if (storeListener != null) {
                view1.startAnimation(AnimationUtils.loadAnimation(context, R.anim.image_click));
                boolean isFav = data.get(i).isFavourite();
                if (isFav) {
                    if (storeListener instanceof FavouriteFragment)
                        storeListener.onRemoveFav(i, false);
                    else {
                        storeListener.onSaveProductFavorite(data.get(i), false, i);
                        if (!data.isEmpty())
                            data.get(i).setFavourite(false);
                    }
                } else {
                    storeListener.onSaveProductFavorite(data.get(i), true, i);
                    if (!data.isEmpty())
                        data.get(i).setFavourite(true);
                }

            }
        });
        Glide.with(viewGroup.getContext()).load(data.get(i).getProduct_image())
                .apply(new RequestOptions().override(250, 180)
                        .error(R.drawable.ic_photo_size_select_actual_black_24dp)
                        .placeholder(R.drawable.ic_photo_size_select_actual_black_24dp)
                        .centerCrop())
                .into(viewHolder.imageView);
        return view;
    }

    private static class ViewHolder {

        ImageView imageView, favImg;
        TextView name, price, tvCall;

    }


}
