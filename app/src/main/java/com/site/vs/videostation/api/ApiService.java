package com.site.vs.videostation.api;




import com.site.vs.videostation.entity.HomePageEntity;
import com.site.vs.videostation.entity.RankingEntity;
import com.site.vs.videostation.http.JSONResult;
import com.zhusx.core.network.HttpResult;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by yangang on 2018/1/18.
 */

public interface ApiService {

    /**
     * 首页
     */
    @GET("ysapi/v1.HomePage/getHomeData")
    Observable<JSONResult<HomePageEntity>> getHomePage();

    /**
     * 排行榜
     */
    @POST("ysapi/v1.Rank/getRankByid")
    @FormUrlEncoded
    Observable<JSONResult<RankingEntity>> top(@Field("typeid")String type, @Field("page")String pages);
}
