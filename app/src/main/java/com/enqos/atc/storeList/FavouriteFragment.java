package com.enqos.atc.storeList;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.enqos.atc.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FavouriteFragment extends Fragment {

    @BindView(R.id.gridview)
    GridView gridView;

    private Unbinder unbinder;

    public FavouriteFragment() {
    }

    public static FavouriteFragment newInstance() {
        return new FavouriteFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_favourite_layout, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        setShopListData();
        return rootView;
    }

    /**
     * Set the shop lists
     */
    private void setShopListData() {

        gridView.setAdapter(new ShopListAdapter());

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
