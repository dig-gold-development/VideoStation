package com.site.vs.videostation.ui.search;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.site.vs.videostation.base.RecyclerFragment;
import com.site.vs.videostation.entity.SearchResultEntity;
import com.site.vs.videostation.widget.refreshRecycler.RecyclerAdapterWithHF;

/**
 * @author zhangbb
 * @date 2016/12/19
 */
public abstract class SearchBaseFragment extends RecyclerFragment<SearchResultEntity.SearchResult>
        implements RecyclerAdapterWithHF.OnItemClickListener {

    public Context mContext;
    protected boolean isVisible;
    private boolean isPrepared;
    private boolean isFirst = true;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            lazyLoad();
        } else {
            isVisible = false;
            onInvisible();
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isPrepared = true;
        lazyLoad();
    }

    protected void lazyLoad() {
        if (!isPrepared || !isVisible || !isFirst) {
            return;
        }
        initData();
        isFirst = false;
    }

    protected void onInvisible() {
    }

    public abstract void initData();

    protected abstract void initView(View view);
}
