package com.site.vs.videostation.ui.homepage

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.site.vs.videostation.R


import com.site.vs.videostation.base.BaseFragment
import com.site.vs.videostation.entity.HomePageEntity
import com.site.vs.videostation.http.LoadData
import com.site.vs.videostation.http.LoadingHelper
import com.site.vs.videostation.widget.FrescoImageView
import com.zhusx.core.adapter.Lib_BaseAdapter
import com.zhusx.core.adapter.Lib_BasePagerAdapter
import com.zhusx.core.network.HttpRequest
import com.zhusx.core.network.HttpResult
import com.zhusx.core.utils._Lists
import kotlinx.android.synthetic.main.fragment_homepage.*

/**
 * Created by yangang on 2018/1/18.
 */

class MainHomeFragment : BaseFragment() {
    private var loadData: LoadData<HomePageEntity>? = null
    private var data: HomePageEntity? = null


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_homepage, container, false)


    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        println("content = $content")
        loadData = LoadData(LoadData.Api.Home, this)
        loadData!!._setOnLoadingListener(object : LoadingHelper<HomePageEntity>(content, loadData) {
            override fun __onComplete(httpRequest: HttpRequest<Any>, data: HttpResult<HomePageEntity>) {
                initData(data.data)
            }


        })
        loadData!!._loadData()
    }

    private fun initData(data: HomePageEntity) {
        content!!.visibility = View.VISIBLE
        this.data = data
        val lp = topLayout!!.layoutParams
        lp.height = _getFullScreenWidth() * 350 / 750
        lp.width = _getFullScreenWidth()
        topLayout!!.layoutParams = lp

        viewPager!!.adapter = object : Lib_BasePagerAdapter<HomePageEntity.SlideListBean>(activity, data.slide_list) {
            override fun getView(layoutInflater: LayoutInflater, i: Int, slide: HomePageEntity.SlideListBean, view: View?, viewGroup: ViewGroup): View {
                var view = view
                if (view == null) {
                    view = FrescoImageView(layoutInflater.context)
                }
                (view as FrescoImageView).setImageURI(slide.litpic)
                view.setOnClickListener { }
                return view
            }
        }

        if (!_Lists.isEmpty(data.slide_list)) {
            topMessageTv!!.text = data.slide_list[0].title
        }

        viewPager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                topMessageTv!!.text = data.slide_list[viewPager!!.currentItem].title
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

        indicatorView!!._setViewPager(viewPager)

        gridView!!.adapter = object : Lib_BaseAdapter<HomePageEntity.DataListBean.DataBean>(data.move_list.data) {
            override fun getView(layoutInflater: LayoutInflater, tv: HomePageEntity.DataListBean.DataBean, i: Int, view: View?, viewGroup: ViewGroup): View {
                val holder = _getViewHolder(view, viewGroup, R.layout.list_item_movie)
                holder.setText(R.id.tv_message, if (TextUtils.isEmpty(tv.title)) "" else tv.title)
                holder.setText(R.id.tv_name, tv.title)
                setImageURI(holder.getView(R.id.iv_image), tv.litpic)

                return holder.rootView
            }
        }

        gridView1!!.adapter = object : Lib_BaseAdapter<HomePageEntity.DataListBean.DataBean>(data.tv_list.data) {
            override fun getView(layoutInflater: LayoutInflater, tv: HomePageEntity.DataListBean.DataBean, i: Int, view: View?, viewGroup: ViewGroup): View {
                val holder = _getViewHolder(view, viewGroup, R.layout.list_item_movie)
                holder.setText(R.id.tv_message, if (TextUtils.isEmpty(tv.title)) "" else tv.title)
                holder.setText(R.id.tv_name, tv.title)
                setImageURI(holder.getView(R.id.iv_image), tv.litpic)

                return holder.rootView
            }
        }

        gridView2!!.adapter = object : Lib_BaseAdapter<HomePageEntity.DataListBean.DataBean>(data.arts_list.data) {
            override fun getView(layoutInflater: LayoutInflater, tv: HomePageEntity.DataListBean.DataBean, i: Int, view: View?, viewGroup: ViewGroup): View {
                val holder = _getViewHolder(view, viewGroup, R.layout.list_item_movie)
                holder.setText(R.id.tv_message, if (TextUtils.isEmpty(tv.title)) "" else tv.title)
                holder.setText(R.id.tv_name, tv.title)
                setImageURI(holder.getView(R.id.iv_image), tv.litpic)

                return holder.rootView
            }
        }
//
        gridView3!!.adapter = object : Lib_BaseAdapter<HomePageEntity.DataListBean.DataBean>(data.comic_list.data) {
            override fun getView(layoutInflater: LayoutInflater, tv: HomePageEntity.DataListBean.DataBean, i: Int, view: View?, viewGroup: ViewGroup): View {
                val holder = _getViewHolder(view, viewGroup, R.layout.list_item_movie)
                holder.setText(R.id.tv_message, if (TextUtils.isEmpty(tv.title)) "" else tv.title)
                holder.setText(R.id.tv_name, tv.title)
                setImageURI(holder.getView(R.id.iv_image), tv.litpic)

                return holder.rootView
            }
        }


    }
}

