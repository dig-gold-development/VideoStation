package com.site.vs.videostation.ui.homepage


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.OnClick
import com.site.vs.videostation.R
import com.site.vs.videostation.base.BaseFragment
import com.site.vs.videostation.entity.ChannelEntity
import com.site.vs.videostation.http.LoadData
import com.site.vs.videostation.http.LoadingHelper
import com.site.vs.videostation.ui.category.CategoryActivity
import com.site.vs.videostation.ui.search.SearchActivity
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
        ButterKnife.bind(this, view)

        val loadData:LoadData<ChannelEntity> = LoadData(LoadData.Api.Channel, this)
        loadData._setOnLoadingListener(object : LoadingHelper<ChannelEntity>(contentView, loadData) {
           override fun __onComplete(request: HttpRequest<Any>, data: HttpResult<ChannelEntity>) {
                initData(data.data)
            }
        })
        loadData._loadData()
    }

    @OnClick(R.id.iv_search, R.id.iv_history)
    fun onClick(v: View) {
        when (v.id) {
            R.id.iv_search -> startActivity(Intent(activity, SearchActivity::class.java))

        }
    }

    private fun initData(data: ChannelEntity) {
        this.en = data
        if (data.hot.count() == 4) {
            var movieTotal = data.hot[0].total;
            var tvTotal = data.hot[1].total
            var artsTotal = data.hot[2].total
            var comicTotal = data.hot[3].total
            tvMovieTotal.text = "$movieTotal"
            tvTvTotal.text ="$tvTotal"
            tvArtsTotal.text = "$artsTotal"
            tvComicTotal.text = "$comicTotal"

//            setImageURI(ivMovie, data.hot[0].pic)
//            setImageURI(ivTv, data.hot[1].pic)
//            setImageURI(ivComic, data.hot[2].pic)
//            setImageURI(ivArts, data.hot[3].pic)

            layoutMovie.setOnClickListener {
                val intent = Intent(it.getContext(), CategoryActivity::class.java)
                var tid = data.hot[0].tid
                intent.putExtra(CategoryActivity.EXTRA_STRING_ID, "$tid")
                startActivity(intent)
            }

            layoutTv.setOnClickListener {
                val intent = Intent(it.getContext(), CategoryActivity::class.java)
                var tid = data.hot[1].tid
                intent.putExtra(CategoryActivity.EXTRA_STRING_ID, "$tid")
                startActivity(intent)
            }

            layoutArts.setOnClickListener {
                val intent = Intent(it.getContext(), CategoryActivity::class.java)
                var tid = data.hot[2].tid
                intent.putExtra(CategoryActivity.EXTRA_STRING_ID, "$tid")
                startActivity(intent)
            }

            layoutComic.setOnClickListener {
                val intent = Intent(it.getContext(), CategoryActivity::class.java)
                var tid = data.hot[3].tid
                intent.putExtra(CategoryActivity.EXTRA_STRING_ID, "$tid")
                startActivity(intent)
            }

        }

        movieGridView!!.adapter = object : Lib_BaseAdapter<ChannelEntity.Channel>(data.channels) {
            override fun getView(layoutInflater: LayoutInflater, hot: ChannelEntity.Channel, i: Int, view: View?, viewGroup: ViewGroup): View {
                val holder = _getViewHolder(view, viewGroup, R.layout.list_item_channel)
                holder.setText(R.id.tv_name, hot.tname)
                setImageURI(holder.getView(R.id.iv_image), hot.pic)
                holder.rootView.setOnClickListener { v ->
                    val intent = Intent(v.context, CategoryActivity::class.java)
                    intent.putExtra(CategoryActivity.EXTRA_STRING_ID, "${hot.tid}")
                    startActivity(intent)
                }
                return holder.rootView
            }
        }
    }




}
