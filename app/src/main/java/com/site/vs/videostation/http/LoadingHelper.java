package com.site.vs.videostation.http;

import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.site.vs.videostation.R;
import com.zhusx.core.helper.Lib_LoadingHelper;
import com.zhusx.core.network.HttpRequest;
import com.zhusx.core.network.HttpResult;

/**
 * 处理第一次进入页面时,显示加载信息 (正在加载,加载失败)
 * Author        zhusx
 * Email         327270607@qq.com
 * Created       2016/2/16 16:33
 */
public abstract class LoadingHelper<T> extends Lib_LoadingHelper<LoadData.Api, T, Object> {

    public LoadingHelper(View resView, final LoadData<T> loadData) {
        super(resView);

        View view = LayoutInflater.from(resView.getContext()).inflate(R.layout.layout_loading, null, false);
        //设置Loading 视图
        View loadingView = view.findViewById(R.id.layout_loading);
        _setLoadingView(loadingView);
        ImageView iv = (ImageView) loadingView.findViewById(R.id.iv_loading);
        _setLoadingAnim((AnimationDrawable) iv.getDrawable());
        View errorView = view.findViewById(R.id.layout_error);
        //设置 失败后,重试
        errorView.findViewById(R.id.btn_retry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData._reLoadData();
            }
        });
        //设置失败 视图
        _setErrorView(errorView);

    }

    @Override
    public void __onError(View errorView, HttpRequest<Object> request, HttpResult<T> data, boolean isAPIError, String error_message) {
        ((TextView) errorView.findViewById(R.id.tv_errorMessage)).setText(error_message);
    }
}
