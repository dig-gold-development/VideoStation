package com.site.vs.videostation.ui.detail.view;


import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.site.vs.videostation.R;
import com.site.vs.videostation.adapter.base.BaseAdapterHelper;
import com.site.vs.videostation.adapter.base.CommRecyclerAdapter;
import com.site.vs.videostation.base.BaseFragment;
import com.site.vs.videostation.base.RecyclerViewHelper;
import com.site.vs.videostation.entity.DetailEntity;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author dxplay120
 * @date 2016/12/19
 */
public class NearFragment extends BaseFragment implements RecyclerViewHelper.LoadingAndRetryAdapter {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    DetailEntity detailEntity;
    RecyclerViewHelper<DetailEntity.Near> helper = new RecyclerViewHelper<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_near, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        helper.initViews(getActivity(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        detailEntity = (DetailEntity) getArguments().getSerializable("data");
        helper.initDataSuccess(detailEntity.near_list);
    }


    @Override
    public CommRecyclerAdapter createAdapter() {
        return new CommRecyclerAdapter<DetailEntity.Near>(getContext(), R.layout.list_item_near, new ArrayList<DetailEntity.Near>()) {
            @Override
            public void onUpdate(BaseAdapterHelper helper, final DetailEntity.Near item, int position) {
                helper.setText(R.id.tv_name, item.name);

//                FrescoImageView iv = helper.getView(R.id.iv_img);
//                iv.setImageURI(item.pic);


                Glide.with(Objects.requireNonNull(getActivity()))
                        .load(item.pic)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into((ImageView)  helper.getView(R.id.iv_img));

                if (detailEntity.type == 1) {
                    helper.setVisible(R.id.tv_score, true);
                    if (item.year == 0)
                        helper.setText(R.id.tv_mainTitle, item.area);
                    else
                        helper.setText(R.id.tv_mainTitle, item.area + "/" + item.year);
                    helper.setText(R.id.tv_score, item.title);
                } else {
                    helper.setVisible(R.id.tv_score, false);
                    helper.setText(R.id.tv_mainTitle, item.title);
                }

                helper.setText(R.id.tv_subTitle, item.actor);
                helper.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), DetailActivity.class);
                        intent.putExtra(DetailActivity.Companion.getID(), item.id);
                        startActivity(intent);
                    }
                });
            }
        };
    }

    @Override
    public RecyclerView gainRecyclerView() {
        return recyclerView;
    }

    @Override
    public void onRefresh() {

    }
}
