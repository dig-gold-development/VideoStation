package com.site.vs.videostation.ui.video;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ProgressBar;
import android.widget.TextView;


import com.site.vs.videostation.R;
import com.site.vs.videostation.base.BaseActivity;
import com.site.vs.videostation.webview.X5WebView;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author dxplay120
 * @date 2016/12/20
 */
public class WebVideoActivity extends BaseActivity {
    public static final String URL="url";

    @BindView(R.id.web)
    X5WebView webView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.tv_title)
    TextView titleTv;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_video);
        ButterKnife.bind(this);
        String url = getIntent().getStringExtra(URL);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView webView, int i) {
                super.onProgressChanged(webView, i);
                if (progressBar != null) {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(i);
                    if (i == 100) {
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onReceivedTitle(WebView webView, String s) {
                titleTv.setText(s);
                super.onReceivedTitle(webView, s);
            }
        });
        webView.setWebViewClient(null);
        webView.loadUrl(url);
    }

    @OnClick({R.id.iv_back})
    void onClick(){
        finish();
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (webView != null)
            webView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (webView != null)
            webView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            ViewGroup parent = (ViewGroup) webView.getParent();
            if (parent != null)
                parent.removeView(webView);
            webView.removeAllViews();
            webView.destroy();
        }
    }

}
