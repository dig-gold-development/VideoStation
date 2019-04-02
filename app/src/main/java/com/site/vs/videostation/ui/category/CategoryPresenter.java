package com.site.vs.videostation.ui.category;


import com.site.vs.videostation.entity.CategoryDetailEntity;
import com.zhusx.core.network.HttpResult;

import java.util.Map;

import rx.Subscriber;

/**
 * @author zhangbb
 * @date 2016/12/19
 */
public class CategoryPresenter extends CategoryDetailContract.Presenter {

    @Override
    protected CategoryModel createModel() {
        return new CategoryModel();
    }

    @Override
    public void initCategoryBy(String id, Map<String, String> map, int page, final boolean b) {
        toSubscribe(mModel.getCategoryBy(id, map, page),
                new Subscriber<HttpResult<CategoryDetailEntity>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().initCategoryFail();
                    }

                    @Override
                    public void onNext(HttpResult<CategoryDetailEntity> result) {
                        if (b) {
                            getView().initCategorySuccess(result.getData());
                        } else {
                            getView().initCategoryContentSuccess(result.getData());
                        }
                    }
                });
    }

    @Override
    public void loadMoreVideo(String id, Map<String, String> map, int page) {
        toSubscribe(mModel.getCategoryBy(id, map, page),
                new Subscriber<HttpResult<CategoryDetailEntity>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().loadMoreVideoFail();
                    }

                    @Override
                    public void onNext(HttpResult<CategoryDetailEntity> result) {
                        getView().loadMoreVideoSuccess(result.getData());
                    }
                });
    }

}
