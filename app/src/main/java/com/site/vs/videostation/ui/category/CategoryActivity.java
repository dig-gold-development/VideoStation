package com.site.vs.videostation.ui.category;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.site.vs.videostation.R;
import com.site.vs.videostation.base.MVPBaseActivity;
import com.site.vs.videostation.entity.CategoryDetailEntity;
import com.site.vs.videostation.widget.loadingtips.DefaultLoadingLayout;
import com.site.vs.videostation.widget.loadingtips.SmartLoadingLayout;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author zhangbb
 * @date 2016/12/19
 */
public class CategoryActivity extends MVPBaseActivity<CategoryPresenter>
        implements CategoryDetailContract.View, CategoryFragment.FilterChangeListener {

    public static final String EXTRA_STRING_ID = "extra_id";
    public static final String EXTRA_STRING_CID = "extra_cid";

    @BindView(R.id.flayout_category)
    public FrameLayout frameLayout;
    @BindView(R.id.tv_title)
    public TextView tvTitle;
    private CategoryFragment categoryFragment;
    private DefaultLoadingLayout tipsLayout;
    private Map<String, String> paraMap;

    private String id;
    private String cid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_homepage);
        ButterKnife.bind(this);
        tipsLayout = SmartLoadingLayout.createDefaultLayout(this, frameLayout);
        categoryFragment = new CategoryFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.flayout_category, categoryFragment).commit();
        categoryFragment.setChangeListener(this);
        initData();
    }

    private void initData() {
        showLoading();
        id = getIntent().getStringExtra(EXTRA_STRING_ID);
        cid = getIntent().getStringExtra(EXTRA_STRING_CID);

        switch (Integer.parseInt(id)) {
            case 2:
                tvTitle.setText("电视剧");
                break;
            case 3:
                tvTitle.setText("动漫");
                break;
            case 4:
                tvTitle.setText("综艺");
                break;
        }

        paraMap = new HashMap<>();
        paraMap.put("cid", cid == null ? "" : cid);
        paraMap.put("year", "");
        paraMap.put("area", "");
        mPresenter.initCategoryBy(id, paraMap, 1, true);
    }

    @Override
    protected void createPresenter() {
        mPresenter = new CategoryPresenter();
        mPresenter.attachView(this);
    }

    @Override
    public void initCategorySuccess(CategoryDetailEntity entity) {
        tipsLayout.onDone();
        categoryFragment.setHeaderData(entity, cid);
        initCategoryContentSuccess(entity);
    }

    @Override
    public void initCategoryContentSuccess(CategoryDetailEntity entity) {
        if (entity != null && entity.list != null) {
            categoryFragment.updateList(entity);
        }
    }

    @Override
    public void loadMoreVideoSuccess(CategoryDetailEntity entity) {
        categoryFragment.appendList(entity);
    }

    @Override
    public void loadMoreVideoFail() {
        categoryFragment.loadMoreDataFail();
    }

    @Override
    public void initCategoryFail() {
        tipsLayout.onError();
        tipsLayout.setErrorButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.initCategoryBy(id, paraMap, 1, true);
            }
        });
    }

    @Override
    public void showLoading() {
        tipsLayout.onLoading();
    }

    @Override
    public void hideLoading() {
        tipsLayout.onDone();
    }

    @Override
    public void filterChange(Map<String, String> map) {
        this.paraMap = map;
        mPresenter.initCategoryBy(id, map, 1, false);
    }

    @Override
    public void loadMore(int pageIndex) {
        if (paraMap != null)
            mPresenter.loadMoreVideo(id, paraMap, pageIndex);
    }

    @OnClick(R.id.iv_search)
    public void searchMovies() {
//        startActivity(new Intent(this, SearchActivity.class));
    }
}
