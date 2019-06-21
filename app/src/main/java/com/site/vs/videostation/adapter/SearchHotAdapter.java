package com.site.vs.videostation.adapter;

import android.content.Context;
import android.widget.TextView;


import com.site.vs.videostation.R;
import com.site.vs.videostation.adapter.base.BaseAdapterHelper;
import com.site.vs.videostation.adapter.base.CommRecyclerAdapter;

/**
 * @author zhangbb
 * @date 2016/12/19
 */
public class SearchHotAdapter extends CommRecyclerAdapter<String> {

    private Context context;
    private int[] tvColor = new int[]{R.color.c_e1, R.color.c_fa, R.color.c_fb};

    public SearchHotAdapter(Context context, int id) {
        super(context, id);
        this.context = context;
    }

    @Override
    public void onUpdate(BaseAdapterHelper helper, String item, int position) {
        helper.setText(R.id.tv_history, item);
        TextView numTv = helper.getView(R.id.tv_num);
        numTv.setTextColor(context.getResources().getColor(tvColor[position % 3]));
        numTv.setText(String.valueOf(position + 1));
    }
}
