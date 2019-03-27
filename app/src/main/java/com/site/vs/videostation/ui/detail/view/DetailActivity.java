package com.site.vs.videostation.ui.detail.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.site.vs.videostation.R;
import com.site.vs.videostation.base.MVPBaseActivity;
import com.site.vs.videostation.db.DBManager;
import com.site.vs.videostation.entity.DetailEntity;
import com.site.vs.videostation.entity.HistoryEntity;
import com.site.vs.videostation.ui.detail.presentation.DetailContract;
import com.site.vs.videostation.ui.detail.presentation.DetailPresenter;
import com.site.vs.videostation.ui.video.VideoActivity;
import com.site.vs.videostation.utils.UnitUtils;
import com.site.vs.videostation.widget.FrescoImageView;
import com.site.vs.videostation.widget.StickyNavLayout;
import com.site.vs.videostation.widget.dialog.SelectOriginDialog;
import com.zhusx.core.network.Lib_NetworkStateReceiver;
import com.zhusx.core.utils._Networks;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author dxplay120
 * @date 2016/12/17
 */
public class DetailActivity extends MVPBaseActivity<DetailPresenter> implements DetailContract.View,
        SelectOriginDialog.OriginSelectedListener {

    public static final String ID = "id";
    private String id;
    private DetailEntity en;
    @BindView(R.id.tv_title)
    TextView titleTv;
    @BindView(R.id.iv_img)
    FrescoImageView coverIv;
    @BindView(R.id.iv_background)
    FrescoImageView backgroundIv;
    @BindView(R.id.tv_type)
    TextView typeTv;
    @BindView(R.id.tv_area)
    TextView areaTv;
    @BindView(R.id.tv_year)
    TextView yearTv;
    @BindView(R.id.tv_language)
    TextView languageTv;
    @BindView(R.id.tv_origin)
    TextView originTv;
    @BindView(R.id.iv_origin)
    FrescoImageView originIv;

    @BindView(R.id.id_stickynavlayout_indicator)
    TabLayout tabLayout;
    @BindView(R.id.id_stickynavlayout_viewpager)
    ViewPager viewPager;
    @BindView(R.id.iv_collect)
    ImageView collectImg;
    @BindView(R.id.tv_record)
    TextView recordTv;
    @BindView(R.id.iv_tips)
    ImageView tipsIv;
    @BindView(R.id.layout_loading)
    LinearLayout loadingLayout;
    @BindView(R.id.layout_content)
    StickyNavLayout contentLayout;

    VodListFragment vodListFragment;
    SelectOriginDialog selectOriginDialog;
    int originSel = 0;
    HistoryEntity history;
    Lib_NetworkStateReceiver receiver;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        if (getIntent() != null && getIntent().getStringExtra(ID) != null)
            id = getIntent().getStringExtra(ID);
        mPresenter.getDetail(id);

        receiver = new Lib_NetworkStateReceiver();
        receiver.registerNetworkStateReceiver(this);
    }

    @Override
    protected void createPresenter() {
        mPresenter = new DetailPresenter();
    }

    @Override
    public void getDetailSuccess(DetailEntity entity) {
        en = entity;
        titleTv.setText(entity.name);
        collectImg.setImageResource(DBManager.isFavorite(en.id) ? R.drawable.nav_collection_current : R.drawable.nav_collection);
        updateRecord();
        coverIv.setImageURI(entity.pic);
        backgroundIv.setImageUrlWithBlur(entity.pic);
        backgroundIv.setAlpha(0.5f);
        typeTv.setText("类型：" + entity.keywords);
        areaTv.setText("区域：" + entity.area);
        originTv.setText(entity.vod_url_list.get(0).origin.name);
        originIv.setImageURI(entity.vod_url_list.get(0).origin.img_url);
        final List<Item> lst = getItems(entity);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return lst.size();
            }

            @Override
            public Fragment getItem(int position) {
                return lst.get(position).fragment;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return lst.get(position).title;
            }
        });
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(3);
    }

    private void updateRecord() {
        if (en != null){
            history = DBManager.getHistory(en.id);
            if (history != null) {
                if (en.type == 1)
                    recordTv.setText("上次已观看到:" + UnitUtils.secToTime(history.playTime));
                else {
                    recordTv.setText("上次已观看到:" + history.playName);
                }
                recordTv.setVisibility(View.VISIBLE);
                tipsIv.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateRecord();
    }

    @Override
    public void getDetailFailed(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        finish();
    }


    private List<Item> getItems(DetailEntity entity) {
        List<Item> lst = new ArrayList<>();
        Fragment fragment = new ProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", entity);
        fragment.setArguments(bundle);
        lst.add(new Item("简介", fragment));
        switch (entity.type) {
            case 1:
                fragment = new CommentsFragment();
                fragment.setArguments(bundle);
                lst.add(0, new Item("影评（" + (entity.comment_list != null ? entity.comment_list.size() : 0) + "）", fragment));

                fragment = new NearFragment();
                fragment.setArguments(bundle);
                lst.add(0, new Item("类似", fragment));
                break;
            case 2:
            case 3:
                fragment = new NearFragment();
                fragment.setArguments(bundle);
                lst.add(0, new Item("类似", fragment));

                vodListFragment = new VodListFragment();
                vodListFragment.setArguments(bundle);
                lst.add(0, new Item("剧集", vodListFragment));
                break;
            case 4:
                vodListFragment = new VodListFragment();
                vodListFragment.setArguments(bundle);
                lst.add(0, new Item("剧集", vodListFragment));
                break;
        }
        return lst;
    }

    @OnClick({R.id.iv_back, R.id.tv_play, R.id.iv_collect, R.id.tv_origin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_play:
                updateRecord();
                if (Lib_NetworkStateReceiver._Current_NetWork_Status == _Networks.NetType.Wifi)
                    startPlayVideo();
                else {
                    new AlertDialog.Builder(this).setMessage("非wifi环境观看视频会消耗流量，是否继续观看？").setPositiveButton("取消 ", null).setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startPlayVideo();
                        }
                    }).create().show();
                }
                break;
            case R.id.iv_collect:
                if (en != null) {
                    collectImg.setImageResource(DBManager.swithFavorite(en) ? R.drawable.nav_collection_current : R.drawable.nav_collection);
                }
                break;
            case R.id.tv_origin:
                if (selectOriginDialog == null)
                    selectOriginDialog = new SelectOriginDialog(this, en, this);
                selectOriginDialog.setSel(originSel);
                selectOriginDialog.show();
                break;
        }
    }

    private void startPlayVideo() {
        if (history == null)
            VideoActivity.playVideo(DetailActivity.this, en.name, en.vod_url_list.get(originSel).list.get(0).play_url, en, originSel, 0,
                    0);
        else
            VideoActivity.playVideo(DetailActivity.this, en.name,
                    en.vod_url_list.get(history.originIndex).list.get(history.playIndex).play_url,
                    en, history.originIndex, history.playIndex,
                    history.playTime);
    }

    @OnClick(R.id.iv_share)
    public void shareMovieInfo() {
    }

    @Override
    public void onOriginSelected(int sel) {
        if (originSel != sel) {
            originSel = sel;
            if (vodListFragment != null)
                vodListFragment.setIndex(originSel);
            originTv.setText(en.vod_url_list.get(originSel).origin.name);
            originIv.setImageURI(en.vod_url_list.get(originSel).origin.img_url);
        }
    }

    @Override
    public void showLoading() {
        super.showLoading();
        contentLayout.setVisibility(View.INVISIBLE);
        loadingLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        contentLayout.setVisibility(View.VISIBLE);
        loadingLayout.setVisibility(View.INVISIBLE);
    }

    class Item {
        public String title;
        public Fragment fragment;

        public Item(String title, Fragment fragment) {
            this.title = title;
            this.fragment = fragment;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        receiver.unRegisterNetworkStateReceiver(this);
    }
}
