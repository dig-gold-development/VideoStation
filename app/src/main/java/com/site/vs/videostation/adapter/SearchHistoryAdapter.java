package com.site.vs.videostation.adapter;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.site.vs.videostation.R;
import com.site.vs.videostation.adapter.base.BaseAdapterHelper;
import com.site.vs.videostation.adapter.base.CommRecyclerAdapter;


/**
 * @author zhangbb
 * @date 2016/12/19
 */
public class SearchHistoryAdapter extends CommRecyclerAdapter<String> {

    public SearchHistoryAdapter(Context context, int id) {
        super(context, id);
    }

    @Override
    public int getItemCount() {
        if (mData != null) {
            return mData.size() > 10 ? 10 : mData.size();
        }
        return 0;
    }

    @Override
    public void onUpdate(BaseAdapterHelper helper, String item, int position) {
        helper.setText(R.id.tv_history, item);
        setOnItemClickListener(new CommRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder, View view, int position) {
                if (clickListener != null) {
                    clickListener.onItemClick(getItem(position));
                }
            }
        });
    }

    public OnItemClickListener clickListener;

    public void setClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(String s);
    }
}
