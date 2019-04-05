package com.site.vs.videostation.adapter;

import android.content.Context;

import com.site.vs.videostation.R;
import com.site.vs.videostation.adapter.base.BaseAdapterHelper;
import com.site.vs.videostation.adapter.base.CommRecyclerAdapter;
import com.site.vs.videostation.entity.CategoryDetailEntity;


/**
 * @author zhangbb
 * @date 2016/12/19
 */
public class CategoryContentAdapter extends CommRecyclerAdapter<CategoryDetailEntity.VideoEntity> {


    public CategoryContentAdapter(Context context, int id) {
        super(context, id);
    }

    @Override
    public void onUpdate(BaseAdapterHelper helper, CategoryDetailEntity.VideoEntity item, int position) {
        helper.setImageUrl(R.id.img_content, item.litpic);
        helper.setText(R.id.tv_name, item.title);
    }
}
