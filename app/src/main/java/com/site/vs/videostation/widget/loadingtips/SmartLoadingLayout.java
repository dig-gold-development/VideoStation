package com.site.vs.videostation.widget.loadingtips;

import android.app.Activity;
import android.content.Context;
import android.view.View;

public abstract class SmartLoadingLayout {

    public static CustomLoadingLayout createCustomLayout(Activity hostActivity) {
        return new CustomLoadingLayout(hostActivity);
    }

    public static DefaultLoadingLayout createDefaultLayout(Activity hostActivity, View contentView) {
        return new DefaultLoadingLayout(hostActivity, contentView);
    }

    public static DefaultLoadingLayout createDefaultLayout(Context hostActivity, View contentView) {
        return new DefaultLoadingLayout(hostActivity, contentView);
    }

    protected View mLoadingView;
    protected View mContentView;
    protected View mEmptyView;
    protected View mErrorView;
    protected boolean mLoadingAdded;
    protected boolean mEmptyAdded;
    protected boolean mErrorAdded;

    public abstract void onLoading();

    public abstract void onDone();

    public abstract void onEmpty();

    public abstract void onError();

    protected void showViewWithStatus(LayoutStatus status) {
        switch (status) {
            case Loading:
                if (mLoadingView == null) {
                    throw new NullPointerException("The loading view with this layout was not set");
                }
                mLoadingView.setVisibility(View.VISIBLE);
                if (mContentView != null)
                    mContentView.setVisibility(View.GONE);
                if (mEmptyView != null)
                    mEmptyView.setVisibility(View.GONE);
                if (mErrorView != null)
                    mErrorView.setVisibility(View.GONE);
                break;
            case Done:
                if (mContentView == null) {
                    throw new NullPointerException("The loading view with this layout was not set");
                }
                mContentView.setVisibility(View.VISIBLE);
                if (mLoadingView != null)
                    mLoadingView.setVisibility(View.GONE);
                if (mEmptyView != null)
                    mEmptyView.setVisibility(View.GONE);
                if (mErrorView != null)
                    mErrorView.setVisibility(View.GONE);
                break;
            case Empty:
                if (mEmptyView == null) {
                    throw new NullPointerException("The loading view with this layout was not set");
                }
                mEmptyView.setVisibility(View.VISIBLE);
                if (mLoadingView != null)
                    mLoadingView.setVisibility(View.GONE);
                if (mContentView != null)
                    mContentView.setVisibility(View.GONE);
                if (mErrorView != null)
                    mErrorView.setVisibility(View.GONE);
                break;
            case Error:
                if (mErrorView == null) {
                    throw new NullPointerException("The loading view with this layout was not set");
                }
                mErrorView.setVisibility(View.VISIBLE);
                if (mLoadingView != null)
                    mLoadingView.setVisibility(View.GONE);
                if (mContentView != null)
                    mContentView.setVisibility(View.GONE);
                if (mEmptyView != null)
                    mEmptyView.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }


    protected enum LayoutStatus {
        Loading, Done, Empty, Error
    }
}
