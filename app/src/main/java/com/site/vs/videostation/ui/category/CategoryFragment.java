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
import com.site.vs.videostation.entity.Move;
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
public class CategoryFragment extends RecyclerFragment<Move> implements RecyclerAdapterWithHF.OnItemClickListener {

    private RecyclerView categoryListView;
    private RecyclerView areaListView;
    private RecyclerView yearListView;

    private CategoryHeaderAdapter headerTopAdapter;
    private CategoryHeaderAdapter headerMidAdapter;
    private CategoryHeaderAdapter headerBtmAdapter;

    private static final int SIZE = 20;
    protected int pageIndex = 1;
    protected int totalPage;



    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setPullRefreshEnable(false);
        recyclerView.addItemDecoration(new GridMarginDecoration((int) getResources().getDimension(R.dimen.font_size4)));
        adapterWithHF.setOnItemClickListener(this);
    }

    @Override protected View createHeader() {
        View headerView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_category_header, null);
        categoryListView = (RecyclerView) headerView.findViewById(R.id.list_category);
        areaListView = (RecyclerView) headerView.findViewById(R.id.list_area);
        yearListView = (RecyclerView) headerView.findViewById(R.id.list_time);
        return headerView;
    }

    @Override protected CommRecyclerAdapter createAdapter() {
        return new CategoryContentAdapter(getContext(), R.layout.search_result_item);
    }

    /**
     * 初始化筛选条件
     *
     * @param entity
     * @param tid
     */
    public void setHeaderData(CategoryDetailEntity entity, String tid) {
        if (entity == null) return;
        if (entity.category_list != null && entity.category_list.size() > 0) {
            categoryListView.setVisibility(View.VISIBLE);
            headerTopAdapter = new CategoryHeaderAdapter(getContext(), R.layout.category_list_item, "全部类别");
            final LinearLayoutManager headerLayout = new LinearLayoutManager(getContext());
            headerLayout.setOrientation(LinearLayoutManager.HORIZONTAL);
            categoryListView.setLayoutManager(headerLayout);
            categoryListView.addItemDecoration(new GridMarginDecoration(10));
            categoryListView.setAdapter(headerTopAdapter);
            headerTopAdapter.addAll(entity.category_list);
            int selectPosition = 0;
            for (CategoryFilterEntity en : entity.category_list) {
                selectPosition++;
                if (en.tid.equals(tid)) {
                    headerTopAdapter.setDefaultSelect(selectPosition);
                    smoothMoveToPosition(categoryListView, headerLayout, selectPosition);
                    break;
                }
            }
            headerTopAdapter.setChangeListener(new CategoryHeaderAdapter.OnFilterChangeListener() {
                @Override
                public void OnFilterChangeClick(int i) {
                    updateFilter();
                    smoothMoveToPosition(categoryListView, headerLayout, i);
                }
            });
        }

        //地区筛选
        if (entity.area_list != null && entity.area_list.size() > 0) {
            areaListView.setVisibility(View.VISIBLE);
            List<CategoryFilterEntity> areaList = new ArrayList<>();
            CategoryFilterEntity filterEntity;
            for (String area : entity.area_list) {
                filterEntity = new CategoryFilterEntity();
                filterEntity.tname = area;
                areaList.add(filterEntity);
            }
            headerMidAdapter = new CategoryHeaderAdapter(getContext(), R.layout.category_list_item, "全部地区");
            final LinearLayoutManager areLayout = new LinearLayoutManager(getContext());
            areLayout.setOrientation(LinearLayoutManager.HORIZONTAL);
            areaListView.setLayoutManager(areLayout);
            areaListView.addItemDecoration(new GridMarginDecoration(10));
            areaListView.setAdapter(headerMidAdapter);
            headerMidAdapter.addAll(areaList);
            headerMidAdapter.setChangeListener(new CategoryHeaderAdapter.OnFilterChangeListener() {
                @Override
                public void OnFilterChangeClick(int i) {
                    updateFilter();
                    smoothMoveToPosition(areaListView, areLayout, i);
                }
            });
        }
        //时间筛选
        if (entity.year_list != null && entity.year_list.size() > 0) {
            yearListView.setVisibility(View.VISIBLE);
            List<CategoryFilterEntity> yearList = new ArrayList<>();
            CategoryFilterEntity filterEntity;
            for (String year : entity.year_list) {
                filterEntity = new CategoryFilterEntity();
                filterEntity.tname = year;
                yearList.add(filterEntity);
            }
            headerBtmAdapter = new CategoryHeaderAdapter(getContext(), R.layout.category_list_item, "全部年份");
            final LinearLayoutManager yearLayout = new LinearLayoutManager(getContext());
            yearLayout.setOrientation(LinearLayoutManager.HORIZONTAL);
            yearListView.setLayoutManager(yearLayout);
            yearListView.addItemDecoration(new GridMarginDecoration(10));
            yearListView.setAdapter(headerBtmAdapter);
            headerBtmAdapter.addAll(yearList);
            headerBtmAdapter.setChangeListener(new CategoryHeaderAdapter.OnFilterChangeListener() {
                @Override
                public void OnFilterChangeClick(int i) {
                    updateFilter();
                    smoothMoveToPosition(yearListView, yearLayout, i);
                }
            });
        }

    }

    /**
     * 条件筛选
     */
    private void updateFilter() {
        Map<String, String> map = new HashMap<>();

        if (headerTopAdapter != null) {
            CategoryFilterEntity categoryItem = headerTopAdapter.getSelectItem();
            if (categoryItem != null) {
                map.put("tid", categoryItem.tid);
            }
        }
        if (headerMidAdapter != null) {
            CategoryFilterEntity areaItem = headerMidAdapter.getSelectItem();
            if (areaItem != null) {
                map.put("area", areaItem.tname);
            }
        }
        if (headerBtmAdapter != null) {
            CategoryFilterEntity yearItem = headerBtmAdapter.getSelectItem();
            if (yearItem != null) {
                map.put("year", yearItem.tname);
            }
        }
        if (changeListener != null) {
            pageIndex = 1;
            changeListener.filterChange(map);
        }
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
        initDataSuccess(entity.vod_list, pageIndex < totalPage);
    }

    public void appendList(CategoryDetailEntity entity) {
        initPages(entity);
        loadMoreDataSuccess(entity.vod_list, pageIndex <= totalPage);
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

    @Override protected void onRefresh() {

    }

    @Override protected void onLoadMore() {
        if (changeListener != null) changeListener.loadMore(pageIndex);
    }

    @Override public void onItemClick(RecyclerAdapterWithHF adapter, RecyclerView.ViewHolder vh, int position) {
        CategoryFilterEntity item = (CategoryFilterEntity) commRecyclerAdapter.getItem(position);
        Bundle bundle = new Bundle();
        bundle.putString("id", item.tid);
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
