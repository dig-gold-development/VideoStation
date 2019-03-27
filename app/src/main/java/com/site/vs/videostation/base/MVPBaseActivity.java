package com.site.vs.videostation.base;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;


import com.site.vs.videostation.R;

import butterknife.OnClick;
import butterknife.Optional;


/**
 * @author zhangbb
 * @date 2016/10/18
 */
public abstract class MVPBaseActivity<T extends BasePresenter> extends BaseActivity implements BaseView {

    public static final String TAG = "MVPBaseActivity";
    public T mPresenter;
    protected InputMethodManager inputMethodManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createPresenter();
        if (mPresenter != null)
            mPresenter.attachView(this);
    }

    protected abstract void createPresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.detachView();
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
    }

    /**
     * 隐藏软键盘
     */
    protected void hideKeyboard(View v) {
        if (inputMethodManager == null) {
            inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null && inputMethodManager.isActive())
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    /**
     * 显示键盘
     */
    protected void showKeyboard() {
        if (inputMethodManager == null) {
            inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE) {
            if (getCurrentFocus() != null)
                inputMethodManager.showSoftInput(getCurrentFocus(), InputMethodManager.SHOW_FORCED);
        }
    }

    @Optional
    @OnClick(R.id.iv_finish)
    public void finishClick() {
        finish();
    }
}
