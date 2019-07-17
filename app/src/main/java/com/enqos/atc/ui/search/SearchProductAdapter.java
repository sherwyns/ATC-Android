package com.enqos.atc.ui.search;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.enqos.atc.R;
import com.enqos.atc.data.response.ProductEntity;

import java.util.List;

public class SearchProductAdapter extends RecyclerView.Adapter<SearchProductAdapter.MyViewHolder> {
    private List<ProductEntity> data;
    private RecyclerViewItemClickListener recyclerViewItemClickListener;

    public SearchProductAdapter(List<ProductEntity> data, RecyclerViewItemClickListener recyclerViewItemClickListener) {
        this.data = data;
        this.recyclerViewItemClickListener = recyclerViewItemClickListener;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_view_item, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int i) {
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
                if (!TextUtils.isEmpty(data.get(i).getPrice()) && data.get(i).getPrice().contains("."))
                    viewHolder.price.setText(String.format("$ %s", data.get(i).getPrice()));
                else
                    viewHolder.price.setText(String.format("%s.00", String.format("$ %s", data.get(i).getPrice())));
            }
        } else {
            viewHolder.price.setVisibility(View.GONE);
            viewHolder.tvCall.setVisibility(View.VISIBLE);
        }
        String url = "";
        if (TextUtils.isEmpty(data.get(i).getImage_medium()) && !TextUtils.isEmpty(data.get(i).getImage()))
            url = data.get(i).getImage();
        else if (!TextUtils.isEmpty(data.get(i).getImage_medium()))
            url = data.get(i).getImage_medium();
        Glide.with(viewHolder.itemView.getContext()).load(url)
                .apply(new RequestOptions().override(250, 180)
                        .error(R.drawable.ic_photo_size_select_actual_black_24dp)
                        .placeholder(R.drawable.ic_photo_size_select_actual_black_24dp)
                        .centerCrop())
                .into(viewHolder.imageView);
        viewHolder.itemView.setOnClickListener(view -> recyclerViewItemClickListener.onItemClick(data.get(i)));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, favImg;
        TextView name, price, tvCall;

        public MyViewHolder(@NonNull View view) {
            super(view);
            imageView = view.findViewById(R.id.product_img);
            favImg = view.findViewById(R.id.img_fav);
            name = view.findViewById(R.id.tv_product_name);
            price = view.findViewById(R.id.tv_price);
            tvCall = view.findViewById(R.id.tv_call);
        }
    }
}
