package com.site.vs.videostation.ui.category;



import com.site.vs.videostation.base.BaseModel;
import com.site.vs.videostation.base.BasePresenter;
import com.site.vs.videostation.base.BaseView;
import com.site.vs.videostation.entity.CategoryDetailEntity;
import com.zhusx.core.network.HttpResult;

import java.util.Map;

import rx.Observable;

/**
 * @author zhangbb
 * @date 2016/11/17
 */
public interface CategoryDetailContract {

    interface Model extends BaseModel {
        Observable<HttpResult<CategoryDetailEntity>> getCategoryBy(String id, Map<String, String> map, int page);
    }


    interface View extends BaseView {

        void initCategorySuccess(CategoryDetailEntity categoryDetailEntity);

        void initCategoryContentSuccess(CategoryDetailEntity categoryDetailEntity);

        void loadMoreVideoSuccess(CategoryDetailEntity categoryDetailEntity);

        void loadMoreVideoFail();

        void initCategoryFail();
    }

    abstract class Presenter extends BasePresenter<View, CategoryModel> {
        public abstract void initCategoryBy(String id, Map<String, String> map, int page, boolean b);

        public abstract void loadMoreVideo(String id, Map<String, String> paraMap, int pageIndex);
    }
}
