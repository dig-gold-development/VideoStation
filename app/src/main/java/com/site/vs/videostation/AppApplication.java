package com.site.vs.videostation;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.multidex.MultiDex;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.site.vs.videostation.db.DBManager;

/**
 * Created by yangang on 2018/1/19.
 */

public class AppApplication extends Application {
    public void onCreate() {
        super.onCreate();

        initFresco(this);
        initDatabase(this);

    }

    private void initFresco(Context base) {
        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(base).setIndexPopulateAtStartupEnabled(true).build();
        Fresco.initialize(base, ImagePipelineConfig.newBuilder(base).setDownsampleEnabled(true).setMainDiskCacheConfig(diskCacheConfig).build());
    }

    private void initDatabase(Context context) {
        DBManager.instance(context);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base); MultiDex.install(this);
    }
}
