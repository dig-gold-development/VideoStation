
package com.site.vs.videostation.ui.detail.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.site.vs.videostation.R;
import com.site.vs.videostation.adapter.base.BaseAdapterHelper;
import com.site.vs.videostation.adapter.base.CommRecyclerAdapter;
import com.site.vs.videostation.base.BaseFragment;
import com.site.vs.videostation.entity.DetailEntity;
import com.site.vs.videostation.ui.video.VideoActivity;
import com.site.vs.videostation.utils.UnitUtils;
import com.site.vs.videostation.widget.refreshRecycler.DividerGridItemDecoration;
import com.zhusx.core.network.Lib_NetworkStateReceiver;
import com.zhusx.core.utils._Networks;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @author dxplay120
 * @date 2016/12/19
 */
public class VodListFragment extends BaseFragment {

    @BindView(R.id.rv_cat)
    RecyclerView catRv;
    @BindView(R.id.rv_urls)
    RecyclerView urlsRv;

    DetailEntity entity;
    CommRecyclerAdapter<DetailEntity.Origin.Play> urlAdapter;
    RecyclerView.Adapter<ViewHolder> catAdapter;
    List<DetailEntity.Origin.Play> selLst;
    int index = -1;
    int sel;
    int selStart;
    int mode = 0;
    int playIndex = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vod_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        entity = (DetailEntity) getArguments().getSerializable("data");
        index = getArguments().getInt("index", 0);
        mode = getArguments().getInt("mode", 0);
        playIndex = getArguments().getInt("play_index", 0);
        catRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        urlsRv.setLayoutManager(new GridLayoutManager(getContext(), 4));
        urlsRv.addItemDecoration(new DividerGridItemDecoration(UnitUtils.dip2px(getContext(), 14.0f), UnitUtils.dip2px(getContext(), 14.0f)));

        if (mode != 0)
            getView().setBackgroundColor(getResources().getColor(R.color.pressBg));
        setIndex(index);
    }

    public void setIndex(final int index) {
        this.index = index;
        selLst = entity.vod_url_list.get(this.index).list;
        if (entity.type == 4) {
            catRv.setVisibility(View.GONE);
            urlsRv.setLayoutManager(new GridLayoutManager(getContext(), 3));
        } else {

            if (selLst.size() < 20) {
                catRv.setVisibility(View.GONE);
                selLst = entity.vod_url_list.get(index).list.subList(0, selLst.size());
            } else {
                int size = selLst.size() / 20;
                if (selLst.size() % 20 != 0)
                    size += 1;
                selLst = entity.vod_url_list.get(index).list.subList(0, 20);

                sel = 0;
                final int finalSize = size;
                catAdapter = new RecyclerView.Adapter<ViewHolder>() {

                    @Override
                    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        return new ViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.list_item_vod_cat, parent, false));
                    }

                    @Override
                    public void onBindViewHolder(final ViewHolder holder, final int position) {
                        final int start = position * 20 + 1;
                        int end = start + 19;
                        if (end > entity.vod_url_list.get(index).list.size())
                            end = entity.vod_url_list.get(index).list.size();
                        ((TextView) (holder.itemView)).setText("" + start + "-" + end);
                        if (mode != 0)
                            ((TextView) (holder.itemView)).setTextColor(getResources().getColorStateList(R.color.vod_cat_dark_selector));

                        if (sel == position)
                            holder.itemView.setSelected(true);
                        else
                            holder.itemView.setSelected(false);
                        final int finalEnd = end;
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                sel = position;
                                v.setSelected(true);
                                notifyDataSetChanged();
                                selLst = entity.vod_url_list.get(index).list.subList(start - 1, finalEnd);
                                selStart = start - 1;
                                urlAdapter.replaceAll(selLst);
                            }
                        });
                    }

                    @Override
                    public int getItemCount() {
                        return finalSize;
                    }
                };
            }
        }
        catRv.setAdapter(catAdapter);
        urlsRv.setAdapter(urlAdapter = new CommRecyclerAdapter<DetailEntity.Origin.Play>(getActivity(),
                R.layout.list_item_vod, selLst) {
            @Override
            public void onUpdate(BaseAdapterHelper helper, final DetailEntity.Origin.Play item, final int position) {
                helper.setText(R.id.tv_name, item.play_name);
                helper.getView(R.id.tv_name).setSelected(position == playIndex);
                helper.setOnClickListener(R.id.tv_name, new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (Lib_NetworkStateReceiver._Current_NetWork_Status == _Networks.NetType.Wifi)
                            playVideo(item, index, position);
                        else {
                            new AlertDialog.Builder(getActivity()).setMessage("非wifi环境观看视频会消耗流量，是否继续观看？").setPositiveButton("取消 ", null).setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    playVideo(item, index, position);
                                }
                            }).create().show();
                        }

                    }
                });
            }

            @Override
            public int getLayoutResId(DetailEntity.Origin.Play item) {
                if (mode != 0)
                    return R.layout.list_item_vod_dark;
                else
                    return R.layout.list_item_vod;
            }
        });


    }

    private void setPlayIndex(int index){
        playIndex = index;
        urlAdapter.notifyDataSetChanged();
    }
    private void playVideo(DetailEntity.Origin.Play item, int index, int position) {
        VideoActivity.playVideo(getActivity(), entity.name,
                item.play_url, entity, index,
                selStart + position, 0);
        setPlayIndex(selStart + position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}

