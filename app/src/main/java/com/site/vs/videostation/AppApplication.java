package com.site.vs.videostation;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.multidex.MultiDex;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.stetho.Stetho;

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

        Stetho.initializeWithDefaults(this);


    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base); MultiDex.install(this);
    }
}
