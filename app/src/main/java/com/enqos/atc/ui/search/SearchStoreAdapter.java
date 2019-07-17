package com.enqos.atc.ui.search;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.enqos.atc.R;
import com.enqos.atc.data.response.ProductEntity;
import com.enqos.atc.data.response.StoreEntity;
import com.enqos.atc.listener.StoreListener;
import com.enqos.atc.ui.storeList.FavouriteFragment;

import java.util.List;

public class SearchStoreAdapter extends RecyclerView.Adapter<SearchStoreAdapter.MyViewHolder> {
    private List<StoreEntity> data;
    private RecyclerViewItemClickListener storeListener;

    public SearchStoreAdapter(List<StoreEntity> data, RecyclerViewItemClickListener storeListener) {
        this.data = data;
        this.storeListener = storeListener;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shop_item_layout, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int i) {
        if (data.get(i).isFavourite()) {
            viewHolder.favImg.setImageResource(R.drawable.ic_favorite_black_24dp);
        } else {
            viewHolder.favImg.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }

        if (!TextUtils.isEmpty(data.get(i).getShop_name()))
            viewHolder.shopName.setText(data.get(i).getShop_name());
        else if (!TextUtils.isEmpty(data.get(i).getName()))
            viewHolder.shopName.setText(data.get(i).getName());
        if (!TextUtils.isEmpty(data.get(i).getNeighbourhood()))
            viewHolder.neighbourhood.setText(data.get(i).getNeighbourhood());

        viewHolder.itemView.setOnClickListener(view1 -> storeListener.onItemClick(data.get(i)));


        String url;
        if (TextUtils.isEmpty(data.get(i).getImage()) || data.get(i).getImage().equalsIgnoreCase("null"))
            url = "https://www.retailgazette.co.uk/wp/wp-content/uploads/Typo-696x464.jpg";
        else
            url = data.get(i).getImage();

        if (data.get(i).getCategory() != null && !data.get(i).getCategory().isEmpty()) {
            Glide.with(viewHolder.itemView.getContext()).load(data.get(i).getCategory().get(0).getImage_url())
                    .apply(new RequestOptions()
                            .error(R.drawable.ic_restaurant_menu_black_24dp)
                            .placeholder(R.drawable.ic_restaurant_menu_black_24dp)
                            .centerCrop())
                    .into(viewHolder.ivCategory);
        }

        Glide.with(viewHolder.itemView.getContext()).load(url)
                .apply(new RequestOptions().override(250, 180)
                        .error(R.drawable.ic_photo_size_select_actual_black_24dp)
                        .placeholder(R.drawable.ic_photo_size_select_actual_black_24dp)
                        .centerCrop())
                .into(viewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, favImg, ivCategory;
        TextView shopName, neighbourhood;

        public MyViewHolder(@NonNull View view) {
            super(view);
            imageView = view.findViewById(R.id.shop_img);
            ivCategory = view.findViewById(R.id.iv_category);
            favImg = view.findViewById(R.id.img_fav);
            shopName = view.findViewById(R.id.shop_name);
            neighbourhood = view.findViewById(R.id.neighbourhood);
        }
    }
}
