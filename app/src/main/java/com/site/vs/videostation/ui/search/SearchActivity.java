package com.site.vs.videostation.ui.search;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.site.vs.videostation.R;
import com.site.vs.videostation.adapter.SearchHistoryAdapter;
import com.site.vs.videostation.adapter.SearchHotAdapter;
import com.site.vs.videostation.adapter.base.CommRecyclerAdapter;
import com.site.vs.videostation.base.MVPBaseActivity;
import com.site.vs.videostation.entity.HotSearchEntity;
import com.site.vs.videostation.widget.ClearEditText;
import com.site.vs.videostation.widget.loadingtips.DefaultLoadingLayout;
import com.site.vs.videostation.widget.loadingtips.SmartLoadingLayout;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author zhangbb
 * @date 2016/12/19
 */
public class SearchActivity extends MVPBaseActivity<HotSearchPresenter> implements SearchPageContract.View {

    @BindView(R.id.edt_input)
    public ClearEditText inputEdt;
    @BindView(R.id.flayout_search)
    public FrameLayout frameLayout;
    @BindView(R.id.recyclerView_search)
    public RecyclerView historyList;
    @BindView(R.id.recyclerView_hot)
    public RecyclerView hotSearchList;
    @BindView(R.id.llayout_hot)
    public LinearLayout hotLayout;
    private DefaultLoadingLayout tipsLayout;
    private SearchResultFragment searchResultFragment;
    private List<String> searchList = new ArrayList<>();
    private SearchHistoryAdapter historyAdapter;
    private static final String SEARCH = "search";
    private static final String SEARCH_KEY = "s_h";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_homepage);
        ButterKnife.bind(this);
        initEditInput();
        tipsLayout = SmartLoadingLayout.createDefaultLayout(this, frameLayout);
        updateHistoryList();
        mPresenter.initHotSearch();
    }

    private void initEditInput() {
        inputEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchMovies(inputEdt.getText().toString().trim());
                }
                return false;
            }
        });
        inputEdt.setClearListener(new ClearEditText.ClearListener() {
            @Override
            public void inputClear() {
                tipsLayout.onDone();
                if (searchResultFragment != null && searchResultFragment.isAdded()) {
                    getSupportFragmentManager().beginTransaction()
                            .hide(searchResultFragment).commit();
                }
                if (hotLayout.getVisibility() == View.GONE)
                    hotLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void createPresenter() {
        mPresenter = new HotSearchPresenter();
        mPresenter.attachView(this);
    }

    private void searchMovies(String v) {
        if (!v.isEmpty()) {
            hideKeyboard(inputEdt);
            if (searchList.contains(v)) {
                searchList.remove(v);
            }
            searchList.add(0, v);
            showSearchView(v);
        }
    }

    public void showSearchView(String key) {
        hotLayout.setVisibility(View.GONE);
        searchResultFragment = new SearchResultFragment();
        Bundle bundle = new Bundle();
        bundle.putString("search_key", key);
        searchResultFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flayout_search, searchResultFragment).commit();
    }

    /**
     * 热搜
     *
     * @param entity
     */
    @Override
    public void initHotSearchSuccess(final HotSearchEntity entity) {
        SearchHotAdapter hotAdapter = new SearchHotAdapter(this, R.layout.search_hot_history_item);
        hotSearchList.setLayoutManager(new GridLayoutManager(this, 2));
        hotSearchList.setHasFixedSize(true);
        hotSearchList.setAdapter(hotAdapter);
        hotAdapter.addAll(entity.hot_list);
        hotAdapter.setOnItemClickListener(new CommRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder, View view, int position) {
                searchMovies(entity.hot_list.get(position));
                inputEdt.setSearchText(entity.hot_list.get(position));
            }
        });
    }

    @OnClick(R.id.tv_finish)
    public void onSearchClick() {
        SearchActivity.this.finish();
    }

    /**
     * 更新搜索历史列表
     */
    private void updateHistoryList() {
        String s = getHistory(SEARCH_KEY, "");
        if (!s.isEmpty()) {
            searchList = JSON.parseArray(s, String.class);
            if (searchList.isEmpty()) {
                return;
            }
            historyAdapter = new SearchHistoryAdapter(this, R.layout.search_history_item);
            historyList.setLayoutManager(new GridLayoutManager(this, 2));
            historyList.setHasFixedSize(true);
            historyList.setAdapter(historyAdapter);
            historyAdapter.addAll(searchList);
            historyAdapter.setClickListener(new SearchHistoryAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(String s) {
                    searchMovies(s);
                    inputEdt.setSearchText(s);
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (searchList != null && searchList.size() > 0) {
            JSONArray jsonArray = new JSONArray();
            jsonArray.addAll(searchList);
            putHistory(SEARCH_KEY, jsonArray.toString());
        }
    }

    @OnClick(R.id.tv_clear)
    public void SearchHistoryClear() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("是否删除搜索记录？");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                searchList.clear();
                if (historyAdapter != null)
                    historyAdapter.removeAll();
                clear();
            }
        });
        builder.show();
    }

    private void putHistory(String key, String value) {
        SharedPreferences.Editor edit = getSp().edit();
        edit.putString(key, value);
        edit.apply();
    }

    private void clear() {
        SharedPreferences.Editor edit = getSp().edit();
        edit.clear();
        edit.apply();
    }

    private String getHistory(String key, String defValue) {
        return getSp().getString(key, defValue);
    }

    private SharedPreferences getSp() {
        return SearchActivity.this.getSharedPreferences(SEARCH, Context.MODE_PRIVATE);
    }
}
