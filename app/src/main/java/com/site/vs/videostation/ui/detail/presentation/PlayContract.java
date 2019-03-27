package com.site.vs.videostation.ui.detail.presentation;



import com.site.vs.videostation.base.BaseModel;
import com.site.vs.videostation.base.BasePresenter;
import com.site.vs.videostation.base.BaseView;
import com.site.vs.videostation.entity.MoveAddressEntity;
import com.zhusx.core.network.HttpResult;

import rx.Observable;

/**
 * @author dxplay120
 * @date 2016/12/26
 */
public interface PlayContract {
    interface View extends BaseView {
        void playMoveSuccess(MoveAddressEntity entity, String title);
        void playWebMoveSuccess(String msg);
        void playMoveFailed(String msg);
    }

    interface Model extends BaseModel {
        Observable<HttpResult<MoveAddressEntity>> playMove(String url, String type);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void playMove(String url, String type, String title);
    }
}
