package com.site.vs.videostation.ui.search;



import com.site.vs.videostation.entity.HotSearchEntity;
import com.zhusx.core.network.HttpResult;

import rx.Subscriber;

/**
 * @author zhangbb
 * @date 2016/12/19
 */
public class HotSearchPresenter extends SearchPageContract.Presenter {

    @Override
    protected SearchModel createModel() {
        return new SearchModel();
    }

    @Override
    public void initHotSearch() {
        toSubscribe(mModel.getHotSearch(),
                new Subscriber<HttpResult<HotSearchEntity>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(HttpResult<HotSearchEntity> result) {
                        getView().initHotSearchSuccess(result.getData());
                    }
                });
    }
}
