package com.enqos.atc.ui.productdetail;

import android.content.Context;
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
import com.enqos.atc.listener.RecyclerViewItemClickListner;

import java.util.List;

public class SimiliarProductsAdapter extends RecyclerView.Adapter<SimiliarProductsAdapter.ViewHolder> {
    private Context context;
    private List<ProductEntity> products;
    private RecyclerViewItemClickListner listner;

    public SimiliarProductsAdapter(Context context, List<ProductEntity> products) {

        this.context = context;
        this.products = products;
    }

    public void setListener(RecyclerViewItemClickListner listener) {
        this.listner = listener;
    }

    @NonNull
    @Override
    public SimiliarProductsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.similiar_product_view_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SimiliarProductsAdapter.ViewHolder viewHolder, int i) {
        ProductEntity product = products.get(i);
        if (!TextUtils.isEmpty(product.getTitle()))
            viewHolder.name.setText(product.getTitle());

        if (!TextUtils.isEmpty(products.get(i).getPrice())) {
            if (products.get(i).getPrice().equalsIgnoreCase("0") || products.get(i).getPrice().equalsIgnoreCase("0.0")) {
                viewHolder.price.setVisibility(View.GONE);
                viewHolder.tvCall.setVisibility(View.VISIBLE);
            } else {
                viewHolder.price.setVisibility(View.VISIBLE);
                viewHolder.tvCall.setVisibility(View.INVISIBLE);
                if (!TextUtils.isEmpty(products.get(i).getPrice()) && products.get(i).getPrice().contains("."))
                    viewHolder.price.setText(String.format("$ %s", products.get(i).getPrice()));
                else
                    viewHolder.price.setText(String.format("%s.00", String.format("$ %s", products.get(i).getPrice())));
            }
        } else {
            viewHolder.price.setVisibility(View.GONE);
            viewHolder.tvCall.setVisibility(View.VISIBLE);
        }
        String url = "";
        if (TextUtils.isEmpty(product.getProduct_image()) && !TextUtils.isEmpty(product.getImage()))
            url = product.getImage();
        else if (!TextUtils.isEmpty(product.getProduct_image()))
            url = product.getProduct_image();
        Glide.with(context).load(url)
                .apply(new RequestOptions()
                        //.override(150, 180)
                        .error(R.drawable.ic_photo_size_select_actual_black_24dp)
                        .placeholder(R.drawable.ic_photo_size_select_actual_black_24dp)
                        .centerCrop())
                .into(viewHolder.imageView);


        viewHolder.itemView.setOnClickListener(view -> listner.onItemClick(viewHolder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return products == null ? 0 : products.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, favImg;
        TextView name, price, tvCall;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.product_img);
            favImg = itemView.findViewById(R.id.img_fav);
            name = itemView.findViewById(R.id.tv_product_name);
            price = itemView.findViewById(R.id.tv_price);
            tvCall = itemView.findViewById(R.id.tv_call);
        }
    }
}
