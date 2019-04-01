package com.site.vs.videostation.ui.detail.view

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


import com.site.vs.videostation.R
import com.site.vs.videostation.adapter.base.BaseAdapterHelper
import com.site.vs.videostation.adapter.base.CommRecyclerAdapter
import com.site.vs.videostation.base.BaseFragment
import com.site.vs.videostation.entity.DetailEntity
import com.site.vs.videostation.entity.Play
import com.site.vs.videostation.ui.video.VideoActivity
import com.site.vs.videostation.utils.UnitUtils
import com.site.vs.videostation.widget.refreshRecycler.DividerGridItemDecoration
import com.zhusx.core.network.Lib_NetworkStateReceiver
import com.zhusx.core.utils._Networks

import kotlinx.android.synthetic.main.fragment_vod_list.*


/**
 * @author dxplay120
 * @date 2016/12/19
 */
class VodListFragment : BaseFragment() {


    lateinit var entity: DetailEntity
    internal var urlAdapter: CommRecyclerAdapter<Play>? = null
    internal var catAdapter: RecyclerView.Adapter<ViewHolder>? = null
    internal var selLst: List<Play>? = null
    internal var index = -1
    internal var sel: Int = 0
    internal var selStart: Int = 0
    internal var mode = 0
    internal var playIndex = 0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_vod_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        entity = arguments!!.getSerializable("data") as DetailEntity
        index = arguments!!.getInt("index", 0)
        mode = arguments!!.getInt("mode", 0)
        playIndex = arguments!!.getInt("play_index", 0)
        rv_cat!!.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rv_urls!!.layoutManager = GridLayoutManager(context, 4)
        rv_urls!!.addItemDecoration(
                DividerGridItemDecoration(UnitUtils.dip2px(context, 14.0f), UnitUtils.dip2px(context, 14.0f)))

        if (mode != 0) getView()!!.setBackgroundColor(resources.getColor(R.color.pressBg))
        setIndex(index)
    }

    fun setIndex(index: Int) {
        this.index = index
        selLst = entity.vod_url_list!![this.index].list
        if (entity.type == 4) {
            rv_cat!!.visibility = View.GONE
            rv_urls!!.layoutManager = GridLayoutManager(context, 3)
        } else {

            if (selLst!!.size < 20) {
                rv_cat!!.visibility = View.GONE
                selLst = entity.vod_url_list!![index].list!!.subList(0, selLst!!.size)
            } else {
                var size = selLst!!.size / 20
                if (selLst!!.size % 20 != 0) size += 1
                selLst = entity.vod_url_list!![index].list!!.subList(0, 20)

                sel = 0
                val finalSize = size
                catAdapter = object : RecyclerView.Adapter<ViewHolder>() {

                    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                        return ViewHolder(
                                LayoutInflater.from(context).inflate(R.layout.list_item_vod_cat, parent, false))
                    }

                    @SuppressLint("ResourceType")
                    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                        val start = position * 20 + 1
                        var end = start + 19
                        if (end > entity.vod_url_list!![index].list!!.size) end = entity.vod_url_list!![index].list!!.size
                        (holder.itemView as TextView).text = "$start-$end"
                        if (mode != 0) holder.itemView.setTextColor(
                                resources.getColorStateList(R.drawable.vod_cat_dark_selector))

                        if (sel == position) holder.itemView.setSelected(true)
                        else holder.itemView.setSelected(false)
                        val finalEnd = end
                        holder.itemView.setOnClickListener { v ->
                            sel = position
                            v.isSelected = true
                            notifyDataSetChanged()
                            selLst = entity.vod_url_list!![index].list!!.subList(start - 1, finalEnd)
                            selStart = start - 1
                            urlAdapter?.replaceAll(selLst!!)
                        }
                    }

                    override fun getItemCount(): Int {
                        return finalSize
                    }
                }
            }
        }
        rv_cat!!.adapter = catAdapter

        urlAdapter = object : CommRecyclerAdapter<Play>(activity, R.layout.list_item_vod, selLst) {
            override fun onUpdate(helper: BaseAdapterHelper, item: Play, position: Int) {
                helper.setText(R.id.tv_name, item.play_name)
                helper.getView<View>(R.id.tv_name).isSelected = position == playIndex
                helper.setOnClickListener(R.id.tv_name) {
                    if (Lib_NetworkStateReceiver._Current_NetWork_Status == _Networks.NetType.Wifi) playVideo(item,
                                                                                                              index,
                                                                                                              position)
                    else {
                        AlertDialog.Builder(activity).setMessage("非wifi环境观看视频会消耗流量，是否继续观看？")
                                .setPositiveButton("取消 ", null)
                                .setNegativeButton("确定") { dialog, which -> playVideo(item, index, position) }.create()
                                .show()
                    }
                }
            }

            override fun getLayoutResId(item: Play): Int {
                return if (mode != 0) R.layout.list_item_vod_dark
                else R.layout.list_item_vod
            }
        }

        rv_urls!!.adapter = urlAdapter


    }

    private fun setPlayIndex(index: Int) {
        playIndex = index
        urlAdapter?.notifyDataSetChanged()
    }

    private fun playVideo(item: Play, index: Int, position: Int) {
        VideoActivity.playVideo(activity, entity.name, item.play_url, entity, index, selStart + position, 0)
        setPlayIndex(selStart + position)
    }

    internal inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
