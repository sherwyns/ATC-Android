package com.enqos.atc.ui.filter.multifilter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.enqos.atc.R;
import com.enqos.atc.data.response.CategoryEntity;

import java.util.List;

public class MultiFilterAdapter extends BaseExpandableListAdapter {
    private List<CategoryEntity> parentItems;
    private LayoutInflater inflater;
    private int count = 0;

    MultiFilterAdapter(Context context, List<CategoryEntity> parentItems) {
        this.parentItems = parentItems;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getGroupCount() {
        return parentItems.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        Log.i("******", "ON_CHILD_COUNT");
        return parentItems.get(groupPosition).getAllChildren().size();
    }

    @Override
    public Object getGroup(int i) {
        return null;
    }

    @Override
    public Object getChild(int i, int i1) {
        return null;
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getGroupView(final int groupPosition, final boolean b, View convertView, ViewGroup viewGroup) {
        final ViewHolderParent viewHolderParent;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.filter_row_layout, null);
            viewHolderParent = new ViewHolderParent();
            viewHolderParent.tvMainCategoryName = convertView.findViewById(R.id.tv_category);
            viewHolderParent.cbMainCategory = convertView.findViewById(R.id.check_box);
            viewHolderParent.ivCategory = convertView.findViewById(R.id.iv_category);
            viewHolderParent.tvCount = convertView.findViewById(R.id.tv_count);
            convertView.setTag(viewHolderParent);
        } else {
            viewHolderParent = (ViewHolderParent) convertView.getTag();
        }

        if (parentItems.get(groupPosition).getSelectedCount() > 0) {
            viewHolderParent.tvCount.setVisibility(View.VISIBLE);
            if (parentItems.get(groupPosition).getSelectedCount() == parentItems.get(groupPosition).getAllChildren().size())
                viewHolderParent.tvCount.setText("All");
            else
                viewHolderParent.tvCount.setText(String.valueOf(parentItems.get(groupPosition).getSelectedCount()));
        } else
            viewHolderParent.tvCount.setVisibility(View.GONE);
        if (parentItems.get(groupPosition).isSelected()) {
            viewHolderParent.cbMainCategory.setChecked(true);
        } else {
            viewHolderParent.cbMainCategory.setChecked(false);
        }
        if (parentItems.get(groupPosition).getAllChildren().isEmpty())
            viewHolderParent.ivCategory.setVisibility(View.GONE);
        else
            viewHolderParent.ivCategory.setVisibility(View.VISIBLE);
        viewHolderParent.tvMainCategoryName.setText(parentItems.get(groupPosition).getName());
        viewHolderParent.cbMainCategory.setOnClickListener(v -> {
            boolean isChecked = parentItems.get(groupPosition).isSelected();
            parentItems.get(groupPosition).setSelected(!isChecked);
            for (CategoryEntity categoryEntity : parentItems.get(groupPosition).getAllChildren()) {
                if (!isChecked) {
                    parentItems.get(groupPosition).setSelectedCount(parentItems.get(groupPosition).getAllChildren().size());
                    categoryEntity.setSelected(true);
                } else {
                    parentItems.get(groupPosition).setSelectedCount(0);
                    categoryEntity.setSelected(false);
                }
            }
            notifyDataSetChanged();
        });
        ConstantManager.categories = parentItems;
        return convertView;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getChildView(final int groupPosition, final int childPosition, final boolean b, View convertView, ViewGroup viewGroup) {
        final ViewHolderChild viewHolderChild;
        List<CategoryEntity> child = parentItems.get(groupPosition).getAllChildren();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.child_row_layout, null);
            viewHolderChild = new ViewHolderChild();
            viewHolderChild.cbSubCategory = convertView.findViewById(R.id.check_box);
            convertView.setTag(viewHolderChild);
        } else {
            viewHolderChild = (ViewHolderChild) convertView.getTag();
        }
        if (child.get(childPosition).isSelected()) {
            viewHolderChild.cbSubCategory.setChecked(true);
        } else {
            viewHolderChild.cbSubCategory.setChecked(false);
        }
        viewHolderChild.cbSubCategory.setOnClickListener(v -> {
            boolean isChecked = child.get(childPosition).isSelected();
            if (isChecked) {
                count = 0;
                child.get(childPosition).setSelected(false);
            } else {
                count = 0;
                child.get(childPosition).setSelected(true);
            }
            for (CategoryEntity categoryEntity : child) {
                if (categoryEntity.isSelected()) {
                    count++;
                }
            }
            parentItems.get(groupPosition).setSelectedCount(count);
            if (count == child.size())
                parentItems.get(groupPosition).setSelected(true);
            else
                parentItems.get(groupPosition).setSelected(false);
            notifyDataSetChanged();
        });
        viewHolderChild.cbSubCategory.setText(child.get(childPosition).getName());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    private class ViewHolderParent {
        TextView tvMainCategoryName;
        CheckBox cbMainCategory;
        ImageView ivCategory;
        TextView tvCount;
    }

    private class ViewHolderChild {
        CheckBox cbSubCategory;
    }
}
