package com.site.vs.videostation.base;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;


import com.site.vs.videostation.adapter.base.CommRecyclerAdapter;
import com.site.vs.videostation.widget.loadingtips.DefaultLoadingLayout;
import com.site.vs.videostation.widget.loadingtips.SmartLoadingLayout;
import com.site.vs.videostation.widget.refreshRecycler.RecyclerAdapterWithHF;
import com.site.vs.videostation.widget.refreshRecycler.ptr.OnLoadMoreListener;
import com.site.vs.videostation.widget.refreshRecycler.ptr.PtrClassicFrameLayout;
import com.site.vs.videostation.widget.refreshRecycler.ptr.PtrDefaultHandler;
import com.site.vs.videostation.widget.refreshRecycler.ptr.PtrFrameLayout;

import java.util.List;

/**
 * @author dxplay120
 * @date 2016/11/11
 */
public class RecyclerViewHelper<T> {

    private LoadingAndRetryAdapter helperAdapter;
    public DefaultLoadingLayout tipsLayout;
    public CommRecyclerAdapter adapter;
    private RecyclerAdapterWithHF adapterWithHF;

    public void initViews(Activity activity, final LoadingAndRetryAdapter helperAdapter) {
        this.helperAdapter = helperAdapter;
        if (isHelperAdapter(helperAdapter) && castToHelperAdapter(helperAdapter).getPtrFrameLayout() != null)
            tipsLayout = SmartLoadingLayout.createDefaultLayout(activity, getPtrFrameLayout(helperAdapter));
        else
            tipsLayout = SmartLoadingLayout.createDefaultLayout(activity, helperAdapter.getRecyclerView());
        tipsLayout.onLoading();
        adapter = helperAdapter.createAdapter();
        if (isHelperAdapter(helperAdapter))
            adapterWithHF = castToHelperAdapter(helperAdapter).createAdapterHF(adapter);

        if (adapterWithHF == null)
            helperAdapter.getRecyclerView().setAdapter(adapter);
        else
            helperAdapter.getRecyclerView().setAdapter(adapterWithHF);

        if (getPtrFrameLayout(helperAdapter) != null) {
            getPtrFrameLayout(helperAdapter).setPtrHandler(new PtrDefaultHandler() {
                @Override
                public void onRefreshBegin(PtrFrameLayout frame) {
                    helperAdapter.onRefresh();
                }
            });
            getPtrFrameLayout(helperAdapter).setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void loadMore() {
                    castToHelperAdapter(helperAdapter).onLoadMore();
                }
            });
        }
    }

    /**
     * 初始化成功
     */
    public void initDataSuccess(List<T> data) {
        tipsLayout.onDone();
        if (getPtrFrameLayout(helperAdapter) != null) {
            if (getPtrFrameLayout(helperAdapter).isRefreshing()) {
                getPtrFrameLayout(helperAdapter).refreshComplete();
            }
            getPtrFrameLayout(helperAdapter).setLoadMoreEnable(true);
        }
        adapter.replaceAll(data);

        if (data.size() == 0)
            tipsLayout.onEmpty();
    }

    /**
     * 初始化成功
     */
    public void initDataSuccess(List<T> data, int pageCount) {
        initDataSuccess(data);

        if (pageCount <= 1 && isHelperAdapter(helperAdapter))
            getPtrFrameLayout(helperAdapter).setLoadMoreEnable(false);
    }
    /**
     * 加载更多成功
     *
     * @param
     */
    public void loadMoreDataSuccess(List<T> data, boolean b) {
        adapter.addAll(data);
        if (isHelperAdapter(helperAdapter))
            getPtrFrameLayout(helperAdapter).loadMoreComplete(b);
    }

    /**
     * 初始化失败
     */
    public void initDataFail() {
        if (getPtrFrameLayout(helperAdapter) != null && getPtrFrameLayout(helperAdapter).isRefreshing()) {
            getPtrFrameLayout(helperAdapter).refreshComplete();
        } else {
            tipsLayout.onError();
            tipsLayout.setErrorButtonListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tipsLayout.onLoading();
                    helperAdapter.onRefresh();
                }
            });
        }
    }

    /**
     * 加载更多失败
     */
    public void loadMoreDataFail() {
        getPtrFrameLayout(helperAdapter).loadMoreError();
    }

    public void showLoading() {
        tipsLayout.onLoading();
    }
    public void hideLoading() {
        tipsLayout.onDone();
    }
    public void replaceEmptyIcon(int resId){
        tipsLayout.replaceEmptyIcon(resId);
    }

    public void setEmptyDescription(String desc){
        tipsLayout.setEmptyDescription(desc);
    }

    private boolean isHelperAdapter(LoadingAndRetryAdapter helperAdapter) {
        return helperAdapter instanceof HelperAdapter;
    }

    private HelperAdapter castToHelperAdapter(LoadingAndRetryAdapter helperAdapter) {
        return (HelperAdapter) helperAdapter;
    }

    private PtrClassicFrameLayout getPtrFrameLayout(LoadingAndRetryAdapter helperAdapter) {
        if (isHelperAdapter(helperAdapter))
            return castToHelperAdapter(helperAdapter).getPtrFrameLayout();
        else
            return null;
    }

    public interface LoadingAndRetryAdapter {
        CommRecyclerAdapter createAdapter();

        RecyclerView getRecyclerView();

        void onRefresh();
    }

    public interface HelperAdapter extends LoadingAndRetryAdapter {
        void onLoadMore();

        RecyclerAdapterWithHF createAdapterHF(CommRecyclerAdapter adapter);

        PtrClassicFrameLayout getPtrFrameLayout();

    }


}
