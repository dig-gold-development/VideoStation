package com.site.vs.videostation.ui.homepage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.TextView


import com.zhusx.core.adapter.Lib_BaseAdapter
import com.zhusx.core.network.HttpRequest

import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.site.vs.videostation.R
import com.site.vs.videostation.base.BaseFragment
import com.site.vs.videostation.entity.ChannelEntity
import com.site.vs.videostation.http.LoadData
import com.site.vs.videostation.http.LoadingHelper
import com.site.vs.videostation.ui.category.CategoryActivity
import com.zhusx.core.network.HttpResult
import kotlinx.android.synthetic.main.fragment_channel.*


/**
 * 频道
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2016/12/16 17:09
 */

class MainChannelFragment : BaseFragment() {

    internal var en: ChannelEntity? = null

   override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_channel, container, false)
    }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)
        val loadData:LoadData<ChannelEntity> = LoadData(LoadData.Api.Channel, this)
        loadData._setOnLoadingListener(object : LoadingHelper<ChannelEntity>(contentView, loadData) {
           override fun __onComplete(request: HttpRequest<Any>, data: HttpResult<ChannelEntity>) {
                initData(data.data)
            }
        })
        loadData._loadData()
    }

    private fun initData(data: ChannelEntity) {
        this.en = data
        tvTv!!.setText("$data.channel_total.tv_total")
        artsTv!!.setText("$data.channel_total.arts_total")
        comicTv!!.setText("$data.channel_total.comic_total")
        movieTv!!.setText("$data.channel_total.move_total")
        setImageURI(movieImg, data.category_bg.move_bg)
        setImageURI(tvImg, data.category_bg.tv_bg)
        setImageURI(comicImg, data.category_bg.comic_bg)
        setImageURI(artsImg, data.category_bg.arts_bg)
        gridView!!.adapter = object : Lib_BaseAdapter<ChannelEntity.Hot>(data.hot_channel) {
            override fun getView(layoutInflater: LayoutInflater, hot: ChannelEntity.Hot, i: Int, view: View, viewGroup: ViewGroup): View {
                val holder = _getViewHolder(view, viewGroup, R.layout.list_item_channel)
                holder.setText(R.id.tv_name, hot.name)
                setImageURI(holder.getView(R.id.iv_image), hot.pic)
                holder.rootView.setOnClickListener { v ->
                    val intent = Intent(v.context, CategoryActivity::class.java)
                    intent.putExtra(CategoryActivity.EXTRA_STRING_ID, "$hot.pid")
                    intent.putExtra(CategoryActivity.EXTRA_STRING_CID, "$hot.id")
                    startActivity(intent)
                }
                return holder.rootView
            }
        }
    }

    @OnClick(R.id.iv_search, R.id.iv_history)
    fun onClick(v: View) {
        when (v.id) {
//            R.id.iv_search -> startActivity(Intent(getActivity(), SearchActivity::class.java))
//            R.id.iv_history -> startActivity(Intent(v.context, HistoryActivity::class.java))
        }
    }

    @OnClick(R.id.llayout_movie, R.id.llayout_tv, R.id.llayout_comic, R.id.llayout_arts)
    fun onCategory(v: View) {
        if (en == null) {
            return
        }
        val intent = Intent(v.context, CategoryActivity::class.java)
        when (v.id) {
            R.id.llayout_movie -> intent.putExtra(CategoryActivity.EXTRA_STRING_ID, "$en!!.lulu_category.move_id")
            R.id.llayout_tv -> intent.putExtra(CategoryActivity.EXTRA_STRING_ID, "$en!!.lulu_category.tv_id")
            R.id.llayout_comic -> intent.putExtra(CategoryActivity.EXTRA_STRING_ID, "$en!!.lulu_category.comic_id")
            R.id.llayout_arts -> intent.putExtra(CategoryActivity.EXTRA_STRING_ID, "$en!!.lulu_category.arts_id")
        }
        startActivity(intent)
    }
}
