package com.site.vs.videostation;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.stetho.Stetho;
import com.github.moduth.blockcanary.BlockCanary;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.site.vs.videostation.db.DBManager;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by yangang on 2018/1/19.
 */

public class AppApplication extends Application {
    public void onCreate() {
        super.onCreate();

        initFresco(this);
        initDatabase(this);
        Stetho.initializeWithDefaults(this);



        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(true)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(5)         // (Optional) How many method line to show. Default 2
                .tag("video_Logger")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build();
        com.orhanobut.logger.Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));


        LeakCanary.install(this);
        BlockCanary.install(this, new AppBlockCanaryContext()).start();

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
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
