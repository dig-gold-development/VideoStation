package com.site.vs.videostation.ui.video;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.site.vs.videostation.R;
import com.site.vs.videostation.base.MVPBaseActivity;
import com.site.vs.videostation.db.DBManager;
import com.site.vs.videostation.entity.DetailEntity;
import com.site.vs.videostation.entity.HistoryEntity;
import com.site.vs.videostation.entity.MoveAddressEntity;
import com.site.vs.videostation.ui.detail.presentation.PlayContract;
import com.site.vs.videostation.ui.detail.presentation.PlayPresenter;
import com.site.vs.videostation.ui.detail.view.VodListFragment;
import com.site.vs.videostation.utils.UnitUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tcking.github.com.giraffeplayer.GiraffePlayer;

/**
 * @author dxplay120
 * @date 2016/12/21
 */
public class VideoActivity extends MVPBaseActivity<PlayPresenter> implements PlayContract.View {
    public static final String TITLE = "title";
    public static final String URL = "url";
    public static final String ORIGIN_INDEX = "origin_index";
    public static final String PLAY_INDEX = "play_index";
    public static final String DATA = "data";
    public static final String TIME = "time";
    private int realUrl;


    public static void playVideo(Activity activity, String title, String url, DetailEntity entity, int originIndex,
                                 int playIndex, int time) {
        Intent intent = new Intent(activity, VideoActivity.class);
        intent.putExtra(TITLE, title);
        intent.putExtra(URL, url);
        intent.putExtra(ORIGIN_INDEX, originIndex);
        intent.putExtra(PLAY_INDEX, playIndex);
        Bundle bundle = new Bundle();
        bundle.putSerializable(DATA, entity);
        intent.putExtra(DATA, bundle);
        intent.putExtra(TIME, time);

        activity.startActivity(intent);
    }

    GiraffePlayer player;
    int originIndex = 0;
    int playIndex = 0;
    DetailEntity entity;

