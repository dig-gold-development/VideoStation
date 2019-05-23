package com.site.vs.videostation.ui.category;


import com.site.vs.videostation.entity.CategoryDetailEntity;
import com.site.vs.videostation.http.JSONResult;


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
    public void initCategoryBy(Map<String, String> map, int page, final boolean b) {
        toSubscribe(mModel.getCategoryBy(map, page),
                new Subscriber<JSONResult<CategoryDetailEntity>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().initCategoryFail();
                    }

                    @Override
                    public void onNext(JSONResult<CategoryDetailEntity> result) {
                        if (b) {
                            getView().initCategorySuccess(result.data);
                        } else {
                            getView().initCategoryContentSuccess(result.data);
                        }
                    }
                });
    }

    @Override
    public void loadMoreVideo(String id, Map<String, String> map, int page) {
        toSubscribe(mModel.getCategoryBy( map, page),
                new Subscriber<JSONResult<CategoryDetailEntity>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().loadMoreVideoFail();
                    }

                    @Override
                    public void onNext(JSONResult<CategoryDetailEntity> result) {
                        getView().loadMoreVideoSuccess(result.data);
                    }
                });
    }

}
