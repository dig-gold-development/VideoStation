package com.site.vs.videostation.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.site.vs.videostation.R;
import com.site.vs.videostation.adapter.base.BaseAdapterHelper;
import com.site.vs.videostation.adapter.base.CommRecyclerAdapter;
import com.site.vs.videostation.entity.CategoryFilterEntity;


/**
 * @author zhangbb
 * @date 2016/12/19
 */
public class CategoryHeaderAdapter extends CommRecyclerAdapter<CategoryFilterEntity> {

    private int lastClickPosition = 0;
    private int clickTemp = 0;
    private String type;

    public CategoryHeaderAdapter(Context context, int id, String type) {
        super(context, id);
        this.type = type;
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size() + 1;
    }

    @Override
    public CategoryFilterEntity getItem(int position) {
        if (position == 0) {
            return null;
        }
        return mData.get(position - 1);
    }

    @Override
    public void onUpdate(BaseAdapterHelper helper, final CategoryFilterEntity item, int position) {
        TextView textView = helper.getView(R.id.tv_name);
        if (position == 0) {
            textView.setText(type);
        } else if (item != null) {
            textView.setText(item.getName());
        }

        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder, View view, int position) {
                clickTemp = lastClickPosition;
                lastClickPosition = position;
                if (clickTemp == position) {
                    return;
                }
                notifyItemChanged(position);
                notifyItemChanged(clickTemp);
                if (changeListener != null) {
                    changeListener.OnFilterChangeClick(position);
                }
            }
        });

        if (lastClickPosition == position) {
            textView.setTextColor(Color.parseColor("#00AE54"));
        } else {
            textView.setTextColor(Color.parseColor("#262626"));
        }
    }

    public void setDefaultSelect(int cid) {
        this.lastClickPosition = cid;
    }

    public CategoryFilterEntity getSelectItem() {
        return getItem(lastClickPosition);
    }

    public OnFilterChangeListener changeListener;

    public void setChangeListener(OnFilterChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    public interface OnFilterChangeListener {
        public void OnFilterChangeClick(int index);
    }
}
