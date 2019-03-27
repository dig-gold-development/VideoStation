package com.site.vs.videostation.adapter;

import android.content.Context;

import com.site.vs.videostation.R;
import com.site.vs.videostation.adapter.base.BaseAdapterHelper;
import com.site.vs.videostation.adapter.base.CommRecyclerAdapter;
import com.site.vs.videostation.entity.SearchResultEntity;


/**
 * @author zhangbb
 * @date 2016/12/19
 */
public class SearchResultAdapter extends CommRecyclerAdapter<SearchResultEntity.SearchResult> {

    public SearchResultAdapter(Context context, int id) {
        super(context, id);
    }

    @Override
    public void onUpdate(BaseAdapterHelper helper, SearchResultEntity.SearchResult item, int position) {
        helper.setImageUrl(R.id.img_content, item.pic);
        helper.setText(R.id.tv_name, item.name);
        helper.setText(R.id.tv_subTitle, item.title);
    }
}
