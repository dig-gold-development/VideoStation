package com.site.vs.videostation.http;

import android.app.Activity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;


import com.site.vs.videostation.widget.loadingtips.DefaultLoadingLayout;
import com.site.vs.videostation.widget.loadingtips.SmartLoadingLayout;
import com.site.vs.videostation.widget.refreshRecycler.RecyclerAdapterWithHF;
import com.site.vs.videostation.widget.refreshRecycler.ptr.OnLoadMoreListener;
import com.site.vs.videostation.widget.refreshRecycler.ptr.PtrClassicFrameLayout;
import com.site.vs.videostation.widget.refreshRecycler.ptr.PtrDefaultHandler;
import com.site.vs.videostation.widget.refreshRecycler.ptr.PtrFrameLayout;
import com.zhusx.core.interfaces.IChangeAdapter;
import com.zhusx.core.interfaces.IPageData;
import com.zhusx.core.network.HttpRequest;
import com.zhusx.core.network.HttpResult;
import com.zhusx.core.network.OnHttpLoadingListener;
import com.zhusx.core.utils._Lists;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2016/11/10 9:40
 */

public class PtrLoadingListener<T extends IPageData> extends PtrClassicFrameLayout implements OnHttpLoadingListener<LoadData.Api, HttpResult<T>, Object> {
    private LoadData<T> mLoadData;
    private RecyclerAdapterWithHF adapterWithHF;
    private IChangeAdapter adapter;
    private DefaultLoadingLayout tipsLayout;

    public PtrLoadingListener(final RecyclerView recyclerView, final LoadData<T> loadData) {
        super(recyclerView.getContext());
        this.mLoadData = loadData;
        ViewGroup rootView = (ViewGroup) recyclerView.getParent();
        int index = rootView.indexOfChild(recyclerView);
        rootView.addView(this, index, recyclerView.getLayoutParams());
        rootView.removeView(recyclerView);
        addView(recyclerView);
        onFinishInflate();

        if (recyclerView.getAdapter() instanceof IChangeAdapter) {
            adapter = (IChangeAdapter) recyclerView.getAdapter();
        }
        adapterWithHF = new RecyclerAdapterWithHF(recyclerView.getAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(adapterWithHF);
//        if (loadData._getRequestID() == LoadData.Api.排行榜) {
//            setPullRefreshEnable(false);
//        } else {
        setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mLoadData._reLoadData(true);
            }
        });
//        }

        setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                loadData._reLoadData(false);
            }
        });
        tipsLayout = SmartLoadingLayout.createDefaultLayout((Activity) recyclerView.getContext(), this);
    }

    @Override
    public void onLoadStart(LoadData.Api api, HttpRequest<Object> request) {
        if (request.isRefresh) {
            if (!mLoadData._hasCache()) {
                tipsLayout.onLoading();
            }
        }
    }

    @Override
    public void onLoadError(LoadData.Api api, HttpRequest<Object> request, HttpResult<T> tHttpResult, boolean b, String s) {
        if (request.isRefresh) {
            if (!mLoadData._hasCache()) {
                tipsLayout.onError();
                tipsLayout.setErrorDescription(s);
                tipsLayout.setErrorButtonListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mLoadData._reLoadData(true);
                    }
                });
            } else {
                if (isRefreshing()) {
                    refreshComplete();
                }
            }
        } else {
            loadMoreError();
        }
    }

    @Override
    public void onLoadComplete(LoadData.Api api, HttpRequest<Object> request, HttpResult<T> data) {
        if (request.isRefresh) {
            if (isRefreshing()) {
                refreshComplete();
            } else {
                tipsLayout.onDone();
            }
            if (_Lists.isEmpty(data.getData().getListData())) {
                tipsLayout.onEmpty();
                String message = null;
                int icon = -1;
                switch (api) {
                    case Rank:
//                        message = "购物车空空如也,快去挑选商品吧.";
//                        icon = R.drawable.shoppingcart_empty;
                        break;
                }
                if (message != null) {
                    tipsLayout.setEmptyDescription(message);
                }
                if (icon != -1) {
                    tipsLayout.replaceEmptyIcon(icon);
                }
            }
            setLoadMoreEnable(mLoadData.hasMoreData());
            if (adapter != null) {
                adapter._setItemToUpdate(data.getData().getListData());
            }
        } else {
            if (adapter != null) {
                adapter._addItemToUpdate(data.getData().getListData());
            }
            loadMoreComplete(mLoadData.hasMoreData());
        }
    }
}
