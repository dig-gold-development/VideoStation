package com.site.vs.videostation;

import android.app.ActivityManager;
import android.content.Context;
import androidx.multidex.MultiDex;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.site.vs.videostation.base.BaseApp;
import com.site.vs.videostation.db.DBManager;
import com.site.vs.videostation.kit.WfcUIKit;
import com.site.vs.videostation.kit.conversation.message.viewholder.MessageViewHolderManager;
import com.site.vs.videostation.third.location.viewholder.LocationMessageContentViewHolder;
import com.tencent.bugly.crashreport.CrashReport;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by yangang on 2018/1/19.
 */

public class AppApplication extends BaseApp {

    private WfcUIKit wfcUIKit;

    public void onCreate() {
        super.onCreate();

        initFresco(this);
        initDatabase(this);
//        Stetho.initializeWithDefaults(this);

        CrashReport.initCrashReport(getApplicationContext(), "86f84cfd59", false);


        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(true)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(5)         // (Optional) How many method line to show. Default 2
                .tag("video_Logger")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build();
        com.orhanobut.logger.Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));


//        LeakCanary.install(this);
//        BlockCanary.install(this, new AppBlockCanaryContext()).start();

        closeAndroidPDialog();

        // 只在主进程初始化
        if (getCurProcessName(this).equals("com.site.vs.videostation")) {
            wfcUIKit = new WfcUIKit();
            wfcUIKit.init(this);
            MessageViewHolderManager.getInstance().registerMessageViewHolder(LocationMessageContentViewHolder.class);
        }
    }

    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
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


    private void closeAndroidPDialog(){
        try {
            Class aClass = Class.forName("android.content.pm.PackageParser$Package");
            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
            declaredMethod.setAccessible(true);
            Object activityThread = declaredMethod.invoke(null);
            Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
