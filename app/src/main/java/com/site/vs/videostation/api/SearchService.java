package com.site.vs.videostation.api;


import com.site.vs.videostation.entity.HotSearchEntity;
import com.site.vs.videostation.entity.SearchResultEntity;
import com.zhusx.core.network.HttpResult;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author zhangbb
 * @date 2016/12/19
 */
public interface SearchService {

    /**
     * @param key
     * @param type  类型(1电影,2电视剧,3动漫,4综艺)
     * @param pages
     * @return
     */
    @FormUrlEncoded
    @POST("search")
    Observable<HttpResult<SearchResultEntity>> getSearchResult(@Field("keywords") String key,
                                                               @Field("type") int type,
                                                               @Field("page") int pages);


    /**
     * search
     */
    @GET("search/hot")
    Observable<HttpResult<HotSearchEntity>> getHotSearch();
}
