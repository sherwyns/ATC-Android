package com.enqos.atc.ui.productdetail;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.enqos.atc.R;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class ProductDetailFragment extends Fragment {

    @BindView(R.id.tv_product_name)
    TextView tvProductName;
    @BindView(R.id.tv_product_des)
    TextView tvProductDes;
    @BindView(R.id.iv_product)
    ImageView ivProductImg;
    @BindView(R.id.tv_product_price)
    TextView tvPrice;

    private static final String PRODUCT_ID = "productId";
    private static final String IMG_URL = "IMG_URL";
    private static final String PRODUCT_NAME = "PRODUCT_NAME";
    private static final String PRODUCT_PRICE = "PRODUCT_PRICE";
    private static final String PRODUCT_DES = "PRODUCT_DES";
    private String mProductId, url, name, price, des;
    private Unbinder unbinder;


    public ProductDetailFragment() {
    }


    public static ProductDetailFragment newInstance(String mProductId, String url, String name, String price, String des) {
        ProductDetailFragment fragment = new ProductDetailFragment();
        Bundle args = new Bundle();
        args.putString(PRODUCT_ID, mProductId);
        args.putString(IMG_URL, url);
        args.putString(PRODUCT_NAME, name);
        args.putString(PRODUCT_PRICE, price);
        args.putString(PRODUCT_DES, des);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mProductId = getArguments().getString(PRODUCT_ID);
            url = getArguments().getString(IMG_URL);
            name = getArguments().getString(PRODUCT_NAME);
            price = getArguments().getString(PRODUCT_PRICE);
            des = getArguments().getString(PRODUCT_DES);

        }
    }

    private void setValues() {

        if (TextUtils.isEmpty(name))
            tvProductName.setText(name);
        if (TextUtils.isEmpty(price))
            tvPrice.setText(price);
        if (TextUtils.isEmpty(des))
            tvProductDes.setText(des);

        Glide.with(Objects.requireNonNull(getActivity())).load(url)
                .apply(new RequestOptions()
                        .error(R.drawable.ic_photo_size_select_actual_black_24dp)
                        .placeholder(R.drawable.ic_photo_size_select_actual_black_24dp)
                        .centerCrop())
                .into(ivProductImg);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        setValues();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
