package com.site.vs.videostation.ui.detail.view;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.site.vs.videostation.R;
import com.site.vs.videostation.adapter.base.BaseAdapterHelper;
import com.site.vs.videostation.adapter.base.CommRecyclerAdapter;
import com.site.vs.videostation.base.BaseFragment;
import com.site.vs.videostation.base.RecyclerViewHelper;
import com.site.vs.videostation.entity.DetailEntity;
import com.site.vs.videostation.entity.Near;
import com.site.vs.videostation.widget.FrescoImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author dxplay120
 * @date 2016/12/19
 */
public class NearFragment extends BaseFragment implements RecyclerViewHelper.LoadingAndRetryAdapter{

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    DetailEntity detailEntity;
    RecyclerViewHelper<Near> helper = new RecyclerViewHelper<>();

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
        helper.initDataSuccess(detailEntity.getNear_list());
    }


    @Override
    public CommRecyclerAdapter createAdapter() {
        return new CommRecyclerAdapter<Near>(getContext(), R.layout.list_item_near, new ArrayList<Near>()) {
            @Override
            public void onUpdate(BaseAdapterHelper helper, final Near item, int position) {
                helper.setText(R.id.tv_name, item.getName());
                FrescoImageView iv = helper.getView(R.id.iv_img);
                iv.setImageURI(item.getPic());
                if (detailEntity.getType() == 1){
                    helper.setVisible(R.id.tv_score, true);
                    if (item.getYear() == 0)
                        helper.setText(R.id.tv_mainTitle, item.getArea());
                    else
                        helper.setText(R.id.tv_mainTitle, item.getArea()+"/"+item.getYear());
                    helper.setText(R.id.tv_score, item.getTitle());
                }else {
                    helper.setVisible(R.id.tv_score, false);
                    helper.setText(R.id.tv_mainTitle, item.getTitle());
                }

                helper.setText(R.id.tv_subTitle, item.getActor());
                helper.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), DetailActivity.class);
                        intent.putExtra(DetailActivity.Companion.getID(), item.getId());
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
