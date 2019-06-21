package com.site.vs.videostation.http;

import com.site.vs.videostation.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Author        zhusx
 * Email         327270607@qq.com
 * Created       2016/12/16 13:52
 */
public class Retrofits {

    private volatile static Retrofit retrofit;
    private static final String URL = "http://www.biggold.net.cn:8080/videostation/";

    static {
        synchronized (Retrofits.class) {
            OkHttpClient.Builder client = new OkHttpClient().newBuilder();

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            if (BuildConfig.BUILD_TYPE.equals("debug")) {
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            } else {
                logging.setLevel(HttpLoggingInterceptor.Level.NONE);
            }

            client.addInterceptor(logging);
//            client.addNetworkInterceptor(new StethoInterceptor());
            retrofit = new Retrofit.Builder()
                    .client(client.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .baseUrl(URL)
                    .build();
        }
    }

    /**
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T createApi(Class<T> clazz) {
        return retrofit.create(clazz);
    }
}
