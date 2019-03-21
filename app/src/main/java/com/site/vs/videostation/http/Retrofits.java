package com.site.vs.videostation.http;

import com.zhusx.core.debug.LogUtil;

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
    private static final String URL = "http://www.guaiguaiyingshi.com/TP5/public/index.php/";

    static {
        synchronized (Retrofits.class) {
            OkHttpClient.Builder client = new OkHttpClient().newBuilder();
            if (true) {
                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                client.addInterceptor(logging);
            }
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
