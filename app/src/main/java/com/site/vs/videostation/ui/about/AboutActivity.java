package com.site.vs.videostation.ui.about;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.site.vs.videostation.R;
import com.site.vs.videostation.base.MVPBaseActivity;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author zhangbb
 * @date 2016/12/28
 */
public class AboutActivity extends MVPBaseActivity {

    @BindView(R.id.tv_content)
    public TextView contentTv;
    @BindView(R.id.tv_version)
    public TextView versionTv;
    @BindView(R.id.tv_cache)
    public TextView cacheTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_homepage);
        ButterKnife.bind(this);
        String appName = getResources().getString(R.string.app_name);
        contentTv.setText(getResources().getString(R.string.setting_content, appName, appName, appName, ""));
        try {
            String versionName = getPackageManager().getPackageInfo(
                    getPackageName(), 0).versionName;
            versionTv.setText(appName.concat(versionName));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        showCacheSize();
    }

    private void showCacheSize() {
        long cacheSize = Fresco.getImagePipelineFactory().getMainDiskStorageCache().getSize();
        if (cacheSize <= 0) {
            cacheTv.setText("0.00B");
        } else {
            float cacheSizeTemp1 = Math.round(cacheSize / 1024);
            float cacheSizeTemp2 = Math.round((cacheSize / 1024) / 1024);
            if (cacheSizeTemp1 < 1) {
                cacheTv.setText(cacheSize + "B");
            } else if (((cacheSizeTemp1 >= 1) && (cacheSizeTemp2 < 1))) {
                cacheTv.setText(cacheSizeTemp1 + "KB");
            } else if (cacheSizeTemp2 >= 1) {
                cacheTv.setText(cacheSizeTemp2 + "MB");
            }
        }
    }

    @OnClick(R.id.llayout_clear)
    public void CacheCleanClick() {
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        imagePipeline.clearCaches();
        showCacheSize();
    }

    @OnClick(R.id.tv_praise)
    public void PraiseClick() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void createPresenter() {

    }


}
