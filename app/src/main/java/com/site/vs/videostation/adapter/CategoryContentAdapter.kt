package com.site.vs.videostation.adapter

import android.content.Context

import com.site.vs.videostation.R
import com.site.vs.videostation.adapter.base.BaseAdapterHelper
import com.site.vs.videostation.adapter.base.CommRecyclerAdapter
import com.site.vs.videostation.entity.CategoryDetailEntity
import com.site.vs.videostation.entity.VideoEntity


/**
 * @author zhangbb
 * @date 2016/12/19
 */
class CategoryContentAdapter(context: Context, id: Int) : CommRecyclerAdapter<VideoEntity>(context, id) {

    override fun onUpdate(helper: BaseAdapterHelper, item: VideoEntity, position: Int) {
        helper.setImageUrl(R.id.img_content, item.pic)
        helper.setText(R.id.tv_name, item.name)
    }
}
