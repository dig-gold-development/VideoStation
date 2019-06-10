package com.site.vs.videostation.ui.browse;

import android.graphics.Rect;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.site.vs.videostation.R;
import com.site.vs.videostation.base.BaseFragment;
import com.site.vs.videostation.db.DBManager;
import com.site.vs.videostation.entity.Move;
import com.zhusx.core.adapter.Lib_BaseRecyclerAdapter;
import com.zhusx.core.utils._Densitys;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2016/12/17 16:35
 */

public class MyFavoriteFragment extends BaseFragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Lib_BaseRecyclerAdapter<Move> adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine_favorite, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(_Densitys.dip2px(parent.getContext(), 5), _Densitys.dip2px(parent.getContext(), 5), _Densitys.dip2px(parent.getContext(), 5), 0);
            }
        });
        recyclerView.setAdapter(adapter = new Lib_BaseRecyclerAdapter<Move>(DBManager.getFavorite()) {
            @Override
            protected void __bindViewHolder(_ViewHolder holder, int i, final Move tv) {
                holder.setText(R.id.tv_message, tv.title);
                holder.setText(R.id.tv_name, tv.name);
                setImageURI(holder.getView(R.id.iv_image), tv.pic);
                holder.rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tv.startActivity(v.getContext());
                    }
                });
            }

            @Override
            protected int __getLayoutResource(int i) {
                return R.layout.list_item_movie;
            }
        });
    }

    public void clearFavorite() {
        if (!adapter._isEmpty()) {
            if (DBManager.clearFavorite()) {
                adapter._clearItemToUpdate();
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            adapter._setItemToUpdate(DBManager.getFavorite());
        }
    }
}