    @BindView(R.id.app_video_next)
    ImageView nextIv;
    @BindView(R.id.drawer)
    DrawerLayout drawerLayout;
    @BindView(R.id.app_video_sel_play)
    TextView selPlayTv;
    @BindView(R.id.app_video_sel_definition)
    TextView definitionTv;
    @BindView(R.id.app_video_fullscreen)
    ImageView fullscreenTv;
    VodListFragment vodListFragment;
    List<MoveAddressEntity.Definition> type_list;
    PopupWindow definitionPopupWindow;
    LinearLayout definitionLv;
    String url;
    String title;
    private int currentPos;
    private boolean isInPlay = false;
    private String definition = "2";

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        ButterKnife.bind(this);
        initData(getIntent());
        initViews();
        initPlayer();
    }

    @Override protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initData(intent);
        initViews();
        initPlayer();
    }

    private void initViews() {
        if (entity.type != 1) {
            if (canPlayNext()) nextIv.setVisibility(View.VISIBLE);

            if (vodListFragment == null) {
                vodListFragment = new VodListFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", entity);
                bundle.putInt("index", originIndex);
                bundle.putInt("play_index", playIndex);
                bundle.putInt("mode", 1);
                vodListFragment.setArguments(bundle);
            }
            selPlayTv.setVisibility(View.VISIBLE);
            if (!vodListFragment.isAdded()) getSupportFragmentManager().beginTransaction().replace(R.id.right_content,
                                                                                                   vodListFragment).commitAllowingStateLoss();
        }

        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        fullscreenTv.setVisibility(View.GONE);
    }

    @OnClick({R.id.app_video_next, R.id.app_video_sel_play, R.id.app_video_sel_definition}) void onClick(View view) {
        switch (view.getId()) {
            case R.id.app_video_next:
                playNext();
                break;
            case R.id.app_video_sel_play:
                drawerLayout.openDrawer(Gravity.RIGHT);
                break;
            case R.id.app_video_sel_definition:
                if (definitionPopupWindow == null) {
                    LayoutInflater inflater = LayoutInflater.from(this);
                    View contentView = inflater.inflate(R.layout.popupwindow_definition, null);
                    definitionLv = contentView.findViewById(R.id.listview);
                    if (type_list != null) {
                        for (int i = 0; i < type_list.size(); i++) {
                            TextView tvName = (TextView) LayoutInflater.from(this).inflate(
                                    R.layout.list_item_definition, definitionLv, false);
                            tvName.setText(type_list.get(i).type);
                            final int temp = i;
                            tvName.setOnClickListener(new View.OnClickListener() {
                                @Override public void onClick(View v) {
                                    definitionTv.setText(type_list.get(temp).type);
                                    definitionPopupWindow.dismiss();
                                    player.pause();
                                    definition = type_list.get(temp).hd + "";
                                    mPresenter.playMove(url, definition,
                                                        entity.type == 1 ? "" : entity.vod_url_list.get(
                                                                originIndex).list.get(playIndex).play_name);
                                    currentPos = player.getCurrentPosition();
                                }
                            });
                            definitionLv.addView(tvName);
                        }
                    } else Toast.makeText(this, "对不起，当前只有一种清晰度", Toast.LENGTH_SHORT).show();

                    definitionPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT,
                                                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    definitionPopupWindow.setBackgroundDrawable(new BitmapDrawable());
                    definitionPopupWindow.setOutsideTouchable(true);
                    definitionPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
                    definitionPopupWindow.update();
                    definitionPopupWindow.setTouchable(true);
                    definitionPopupWindow.setFocusable(true);

                }
                int[] location = new int[2];
                view.getLocationOnScreen(location);
                definitionPopupWindow.showAtLocation(drawerLayout, Gravity.NO_GRAVITY, location[0],
                                                     location[1] - UnitUtils.dip2px(this, 150f));
                break;
        }
    }

    private boolean canPlayNext() {
        return playIndex + 1 < entity.vod_url_list.get(originIndex).list.size();
    }

    private void initData(Intent intent) {
        originIndex = intent.getIntExtra(ORIGIN_INDEX, 0);
        playIndex = intent.getIntExtra(PLAY_INDEX, 0);
        entity = (DetailEntity) intent.getBundleExtra(DATA).getSerializable(DATA);
        url = intent.getStringExtra(URL);
        title = intent.getStringExtra(TITLE);
        currentPos = intent.getIntExtra(TIME, 0);
    }

    private void initPlayer() {
        if (player == null) {
            player = new GiraffePlayer(this);
            player.setFullScreenOnly(true);
            player.setScaleType(GiraffePlayer.SCALETYPE_FITPARENT);
            player.onComplete(new Runnable() {
                @Override public void run() {
                    if (entity.type != 1) {
                        playNext();
                    }
                }
            });
        }

        realUrl = entity.vod_url_list.get(originIndex).list.get(playIndex).is_real_url;
        if (realUrl == 0) {

            player.setTitle(title + " " + entity.vod_url_list.get(originIndex).list.get(playIndex).play_name);
            mPresenter.playMove(url, "2", entity.vod_url_list.get(originIndex).list.get(playIndex).play_name);

        } else if (realUrl == 1) {
            url = url.replace("\r\n", "").replace("\t", "").replace(" ", "");
            player.play(url);
            if (currentPos != 0) player.seekTo(currentPos, false);
            isInPlay = true;
        }

//        player.play(url);
//        if (currentPos != 0)
//            player.seekTo(currentPos, false);
    }

    private void playNext() {
        if (canPlayNext()) {
            player.pause();
            mPresenter.playMove(entity.vod_url_list.get(originIndex).list.get(playIndex + 1).play_url, definition,
                                entity.vod_url_list.get(originIndex).list.get(playIndex + 1).play_name);
            player.setTitle(entity.vod_url_list.get(originIndex).list.get(playIndex + 1).play_name);
            playIndex++;
        }
    }

    @Override protected void createPresenter() {
        mPresenter = new PlayPresenter();
    }


    @Override public void playMoveSuccess(MoveAddressEntity entity, String title) {
        Logger.e(new Gson().toJson(entity) + " \n " + title);

        player.play(entity.file);
        if (currentPos != 0) player.seekTo(currentPos, false);
        player.setTitle(this.entity.name + " " + (this.entity.type == 1 ? "" : title));

        type_list = entity.type_list;
        isInPlay = true;
    }

    @Override public void playWebMoveSuccess(String msg) {
        finish();
        Intent intent = new Intent(this, WebVideoActivity.class);
        intent.putExtra(WebVideoActivity.URL, msg);
        startActivity(intent);
    }

    @Override public void playMoveFailed(String msg) {
        finish();
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        isInPlay = false;
    }

    @Override protected void onPause() {
        super.onPause();
        if (isInPlay) saveHistory();
        if (player != null) player.onPause();
    }

    @Override protected void onResume() {
        super.onResume();
        if (player != null) {
            player.onResume();
        }
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.onDestroy();
        }
    }

    private void saveHistory() {
        if (currentPos != 0 && player.getCurrentPosition() == 0) return;
        currentPos = player.getCurrentPosition();
        HistoryEntity historyEntity = new HistoryEntity();
        historyEntity.playName = entity.type == 1 ? "" : entity.vod_url_list.get(originIndex).list.get(
                playIndex).play_name;
        historyEntity.playTime = currentPos;
        historyEntity.originIndex = originIndex;
        historyEntity.playIndex = playIndex;
        historyEntity.name = entity.name;
        historyEntity.id = entity.id;
        historyEntity.pic = entity.pic;
        DBManager.putHistory(historyEntity);
    }

    @Override public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (player != null) {
            player.onConfigurationChanged(newConfig);
        }
    }

    @Override public void onBackPressed() {
        if (player != null && player.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

    @Override public void showLoading() {
        super.showLoading();
        player.showLoading();
        isInPlay = false;
    }
}
