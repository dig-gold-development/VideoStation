package com.site.vs.videostation.ui.detail.presentation;



import com.site.vs.videostation.base.BaseModel;
import com.site.vs.videostation.base.BasePresenter;
import com.site.vs.videostation.base.BaseView;
import com.site.vs.videostation.entity.DetailEntity;
import com.zhusx.core.network.HttpResult;

import rx.Observable;

/**
 * @author dxplay120
 * @date 2016/12/17
 */
public interface DetailContract {
    interface View extends BaseView {
        void getDetailSuccess(DetailEntity entity);

        void getDetailFailed(String msg);
    }

    interface Model extends BaseModel {
        Observable<HttpResult<DetailEntity>> getDetail(String id);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void getDetail(String id);
    }
}
