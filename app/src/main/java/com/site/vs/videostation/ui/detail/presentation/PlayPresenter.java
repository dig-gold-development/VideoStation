package com.site.vs.videostation.ui.detail.presentation;



import com.site.vs.videostation.entity.MoveAddressEntity;
import com.zhusx.core.network.HttpResult;

import rx.Subscriber;

/**
 * @author dxplay120
 * @date 2016/12/26
 */
public class PlayPresenter extends PlayContract.Presenter {
    @Override
    public void playMove(final String url, String type, final String title) {
        toSubscribe(mModel.playMove(url, type), new Subscriber<HttpResult<MoveAddressEntity>>() {

            @Override
            public void onStart() {
                if (getView() != null)
                    getView().showLoading();
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (getView() != null)
                    getView().playMoveFailed(getErrorMsg(e));
            }

            @Override
            public void onNext(HttpResult<MoveAddressEntity> moveAddressEntityHttpResult) {
                if (getView() != null){
                    if (moveAddressEntityHttpResult.getData().play_type == 0)
                        getView().playWebMoveSuccess(url);
                    else
                        getView().playMoveSuccess(moveAddressEntityHttpResult.getData(), title);
                }

            }
        });

    }

    @Override
    protected PlayContract.Model createModel() {
        return new PlayModel();
    }
}
