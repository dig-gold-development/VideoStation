package com.site.vs.videostation.ui.detail.view;


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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @author dxplay120
 * @date 2016/12/19
 */
public class CommentsFragment extends BaseFragment implements RecyclerViewHelper.LoadingAndRetryAdapter{
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    RecyclerViewHelper<DetailEntity.Comment> helper = new RecyclerViewHelper<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comments, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        helper.initViews(getActivity(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        DetailEntity entity = (DetailEntity) getArguments().getSerializable("data");
        if (entity.comment_list != null && entity.comment_list.size() > 0){
            helper.initDataSuccess(entity.comment_list);
        }
        else {
            helper.initDataSuccess(new ArrayList<DetailEntity.Comment>());
        }
    }

    @Override
    public CommRecyclerAdapter createAdapter() {
        return new CommRecyclerAdapter<DetailEntity.Comment>(getContext(), R.layout.list_item_comment, new ArrayList<DetailEntity.Comment>()) {
            @Override
            public void onUpdate(BaseAdapterHelper helper, DetailEntity.Comment item, int position) {
                helper.setText(R.id.tv_name, item.username);
                helper.setText(R.id.tv_date, item.creat_at);
                helper.setText(R.id.tv_content, item.content);
            }
        };
    }

    @Override
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    public void onRefresh() {

    }
}
