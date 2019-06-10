package com.site.vs.videostation.ui.search;

import android.os.Bundle;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.site.vs.videostation.R;
import com.site.vs.videostation.base.MVPBaseFragment;
import com.site.vs.videostation.entity.SearchResultEntity;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author zhangbb
 * @date 2016/12/19
 */
public class SearchResultFragment extends MVPBaseFragment<SearchPresenter> implements SearchPageContract.SearchView {

    @BindView(R.id.viewPager)
    public ViewPager dailyPager;
    @BindView(R.id.tabLayout)
    public TabLayout topTitleTabLayout;
    @BindArray(R.array.search_top_menus)
    public String[] searchMenus;
    private SparseArray<Fragment> fragmentArray;
    private String searchKey;

    private static final int SIZE = 30;
    private int pageIndex = 1;
    protected int totalPage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_result, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        if (bundle != null) {
            searchKey = bundle.getString("search_key");
        }
        initFragments();
    }

    @Override
    protected void createPresenter() {
        mPresenter = new SearchPresenter();
        mPresenter.attachView(this);
    }

    private void initFragments() {
        fragmentArray = new SparseArray<>();
        FragmentManager childFragmentManager = getChildFragmentManager();
        FragmentPagerAdapter adapter = new TabPageIndicatorAdapter(childFragmentManager);
        dailyPager.setAdapter(adapter);
        dailyPager.setOffscreenPageLimit(4);
        topTitleTabLayout.setupWithViewPager(dailyPager);
    }

    private SearchContentFragment getFragment(int type) {
        return (SearchContentFragment) fragmentArray.get(type);
    }

    private void initPages(SearchResultEntity entity) {
        int count = entity.search_count;
        if (count > SIZE) {
            totalPage = count % SIZE == 0 ? count / SIZE : count / SIZE + 1;
        } else {
            totalPage = 1;
        }
        pageIndex++;
        if (count == 0) {
            totalPage = 0;
        }
    }

    @Override
    public void initSearchSuccess(SearchResultEntity entity, int type) {
        initPages(entity);
        getFragment(type).pageIndex++;
        getFragment(type).initDataSuccess(entity.search_list, pageIndex <= totalPage);
    }

    @Override
    public void moreSearchSuccess(int type, SearchResultEntity entity) {
        initPages(entity);
        getFragment(type).pageIndex++;
        getFragment(type).loadMoreDataSuccess(entity.search_list, pageIndex <= totalPage);
    }

    @Override
    public void initSearchFail(int type) {
        getFragment(type).initDataFail();
    }

    @Override
    public void moreSearchFail(int type) {
        getFragment(type).loadMoreDataFail();
    }

    class TabPageIndicatorAdapter extends FragmentPagerAdapter {

        public TabPageIndicatorAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(final int position) {
            if (fragmentArray.get(position) != null) {
                return fragmentArray.get(position);
            } else {
                SearchContentFragment resultFragment = new SearchContentFragment();
                resultFragment.setArguments(getArguments());
                resultFragment.setPresent(mPresenter);
                resultFragment.setType(position);
                fragmentArray.append(position, resultFragment);
                return resultFragment;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return searchMenus[position % searchMenus.length];
        }

        @Override
        public int getCount() {
            return searchMenus.length;
        }
    }

}
