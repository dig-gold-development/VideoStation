package com.site.vs.videostation.widget.loadingtips;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.site.vs.videostation.R;


public class DefaultLoadingLayout extends SmartLoadingLayout {
    private LayoutInflater mInflater;
    private Context mContext;
    private RelativeLayout rlAddedView;
    private boolean mAnyAdded;
    private RelativeLayout.LayoutParams mLayoutParams;

    private TextView tvLoadingDescription;
    private TextView tvEmptyDescription;
    private TextView tvErrorDescription;
    private Button btnErrorHandle;
    private LinearLayout mLoadingContent;

    DefaultLoadingLayout(Context context, View contentView) {
        this.mContext = context;
        this.mContentView = contentView;
        this.mInflater = LayoutInflater.from(context);

        mLoadingView = mInflater.inflate(R.layout.layout_loading_default, null);
        mEmptyView = mInflater.inflate(R.layout.layout_loading_empty, null);
        mErrorView = mInflater.inflate(R.layout.layout_loading_error, null);
        mLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                                                        RelativeLayout.LayoutParams.MATCH_PARENT);
        mLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);

    }

    public void setLoadingDescriptionColor(int color) {
        if (tvLoadingDescription == null)
            tvLoadingDescription = (TextView) mLoadingView.findViewById(R.id.tv_loading_message);

        tvLoadingDescription.setTextColor(color);
    }

    public void setLoadingDescriptionTextSize(float size) {
        if (tvLoadingDescription == null)
            tvLoadingDescription = (TextView) mLoadingView.findViewById(R.id.tv_loading_message);

        tvLoadingDescription.setTextSize(size);
    }

    public void setLoadingDescription(String loadingDescription) {
        if (tvLoadingDescription == null)
            tvLoadingDescription = (TextView) mLoadingView.findViewById(R.id.tv_loading_message);

        tvLoadingDescription.setText(loadingDescription);
    }

    public void setLoadingDescription(int resID) {
        if (tvLoadingDescription == null)
            tvLoadingDescription = (TextView) mLoadingView.findViewById(R.id.tv_loading_message);

        tvLoadingDescription.setText(resID);
    }

    public void replaceLoadingProgress(View view) {
        if (mLoadingContent == null) mLoadingContent = (LinearLayout) mLoadingView.findViewById(R.id.ll_loading);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                                                                         RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        ((RelativeLayout) mLoadingView).addView(view, lp);
        ((RelativeLayout) mLoadingView).removeView(mLoadingContent);
    }

    public void setEmptyDescriptionColor(int color) {
        if (tvEmptyDescription == null) tvEmptyDescription = (TextView) mEmptyView.findViewById(R.id.tv_empty_message);

        tvEmptyDescription.setTextColor(color);
    }

    public void setEmptyDescriptionTextSize(float size) {
        if (tvEmptyDescription == null) tvEmptyDescription = (TextView) mEmptyView.findViewById(R.id.tv_empty_message);

        tvEmptyDescription.setTextSize(size);
    }

    public void setEmptyDescription(String emptyDescription) {
        if (tvEmptyDescription == null) tvEmptyDescription = (TextView) mEmptyView.findViewById(R.id.tv_empty_message);

        tvEmptyDescription.setText(emptyDescription);
    }

    public void setEmptyDescription(int resID) {
        if (tvEmptyDescription == null) tvEmptyDescription = (TextView) mEmptyView.findViewById(R.id.tv_empty_message);

        tvEmptyDescription.setText(resID);
    }

    public void replaceEmptyIcon(Drawable newIcon) {
        if (tvEmptyDescription == null) tvEmptyDescription = (TextView) mEmptyView.findViewById(R.id.tv_empty_message);

        newIcon.setBounds(0, 0, newIcon.getMinimumWidth(), newIcon.getMinimumHeight());
        tvEmptyDescription.setCompoundDrawables(null, newIcon, null, null);
    }

    public void replaceEmptyIcon(int resId) {
        if (tvEmptyDescription == null) tvEmptyDescription = (TextView) mEmptyView.findViewById(R.id.tv_empty_message);

        Drawable newIcon = mContext.getResources().getDrawable(resId);
        newIcon.setBounds(0, 0, newIcon.getMinimumWidth(), newIcon.getMinimumHeight());
        tvEmptyDescription.setCompoundDrawables(null, newIcon, null, null);
    }

    public void setErrorDescriptionColor(int color) {
        if (tvErrorDescription == null) tvErrorDescription = (TextView) mErrorView.findViewById(R.id.tv_error_message);

        tvErrorDescription.setTextColor(color);
    }

    public void setErrorDescriptionTextSize(float size) {
        if (tvErrorDescription == null) tvErrorDescription = (TextView) mErrorView.findViewById(R.id.tv_error_message);

        tvErrorDescription.setTextSize(size);
    }

    public void setErrorDescription(String errorDescription) {
        if (tvErrorDescription == null) tvErrorDescription = (TextView) mErrorView.findViewById(R.id.tv_error_message);

        tvErrorDescription.setText(errorDescription);
    }

    public void setErrorDescription(int resID) {
        if (tvErrorDescription == null) tvErrorDescription = (TextView) mErrorView.findViewById(R.id.tv_error_message);

        tvErrorDescription.setText(resID);
    }

    public void setErrorButtonListener(View.OnClickListener listener) {
        if (btnErrorHandle == null) btnErrorHandle = (Button) mErrorView.findViewById(R.id.btn_error);

        btnErrorHandle.setOnClickListener(listener);
    }


    public void setErrorButtonBackground(Drawable background) {
        if (btnErrorHandle == null) btnErrorHandle = (Button) mErrorView.findViewById(R.id.btn_error);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            btnErrorHandle.setBackground(background);
        } else {
            //noinspection deprecation
            btnErrorHandle.setBackgroundDrawable(background);
        }
    }

    public void setErrorButtonBackground(int resID) {
        if (btnErrorHandle == null) btnErrorHandle = (Button) mErrorView.findViewById(R.id.btn_error);

        btnErrorHandle.setBackgroundResource(resID);

    }

    public void setErrorButtonTextColor(int color) {
        if (btnErrorHandle == null) btnErrorHandle = (Button) mErrorView.findViewById(R.id.btn_error);

        btnErrorHandle.setTextColor(color);
    }

    public void setErrorButtonText(String text) {
        if (btnErrorHandle == null) btnErrorHandle = (Button) mErrorView.findViewById(R.id.btn_error);

        btnErrorHandle.setText(text);
    }

    public void setErrorButtonText(int resID) {
        if (btnErrorHandle == null) btnErrorHandle = (Button) mErrorView.findViewById(R.id.btn_error);

        btnErrorHandle.setText(resID);
    }

    public void hideErrorButton() {
        if (btnErrorHandle == null) btnErrorHandle = (Button) mErrorView.findViewById(R.id.btn_error);

        btnErrorHandle.setVisibility(View.GONE);
    }

    public void replaceErrorButton(Button newButton) {
        if (btnErrorHandle == null) btnErrorHandle = (Button) mErrorView.findViewById(R.id.btn_error);

        ((RelativeLayout) mErrorView).removeView(btnErrorHandle);
        btnErrorHandle = newButton;
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                                                                         RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        lp.addRule(RelativeLayout.BELOW, R.id.tv_error_message);
        lp.topMargin = 20;
        ((RelativeLayout) mErrorView).addView(btnErrorHandle, lp);
    }

    public void replaceErrorIcon(Drawable newIcon) {
        if (tvErrorDescription == null) tvErrorDescription = (TextView) mErrorView.findViewById(R.id.tv_error_message);

        newIcon.setBounds(0, 0, newIcon.getMinimumWidth(), newIcon.getMinimumHeight());
        tvErrorDescription.setCompoundDrawables(null, newIcon, null, null);
    }


    public void replaceErrorIcon(int resId) {
        if (tvErrorDescription == null) tvErrorDescription = (TextView) mErrorView.findViewById(R.id.tv_error_message);

        Drawable newIcon = mContext.getResources().getDrawable(resId);
        newIcon.setBounds(0, 0, newIcon.getMinimumWidth(), newIcon.getMinimumHeight());
        tvErrorDescription.setCompoundDrawables(null, newIcon, null, null);
    }

    public void setLayoutBackgroundColor(int color) {
        initAddedLayout();
        rlAddedView.setBackgroundColor(color);
    }

    public void setLayoutBackground(int resID) {
        initAddedLayout();
        rlAddedView.setBackgroundResource(resID);
    }

    @Override public void onLoading() {
        checkContentView();
        if (!mLoadingAdded) {
            initAddedLayout();

            if (mLoadingView != null) {
                rlAddedView.addView(mLoadingView, mLayoutParams);
                mLoadingAdded = true;
            }
        }
        showViewWithStatus(LayoutStatus.Loading);
    }

    @Override public void onDone() {
        checkContentView();
        showViewWithStatus(LayoutStatus.Done);
    }

    @Override public void onEmpty() {
        checkContentView();
        if (!mEmptyAdded) {
            initAddedLayout();

            if (mEmptyView != null) {
                rlAddedView.addView(mEmptyView, mLayoutParams);
                mEmptyAdded = true;
            }
        }
        showViewWithStatus(LayoutStatus.Empty);
    }

    @Override public void onError() {
        checkContentView();
        if (!mErrorAdded) {
            initAddedLayout();

            if (mErrorView != null) {
                rlAddedView.addView(mErrorView, mLayoutParams);
                mErrorAdded = true;
            }
        }
        showViewWithStatus(LayoutStatus.Error);
    }


    private void checkContentView() {
        if (mContentView == null) throw new NullPointerException("The content view not set..");
    }

    private void initAddedLayout() {
        if (!mAnyAdded) {
            rlAddedView = new RelativeLayout(mContext);
            rlAddedView.setLayoutParams(mLayoutParams);
            ViewGroup parent = (ViewGroup) mContentView.getParent();
            parent.addView(rlAddedView);
            mAnyAdded = true;
        }
    }

}
