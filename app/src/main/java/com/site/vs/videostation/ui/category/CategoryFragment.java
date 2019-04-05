package com.site.vs.videostation.ui.category;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;


import com.site.vs.videostation.R;
import com.site.vs.videostation.adapter.CategoryContentAdapter;
import com.site.vs.videostation.adapter.CategoryHeaderAdapter;
import com.site.vs.videostation.adapter.base.CommRecyclerAdapter;
import com.site.vs.videostation.base.RecyclerFragment;
import com.site.vs.videostation.entity.CategoryDetailEntity;
import com.site.vs.videostation.entity.CategoryFilterEntity;
import com.site.vs.videostation.ui.detail.view.DetailActivity;
import com.site.vs.videostation.widget.refreshRecycler.GridMarginDecoration;
import com.site.vs.videostation.widget.refreshRecycler.RecyclerAdapterWithHF;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;

/**
 * @author zhangbb
 * @date 2016/12/19
 */
public class CategoryFragment extends RecyclerFragment<CategoryDetailEntity.VideoEntity>
        implements RecyclerAdapterWithHF.OnItemClickListener {



    private CategoryHeaderAdapter headerTopAdapter;
    private CategoryHeaderAdapter headerMidAdapter;
    private CategoryHeaderAdapter headerBtmAdapter;
    private final int SIZE = 10;
    protected int pageIndex = 1;
    protected int totalPage;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setPullRefreshEnable(false);
        recyclerView.addItemDecoration(new GridMarginDecoration((int)
                getResources().getDimension(R.dimen.font_size4)));
        adapterWithHF.setOnItemClickListener(this);
    }

    @Override
    protected View createHeader() {
        View headerView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_category_header, null);

        return headerView;
    }

    @Override
    protected CommRecyclerAdapter createAdapter() {
        return new CategoryContentAdapter(getContext(), R.layout.search_result_item);
    }

    /**
     * 初始化筛选条件
     *
     * @param entity
     * @param cid
     */
    public void setHeaderData(CategoryDetailEntity entity, String cid) {
        if (entity == null) return;

    }

    private void smoothMoveToPosition(RecyclerView list, LinearLayoutManager layoutManager, int position) {
        int firstPosition = layoutManager.findFirstCompletelyVisibleItemPosition();
        int lastPosition = layoutManager.findLastCompletelyVisibleItemPosition();
        int mid = lastPosition - firstPosition / 2;
        if (position < mid && position > 0) {
            list.smoothScrollToPosition(position - 1);
        } else {
            list.smoothScrollToPosition(position + 1);
        }
    }



    public void updateList(CategoryDetailEntity entity) {
        initPages(entity);
        initDataSuccess(entity.list, pageIndex < totalPage);
    }

    public void appendList(CategoryDetailEntity entity) {
        initPages(entity);
        loadMoreDataSuccess(entity.list, pageIndex <= totalPage);
    }

    private void initPages(CategoryDetailEntity entity) {
        int count = entity.count;
        if (count > SIZE) {
            totalPage = count % SIZE == 0 ? count / SIZE : count / SIZE + 1;
            pageIndex++;
        } else {
            totalPage = 1;
        }
        if (count == 0) {
            totalPage = 0;
        }
    }

    @Override
    protected void onRefresh() {

    }

    @Override
    protected void onLoadMore() {
        if (changeListener != null)
            changeListener.loadMore(pageIndex);
    }

    @Override
    public void onItemClick(RecyclerAdapterWithHF adapter, RecyclerView.ViewHolder vh, int position) {
        CategoryFilterEntity item = (CategoryFilterEntity) commRecyclerAdapter.getItem(position);
        Bundle bundle = new Bundle();
        bundle.putString("id", item.id);
        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public FilterChangeListener changeListener;

    public void setChangeListener(FilterChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    public interface FilterChangeListener {
        void filterChange(Map<String, String> map);

        void loadMore(int pageIndex);
    }
}
