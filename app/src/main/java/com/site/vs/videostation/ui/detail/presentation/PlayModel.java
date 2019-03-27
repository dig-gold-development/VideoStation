package com.site.vs.videostation.ui.detail.presentation;



import com.site.vs.videostation.api.ApiService;
import com.site.vs.videostation.entity.MoveAddressEntity;
import com.site.vs.videostation.http.Retrofits;
import com.zhusx.core.network.HttpResult;

import rx.Observable;

/**
 * @author dxplay120
 * @date 2016/12/26
 */
public class PlayModel implements PlayContract.Model {
    @Override
    public Observable<HttpResult<MoveAddressEntity>> playMove(String url, String type) {
        return Retrofits.createApi(ApiService.class).setAddress(url, type);
    }
}
