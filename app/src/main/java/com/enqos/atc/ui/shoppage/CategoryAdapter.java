package com.enqos.atc.ui.shoppage;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.enqos.atc.R;
import com.enqos.atc.listener.CategoryItemClickListner;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private List<String> categories;
    private Context context;
    private CategoryItemClickListner listner;
    private int selectedIndex = 0;

    CategoryAdapter(Context context, List<String> categories) {
        this.categories = categories;
        this.context = context;
    }

    public void setListener(CategoryItemClickListner listner) {
        this.listner = listner;
        if (listner != null && categories != null && categories.size() > 0)
            listner.onCategorySelected(categories.get(0));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_row_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        if (selectedIndex == viewHolder.getAdapterPosition()) {
            viewHolder.tvCategory.setBackgroundResource(R.drawable.gradient_blue);
            viewHolder.tvCategory.setTextColor(ContextCompat.getColor(context, android.R.color.white));
        } else {
            viewHolder.tvCategory.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
            viewHolder.tvCategory.setTextColor(ContextCompat.getColor(context, R.color.cateoryTextColor));
        }

        viewHolder.itemView.setOnClickListener(view -> {
            selectedIndex = viewHolder.getAdapterPosition();
            notifyDataSetChanged();
            listner.onCategorySelected(categories.get(i));

        });
        viewHolder.tvCategory.setText(categories.get(i));
    }

    @Override
    public int getItemCount() {
        return categories == null ? 0 : categories.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategory;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.tv_category);
        }
    }
}
