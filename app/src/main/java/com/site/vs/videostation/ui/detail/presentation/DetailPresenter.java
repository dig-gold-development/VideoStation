package com.site.vs.videostation.ui.detail.presentation;



import com.site.vs.videostation.entity.DetailEntity;
import com.zhusx.core.network.HttpResult;

import rx.Subscriber;

/**
 * @author dxplay120
 * @date 2016/12/17
 */
public class DetailPresenter extends DetailContract.Presenter {
    @Override
    public void getDetail(String id) {
        toSubscribe(mModel.getDetail(id), new Subscriber<HttpResult<DetailEntity>>() {
            @Override
            public void onStart() {
                super.onStart();
                if (getView() != null)
                    getView().showLoading();
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (getView() != null){
                    getView().hideLoading();
                    getView().getDetailFailed(getErrorMsg(e));
                }

            }

            @Override
            public void onNext(HttpResult<DetailEntity> detailEntityHttpResult) {
                if (getView() != null){
                    getView().hideLoading();
                    getView().getDetailSuccess(detailEntityHttpResult.getData());
                }

            }
        });
    }


    @Override
    protected DetailContract.Model createModel() {
        return new DetailModel();
    }
}
