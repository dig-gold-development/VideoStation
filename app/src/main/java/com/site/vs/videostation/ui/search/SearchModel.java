package com.site.vs.videostation.ui.search;


import com.site.vs.videostation.api.SearchService;
import com.site.vs.videostation.entity.HotSearchEntity;
import com.site.vs.videostation.entity.SearchResultEntity;
import com.site.vs.videostation.http.Retrofits;
import com.zhusx.core.network.HttpResult;

import rx.Observable;

/**
 * @author zhangbb
 * @date 2016/12/19
 */
public class SearchModel implements SearchPageContract.Model {

    @Override
    public Observable<HttpResult<SearchResultEntity>> getSearchResult(String key, int type, int page) {
        return Retrofits.createApi(SearchService.class).getSearchResult(key, type, page);
    }

    @Override
    public Observable<HttpResult<HotSearchEntity>> getHotSearch() {
        return Retrofits.createApi(SearchService.class).getHotSearch();
    }
}
