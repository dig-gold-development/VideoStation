package com.site.vs.videostation.ui.homepage


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.OnClick
import com.site.vs.videostation.R
import com.site.vs.videostation.base.BaseFragment
import com.site.vs.videostation.entity.ChannelEntity
import com.site.vs.videostation.http.LoadData
import com.site.vs.videostation.http.LoadingHelper
import com.site.vs.videostation.ui.category.CategoryActivity
import com.zhusx.core.adapter.Lib_BaseAdapter
import com.zhusx.core.network.HttpRequest
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

        gridView!!.adapter = object : Lib_BaseAdapter<ChannelEntity.Channel>(data.list) {
            override fun getView(layoutInflater: LayoutInflater, hot: ChannelEntity.Channel, i: Int, view: View?, viewGroup: ViewGroup): View {
                val holder = _getViewHolder(view, viewGroup, R.layout.list_item_channel)
                holder.setText(R.id.tv_name, hot.typename)
                setImageURI(holder.getView(R.id.iv_image), "")
                holder.rootView.setOnClickListener { v ->
                    val intent = Intent(v.context, CategoryActivity::class.java)
                    intent.putExtra(CategoryActivity.EXTRA_STRING_ID, "${hot.typeid}")
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


}
