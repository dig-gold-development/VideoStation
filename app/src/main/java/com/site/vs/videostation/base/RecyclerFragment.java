package com.site.vs.videostation.base;

import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.site.vs.videostation.R;
import com.site.vs.videostation.adapter.base.CommRecyclerAdapter;
import com.site.vs.videostation.widget.loadingtips.DefaultLoadingLayout;
import com.site.vs.videostation.widget.loadingtips.SmartLoadingLayout;
import com.site.vs.videostation.widget.refreshRecycler.RecyclerAdapterWithHF;
import com.site.vs.videostation.widget.refreshRecycler.ptr.OnLoadMoreListener;
import com.site.vs.videostation.widget.refreshRecycler.ptr.PtrClassicFrameLayout;
import com.site.vs.videostation.widget.refreshRecycler.ptr.PtrDefaultHandler;
import com.site.vs.videostation.widget.refreshRecycler.ptr.PtrFrameLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author zhangbb
 * @date 2016/11/9
 */
public abstract class RecyclerFragment<T> extends BaseFragment {

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;
    @BindView(R.id.ptr_layout)
    public PtrClassicFrameLayout frameLayout;
    public RecyclerAdapterWithHF adapterWithHF;
    public CommRecyclerAdapter commRecyclerAdapter;
    public DefaultLoadingLayout tipsLayout;
    public View headerView;
    public View emptyView;
    public View errorView;
    private boolean isPullRefreshEnable = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_refresh_recycler, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initViews();
    }

    private void initViews() {
        tipsLayout = SmartLoadingLayout.createDefaultLayout(getContext(), frameLayout);
        commRecyclerAdapter = createAdapter();
        adapterWithHF = new RecyclerAdapterWithHF(commRecyclerAdapter);
        View headerView = createHeader();
        if (headerView != null) {
            adapterWithHF.addHeader(headerView);
        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        gridLayoutManager.setSpanSizeLookup(adapterWithHF.obtainGridSpanSizeLookUp(3));
        recyclerView.setLayoutManager(gridLayoutManager);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterWithHF);
        frameLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                onLoadMore();
            }
        });
    }

    public void setBackgroundColor(int i) {
        frameLayout.setBackgroundColor(getResources().getColor(i));
    }

    protected abstract View createHeader();

    /**
     * 初始化成功
     *
     * @param data
     * @param b    是否还有第二页
     */
    public void initDataSuccess(List<T> data, boolean b) {
        if (tipsLayout != null)
            tipsLayout.onDone();
        if (frameLayout.isRefreshing()) {
            frameLayout.refreshComplete();
        }
        if (data == null || data.size() == 0) {
            commRecyclerAdapter.removeAll();
            initEmptyView();
        } else {
            removeEmptyView();
            commRecyclerAdapter.replaceAll(data);
        }
        frameLayout.setLoadMoreEnable(b);
    }

    public void initEmptyView() {
        if (emptyView == null) {
            emptyView = LayoutInflater.from(getContext()).inflate(R.layout.layout_filter_empty, null);
        }
        adapterWithHF.addFooter(emptyView);
    }

    public void removeEmptyView() {
        if (emptyView != null)
            adapterWithHF.removeFooter(emptyView);
    }

    public void initFooterEmptyView() {
        if (errorView == null) {
            errorView = LayoutInflater.from(getContext()).inflate(R.layout.layout_loading_error, null);
        }
        adapterWithHF.addFooter(errorView);
    }

    public void removeFooterEmptyView() {
        if (errorView != null)
            adapterWithHF.removeFooter(errorView);
    }

    /**
     * 加载更多
     *
     * @param data
     * @param b    是否还有更多数据
     */
    public void loadMoreDataSuccess(List<T> data, boolean b) {
        commRecyclerAdapter.addAll(data);
        frameLayout.loadMoreComplete(b);
    }

    /**
     * 初始化失败
     */
    public void initDataFail() {
        if (frameLayout.isRefreshing()) {
            frameLayout.refreshComplete();
        } else {
            tipsLayout.onError();
            tipsLayout.setErrorButtonListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tipsLayout.onLoading();
                    onRefresh();
                }
            });
        }
    }

    public void setPullRefreshEnable(boolean pullRefreshEnable) {
        if (pullRefreshEnable) {
            frameLayout.setPtrHandler(new PtrDefaultHandler() {
                @Override
                public void onRefreshBegin(PtrFrameLayout frame) {
                    onRefresh();
                }
            });
        } else {
            frameLayout.setPullRefreshEnable(false);
        }
    }

    /**
     * 加载更多失败
     */
    public void loadMoreDataFail() {
        frameLayout.loadMoreError();
    }

    public void showLoading() {
        tipsLayout.onLoading();
    }

    public void hideLoading() {
        tipsLayout.onDone();
    }

    protected abstract CommRecyclerAdapter createAdapter();

    protected abstract void onRefresh();

    protected abstract void onLoadMore();

    public void setHeaderView(View headerView) {
        this.headerView = headerView;
    }


}
