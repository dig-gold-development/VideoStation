package com.site.vs.videostation.ui.search;




import com.site.vs.videostation.base.BaseModel;
import com.site.vs.videostation.base.BasePresenter;
import com.site.vs.videostation.base.BaseView;
import com.site.vs.videostation.entity.HotSearchEntity;
import com.site.vs.videostation.entity.SearchResultEntity;
import com.zhusx.core.network.HttpResult;

import rx.Observable;

/**
 * @author zhangbb
 * @date 2016/11/17
 */
public interface SearchPageContract {

    interface Model extends BaseModel {
        Observable<HttpResult<SearchResultEntity>> getSearchResult(String key, int type, int page);

        Observable<HttpResult<HotSearchEntity>> getHotSearch();
    }

    interface View extends BaseView {
        void initHotSearchSuccess(HotSearchEntity entity);
    }

    interface SearchView extends BaseView {

        void initSearchSuccess(SearchResultEntity entity, int type);

        void moreSearchSuccess(int type, SearchResultEntity entity);

        void initSearchFail(int type);

        void moreSearchFail(int type);
    }

    abstract class Presenter extends BasePresenter<View, SearchModel> {
        public abstract void initHotSearch();

    }

    abstract class SearchPresenter extends BasePresenter<SearchView, SearchModel> {

        public abstract void initSearch(String key, int type, int page);

        public abstract void loadMoreSearch(String key, int type, int page);
    }
}
