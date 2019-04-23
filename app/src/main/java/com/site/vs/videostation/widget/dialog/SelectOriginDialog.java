package com.site.vs.videostation.widget.dialog;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;


import com.site.vs.videostation.R;
import com.site.vs.videostation.adapter.base.BaseAdapterHelper;
import com.site.vs.videostation.adapter.base.CommRecyclerAdapter;
import com.site.vs.videostation.entity.DetailEntity;
import com.site.vs.videostation.widget.FrescoImageView;
import com.zhusx.core.app.Lib_BaseDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author dxplay120
 * @date 2016/12/20
 */
public class SelectOriginDialog extends Lib_BaseDialog {
    private int sel = 0;
    private DetailEntity entity;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    OriginSelectedListener listener;

    public SelectOriginDialog(Context context, DetailEntity entity, OriginSelectedListener listener) {
        super(context, R.style.lib_dialog_NoTitle);
        setContentView(R.layout.dialog_select_origin);
        ButterKnife.bind(this);
        _setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.bottom_dialog_anim);
        this.entity = entity;
        this.listener = listener;
        initViews();
    }

    private void initViews() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new CommRecyclerAdapter<DetailEntity.Origin>(getContext(), R.layout.list_item_select_origin, entity.vod_url_list) {
            @Override
            public void onUpdate(BaseAdapterHelper helper, DetailEntity.Origin item, int position) {
                helper.getView(R.id.tv_origin_name).setSelected(sel == position);
                helper.setText(R.id.tv_origin_name, item.sourceName);
//                ((FrescoImageView)helper.getView(R.id.tv_origin_img)).setImageURI(item.origin.img_url);
                helper.setVisible(R.id.iv_sel, sel == position);
                final int temp = position;
                helper.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sel = temp;
                        if (listener != null) {
                            listener.onOriginSelected(sel);
                        }
                        dismiss();
                    }
                });
            }
        });
    }

    public void setSel(int index) {
        sel = index;
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @OnClick({R.id.tv_cancel})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                dismiss();
                break;
        }
    }

    public interface OriginSelectedListener {
        void onOriginSelected(int sel);
    }
}
