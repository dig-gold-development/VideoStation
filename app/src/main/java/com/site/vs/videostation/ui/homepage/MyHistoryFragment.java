package com.site.vs.videostation.ui.homepage;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.site.vs.videostation.R;
import com.site.vs.videostation.base.BaseFragment;
import com.site.vs.videostation.db.DBManager;
import com.site.vs.videostation.entity.HistoryEntity;
import com.site.vs.videostation.ui.detail.view.DetailActivity;
import com.site.vs.videostation.widget.refreshRecycler.DefaultItemDecoration;
import com.zhusx.core.adapter.Lib_BaseRecyclerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.site.vs.videostation.utils.UnitUtils.secToTime;


/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2016/12/17 16:37
 */

public class MyHistoryFragment extends BaseFragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Lib_BaseRecyclerAdapter<HistoryEntity> adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DefaultItemDecoration(view.getContext(), 1));
        recyclerView.setAdapter(adapter = new Lib_BaseRecyclerAdapter<HistoryEntity>(DBManager.getHistory()) {
            @Override
            protected void __bindViewHolder(_ViewHolder viewHolder, int i, final HistoryEntity s) {
                setImageURI(viewHolder.getView(R.id.iv_image), s.pic);
                viewHolder.setText(R.id.tv_name, s.name + "   " + s.playName);
                viewHolder.setText(R.id.tv_date, "您上次已看到: " + secToTime(s.playTime));
                viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), DetailActivity.class);
                        intent.putExtra("id", s.id);
                        startActivity(intent);
                    }
                });
            }

            @Override
            protected int __getLayoutResource(int i) {
                return R.layout.list_item_history;
            }
        });
    }

    public void clearHistory() {
        if (!adapter._isEmpty()) {
            if (DBManager.clearHistory()) {
                adapter._clearItemToUpdate();
                _showToast("已清空观看记录");
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            adapter._setItemToUpdate(DBManager.getHistory());
        }
    }
}
