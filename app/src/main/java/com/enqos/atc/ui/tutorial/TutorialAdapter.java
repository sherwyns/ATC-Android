package com.enqos.atc.ui.tutorial;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.enqos.atc.R;

import java.util.List;

public class TutorialAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;
    List<Integer> resIds;

    TutorialAdapter(Context context, List<Integer> resIds) {
        this.context = context;
        this.resIds = resIds;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return resIds.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView = layoutInflater.inflate(R.layout.tutorial_image_view, container, false);

        ImageView imageView = itemView.findViewById(R.id.iv_tutorial);
        imageView.setImageResource(resIds.get(position));

        container.addView(itemView);

        return itemView;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }
}
