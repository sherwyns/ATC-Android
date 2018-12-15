package com.enqos.atc.ui.filter;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.enqos.atc.R;
import com.enqos.atc.listener.StoreActivityListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class FilterFragment extends Fragment {
    private Unbinder unbinder;
    @BindView(R.id.gridview)
    GridView gridView;
    private StoreActivityListener storeActivityListener;

    public FilterFragment() {

    }


    public static FilterFragment getInstance() {
        return new FilterFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categroy, container, false);
        unbinder = ButterKnife.bind(this, view);
        gridView.setAdapter(new FilterAdapter());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (storeActivityListener != null)
            storeActivityListener.changeHeader(R.drawable.ic_menu_black_24dp, getString(R.string.filter_by_category), R.drawable.ic_filter_outline);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        storeActivityListener = (StoreActivityListener) context;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
