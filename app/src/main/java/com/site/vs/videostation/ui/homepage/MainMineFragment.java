package com.site.vs.videostation.ui.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.site.vs.videostation.R;
import com.site.vs.videostation.base.BaseFragment;
import com.site.vs.videostation.ui.SettingActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2016/12/16 17:13
 */

public class MainMineFragment extends BaseFragment {
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    MyHistoryFragment historyFragment;
    MyFavoriteFragment favoriteFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initData();
    }

    private void initData() {
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return historyFragment = new MyHistoryFragment();
                    case 1:
                        return favoriteFragment = new MyFavoriteFragment();
                }
                return null;
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                String[] st = new String[]{"观看记录", "我的收藏"};
                return st[position];
            }
        });
        tabLayout.setupWithViewPager(viewPager);
    }

    @OnClick({R.id.iv_setting, R.id.iv_delete})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_setting:
                //TODO 设置
                startActivity(new Intent(getContext(), SettingActivity.class));
                break;
            case R.id.iv_delete:
                switch (viewPager.getCurrentItem()) {
                    case 0:
                        historyFragment.clearHistory();
                        break;
                    case 1:
                        favoriteFragment.clearFavorite();
                        break;
                }
                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        historyFragment.onHiddenChanged(hidden);
        favoriteFragment.onHiddenChanged(hidden);
    }
}
