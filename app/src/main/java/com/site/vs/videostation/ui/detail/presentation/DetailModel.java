package com.site.vs.videostation.ui.detail.presentation;



import com.site.vs.videostation.api.ApiService;
import com.site.vs.videostation.entity.DetailEntity;
import com.site.vs.videostation.http.Retrofits;
import com.zhusx.core.network.HttpResult;

import rx.Observable;

/**
 * @author dxplay120
 * @date 2016/12/17
 */
public class DetailModel implements DetailContract.Model {
    @Override
    public Observable<HttpResult<DetailEntity>> getDetail(String id) {
        return Retrofits.createApi(ApiService.class).detail(id);
    }
}
