package com.enqos.atc.ui.shopdetail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.enqos.atc.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShopDetailActivity extends AppCompatActivity {

    @BindView(R.id.iv_shop_image)
    ImageView ivShopImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);
        ButterKnife.bind(this);
        Glide.with(this).load("http://sevenhillsrunningshop.com/aroundthecorner/img/7HillsBobbleHat.png")
                .apply(new RequestOptions().override(250, 180)
                        .error(R.drawable.ic_photo_size_select_actual_black_24dp)
                        .placeholder(R.drawable.ic_photo_size_select_actual_black_24dp)
                        .centerCrop())
                .into(ivShopImage);
    }
}
