package com.site.vs.videostation.ui.detail.presentation;



import com.site.vs.videostation.api.ApiService;
import com.site.vs.videostation.entity.MoveAddressEntity;
import com.site.vs.videostation.http.Retrofits;
import com.zhusx.core.network.HttpResult;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * @author dxplay120
 * @date 2016/12/26
 */
public class PlayModel implements PlayContract.Model {
    @Override
    public Observable<MoveAddressEntity> playMove(String url, String type) {

        OkHttpClient.Builder client = new OkHttpClient().newBuilder();

        return new Retrofit.Builder()
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl("http://vip.biggold.net.cn/")
                .build().create(ApiService.class).setAddress(url, type);
    }
}
