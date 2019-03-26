package com.site.vs.videostation;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.multidex.MultiDex;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

/**
 * Created by yangang on 2018/1/19.
 */

public class AppApplication extends Application {
    public void onCreate() {
        super.onCreate();
        //初始化Fresco
        Fresco.initialize(this, ImagePipelineConfig.newBuilder(this)
                .setDownsampleEnabled(true)
                .setBitmapsConfig(Bitmap.Config.ARGB_8888)
                .build());

    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base); MultiDex.install(this);
    }
}
