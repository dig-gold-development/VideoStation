package com.site.vs.videostation.ui.detail.view;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.site.vs.videostation.R;
import com.site.vs.videostation.base.BaseFragment;
import com.site.vs.videostation.entity.DetailEntity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author dxplay120
 * @date 2016/12/19
 */
public class ProfileFragment extends BaseFragment {

    @BindView(R.id.tv_director)
    TextView directorTv;
    @BindView(R.id.tv_actor)
    TextView actorTv;
    @BindView(R.id.tv_type)
    TextView typeTv;
    @BindView(R.id.tv_year)
    TextView yearTv;
    @BindView(R.id.tv_content)
    TextView contentTv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DetailEntity entity = (DetailEntity) getArguments().getSerializable("data");
        directorTv.setText(entity.director);
        actorTv.setText(entity.actor);
        typeTv.setText(entity.tname);
        contentTv.setText(entity.content);
        if(entity.year == 0)
            yearTv.setVisibility(View.GONE);
        else
            yearTv.setText(entity.year+"");
    }
}
