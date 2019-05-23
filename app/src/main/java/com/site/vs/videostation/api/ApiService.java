package com.site.vs.videostation.api;




import com.site.vs.videostation.entity.ChannelEntity;
import com.site.vs.videostation.entity.DetailEntity;
import com.site.vs.videostation.entity.HomePageEntity;
import com.site.vs.videostation.entity.MoveAddressEntity;
import com.site.vs.videostation.entity.RankingEntity;
import com.site.vs.videostation.http.JSONResult;
import com.zhusx.core.network.HttpResult;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by yangang on 2018/1/18.
 */

public interface ApiService {

    /**
     * 首页
     */
    @GET("home")
    Observable<JSONResult<HomePageEntity>> getHomePage();

    /**
     * 频道
     */
    @GET("channel/getChannels")
    Observable<JSONResult<ChannelEntity>> channel();

    /**
     * 排行榜
     */
    @POST("rank/getRankById")
    @FormUrlEncoded
    Observable<JSONResult<RankingEntity>> top(@Field("type")String type, @Field("page")String pages);

    /**
     * 详情
     */
    @GET("detail/getDetailById")
    Observable<HttpResult<DetailEntity>> detail(@Query("id") String id);

    /**
     * 解析视频地址
     */
    @POST("/play/setAddress/")
    @FormUrlEncoded
    Observable<HttpResult<MoveAddressEntity>> setAddress(@Field("url") String url, @Field("type") String type);

}
