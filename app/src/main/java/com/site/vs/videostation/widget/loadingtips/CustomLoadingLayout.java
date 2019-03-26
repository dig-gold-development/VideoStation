package com.site.vs.videostation.widget.loadingtips;

import android.app.Activity;
import android.view.View;

public class CustomLoadingLayout extends SmartLoadingLayout {

    private Activity mHostActivity;

    CustomLoadingLayout(Activity activity) {
        mHostActivity = activity;
    }

    public void setLoadingView(int viewID) {
        mLoadingView = mHostActivity.findViewById(viewID);
        mLoadingView.setVisibility(View.GONE);
    }

    public void setContentView(int viewID) {
        mContentView = mHostActivity.findViewById(viewID);
        mContentView.setVisibility(View.VISIBLE);
    }

    public void setEmptyView(int viewID) {
        mEmptyView = mHostActivity.findViewById(viewID);
        mEmptyView.setVisibility(View.GONE);
    }

    public void setErrorView(int viewID) {
        mErrorView = mHostActivity.findViewById(viewID);
        mErrorView.setVisibility(View.GONE);
    }

    public void onLoading() {
        showViewWithStatus(LayoutStatus.Loading);
    }

    public void onDone() {
        showViewWithStatus(LayoutStatus.Done);
    }

    public void onEmpty() {
        showViewWithStatus(LayoutStatus.Empty);
    }

    public void onError() {
        showViewWithStatus(LayoutStatus.Error);
    }

}
