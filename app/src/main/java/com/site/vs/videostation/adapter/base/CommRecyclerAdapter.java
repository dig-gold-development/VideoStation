package com.site.vs.videostation.adapter.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author zhangbb
 * @date 2016/10/19
 */
public abstract class CommRecyclerAdapter<T> extends RecyclerView.Adapter implements AdapterOperate<T>,
        RecyclerDataOperate<T> {

    protected final Context mContext;
    protected final int mLayoutResId;
    protected final List<T> mData;

    public CommRecyclerAdapter(Context context) {
        this(context, -1, null);
    }

    public CommRecyclerAdapter(Context context, int layoutResId) {
        this(context, layoutResId, null);
    }

    public CommRecyclerAdapter(Context context, int layoutResId, List<T> data) {
        this.mData = data == null ? new ArrayList<T>() : new ArrayList<>(data);
        this.mContext = context;
        this.mLayoutResId = layoutResId;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final BaseAdapterHelper helper = BaseAdapterHelper.get(mContext, null, parent, viewType, -1);
        return new RecyclerViewHolder(helper.getView(), helper);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BaseAdapterHelper helper = ((RecyclerViewHolder) holder).adapterHelper;
        helper.setAssociatedObject(getItem(position));
        onUpdate(helper, getItem(position), position);
    }

    @Override
    public int getItemViewType(int position) {
        return getLayoutResId(getItem(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public T getItem(int position) {
        return position >= mData.size() ? null : mData.get(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public T getData() {
        return (T) mData;
    }

    @Override
    public int getLayoutResId(T item) {
        return this.mLayoutResId;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        BaseAdapterHelper adapterHelper;

        public RecyclerViewHolder(View itemView, BaseAdapterHelper adapterHelper) {
            super(itemView);
            this.adapterHelper = adapterHelper;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onItemClickListener) {
                        onItemClickListener.onItemClick(RecyclerViewHolder.this, v,
                                getAdapterPosition());
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (null != onItemLongClickListener) {
                        onItemLongClickListener.onItemLongClick(RecyclerViewHolder.this, v,
                                getAdapterPosition());
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    /**
     * 监听器相关
     */
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(RecyclerView.ViewHolder viewHolder, View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(RecyclerView.ViewHolder viewHolder, View view, int position);
    }

    /**
     * 数据操作相关
     *
     * @param elem
     */
    @Override
    public void add(T elem) {
        mData.add(elem);
        notifyItemInserted(mData.size());
    }

    @Override
    public void add(T elem, int index) {
        mData.add(index, elem);
        notifyItemInserted(index);
    }

    @Override
    public void addAll(List<T> elem) {
        mData.addAll(elem);
        notifyItemRangeInserted(mData.size() - elem.size(), elem.size());
    }

    @Override
    public void addAll(Set<T> elem) {
        mData.addAll(elem);
        notifyItemRangeInserted(mData.size() - elem.size(), elem.size());
    }

    @Override
    public void remove(T elem) {
        final int position = mData.indexOf(elem);
        mData.remove(elem);
        notifyItemRemoved(position);
    }

    @Override
    public void removeAll() {
        mData.clear();
        notifyDataSetChanged();
    }

    @Override
    public void replaceAll(List<T> elem) {
        mData.clear();
        mData.addAll(elem);
        notifyDataSetChanged();
    }
}
