package com.site.vs.videostation.base;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

/**
 * @author zhangbb
 * @date 2016/10/18
 */
public abstract class MVPBaseFragment<T extends BasePresenter> extends BaseFragment
        implements BaseView {

    public static final String TAG = "MVPBaseActivity";
    public T mPresenter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        createPresenter();
        mPresenter.attachView(this);
    }

    protected abstract void createPresenter();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null)
            mPresenter.detachView();
    }

    @Override
    public void showLoading() {
        Log.d(TAG, "show");
    }

    @Override
    public void hideLoading() {
        Log.d(TAG, "hide");
    }
}
