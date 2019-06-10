package com.site.vs.videostation.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.site.vs.videostation.R;
import com.site.vs.videostation.adapter.SearchResultAdapter;
import com.site.vs.videostation.adapter.base.CommRecyclerAdapter;
import com.site.vs.videostation.entity.SearchResultEntity;
import com.site.vs.videostation.ui.detail.view.DetailActivity;
import com.site.vs.videostation.widget.refreshRecycler.GridMarginDecoration;
import com.site.vs.videostation.widget.refreshRecycler.RecyclerAdapterWithHF;

import butterknife.ButterKnife;

/**
 * @author zhangbb
 * @date 2016/12/19
 */
public class SearchContentFragment extends SearchBaseFragment {

    private String searchKey;
    private SearchPresenter presenter;
    private int type;
    public int pageIndex = 1;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setPullRefreshEnable(false);
        recyclerView.addItemDecoration(new GridMarginDecoration((int)
                getResources().getDimension(R.dimen.font_size4)));
        adapterWithHF.setOnItemClickListener(this);
    }

    @Override
    protected View createHeader() {
        return null;
    }

    @Override
    protected CommRecyclerAdapter createAdapter() {
        return new SearchResultAdapter(getContext(), R.layout.search_result_item);
    }

    @Override
    protected void onRefresh() {
        if (presenter != null)
            presenter.initSearch(searchKey, type, pageIndex);
    }

    @Override
    protected void onLoadMore() {
        presenter.loadMoreSearch(searchKey, type, pageIndex);
    }

    @Override
    public void initData() {
        showLoading();
        Bundle bundle = getArguments();
        if (bundle != null) {
            searchKey = bundle.getString("search_key");
        }
        if (presenter != null)
            presenter.initSearch(searchKey, type, pageIndex);
    }

    @Override
    protected void initView(View view) {
        ButterKnife.bind(this, view);
    }


    @Override
    public void onItemClick(RecyclerAdapterWithHF adapter, RecyclerView.ViewHolder vh, int position) {
        SearchResultEntity.SearchResult item = (SearchResultEntity.SearchResult) commRecyclerAdapter.getItem(position);
        Bundle bundle = new Bundle();
        bundle.putString("id", item.id);
        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void setPresent(SearchPresenter present) {
        this.presenter = present;
    }

    public void setType(int type) {
        this.type = type;
    }

}
