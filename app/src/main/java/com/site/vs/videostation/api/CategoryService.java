package com.site.vs.videostation.api;


import com.site.vs.videostation.entity.CategoryDetailEntity;
import com.site.vs.videostation.http.JSONResult;
import com.zhusx.core.network.HttpResult;

import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author zhangbb
 * @date 2016/12/19
 */
public interface CategoryService {

    /**
     * search
     */
    @FormUrlEncoded
    @POST("category/list")
    Observable<JSONResult<CategoryDetailEntity>> getCategoryBy(@Field("tid") String id,
                                                               @FieldMap Map<String, String> options,
                                                               @Field("page") int pages);
}
