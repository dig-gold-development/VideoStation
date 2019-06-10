package com.site.vs.videostation.ui.search;


import com.site.vs.videostation.entity.SearchResultEntity;
import com.zhusx.core.network.HttpResult;

import rx.Subscriber;

/**
 * @author zhangbb
 * @date 2016/12/19
 */
public class SearchPresenter extends SearchPageContract.SearchPresenter {

    @Override
    protected SearchModel createModel() {
        return new SearchModel();
    }

    @Override
    public void initSearch(String key, final int type, int page) {
        toSubscribe(mModel.getSearchResult(key, type, page),
                new Subscriber<HttpResult<SearchResultEntity>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().initSearchFail(type);
                    }

                    @Override
                    public void onNext(HttpResult<SearchResultEntity> result) {
                        getView().initSearchSuccess(result.getData(),type);
                    }
                });
    }

    @Override
    public void loadMoreSearch(String key, final int type, int page) {
        toSubscribe(mModel.getSearchResult(key, type, page),
                new Subscriber<HttpResult<SearchResultEntity>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().moreSearchFail(type);
                    }

                    @Override
                    public void onNext(HttpResult<SearchResultEntity> result) {
                        getView().moreSearchSuccess(type, result.getData());
                    }
                });
    }
}
