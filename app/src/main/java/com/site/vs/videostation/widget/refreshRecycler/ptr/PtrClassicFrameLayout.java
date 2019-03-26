package com.site.vs.videostation.widget.refreshRecycler.ptr;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.site.vs.videostation.widget.refreshRecycler.loadmore.DefaultLoadMoreViewFooter;
import com.site.vs.videostation.widget.refreshRecycler.loadmore.ILoadMoreViewFactory;


public class PtrClassicFrameLayout extends PtrFrameLayout {

    private PtrClassicDefaultHeader mPtrClassicHeader;

    public PtrClassicFrameLayout(Context context) {
        super(context);
        initViews();
    }

    public PtrClassicFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public PtrClassicFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews();
    }

    private void initViews() {
        mPtrClassicHeader = new PtrClassicDefaultHeader(getContext());
        setHeaderView(mPtrClassicHeader);
        addPtrUIHandler(mPtrClassicHeader);

        ILoadMoreViewFactory loadMoreViewFactory = new DefaultLoadMoreViewFooter();
        setFooterView(loadMoreViewFactory);

    }

    public PtrClassicDefaultHeader getHeader() {
        return mPtrClassicHeader;
    }

    /**
     * Specify the last update time by this key string
     *
     * @param key
     */
    public void setLastUpdateTimeKey(String key) {
        if (mPtrClassicHeader != null) {
            mPtrClassicHeader.setLastUpdateTimeKey(key);
        }
    }

    /**
     * Using an object to specify the last update time.
     *
     * @param object
     */
    public void setLastUpdateTimeRelateObject(Object object) {
        if (mPtrClassicHeader != null) {
            mPtrClassicHeader.setLastUpdateTimeRelateObject(object);
        }
    }


    /**
     * 下拉刷新是否可用
     *
     * @param b
     */
    public void setPullRefreshEnable(boolean b) {
        if (!b) {
            if (mPtrHandler == null) {
                mPtrHandler = new PtrDefaultHandler() {
                    @Override
                    public void onRefreshBegin(PtrFrameLayout frame) {

                    }

                    @Override
                    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                        return false;
                    }
                };
            }
        }
    }
}
