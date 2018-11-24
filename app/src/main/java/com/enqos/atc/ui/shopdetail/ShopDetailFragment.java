package com.enqos.atc.ui.shopdetail;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.enqos.atc.R;
import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.listener.StoreActivityListener;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopDetailFragment extends Fragment {

    private Unbinder unbinder;
    private StoreActivityListener listener;
    @BindView(R.id.iv_shop_image)
    ImageView shopImg;

    @Inject
    public ShopDetailFragment() {

        AtcApplication.getAppComponents().inject(this);
    }


    public static ShopDetailFragment newInstance() {
        return new ShopDetailFragment();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (StoreActivityListener) context;

    }

    @Override
    public void onResume() {
        super.onResume();
        if (listener != null)
            listener.changeHeader(R.drawable.ic_keyboard_arrow_left_black_24dp, "Shop Detail", R.drawable.ic_filter_outline);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shop_detail, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        Glide.with(Objects.requireNonNull(getActivity())).load("https://www.soc.tas.edu.au/wp-content/uploads/college-shop-internal.jpg")
                .apply(new RequestOptions()
                        .error(R.drawable.ic_photo_size_select_actual_black_24dp)
                        .placeholder(R.drawable.ic_photo_size_select_actual_black_24dp)
                        .centerCrop())
                .into(shopImg);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
