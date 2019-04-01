package com.site.vs.videostation.ui.homepage

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView


import com.site.vs.videostation.base.BaseFragment
import com.site.vs.videostation.entity.Move
import com.site.vs.videostation.entity.RankingEntity
import com.site.vs.videostation.http.LoadData
import com.site.vs.videostation.http.PtrLoadingListener
import com.site.vs.videostation.widget.refreshRecycler.DefaultItemDecoration
import com.zhusx.core.adapter.Lib_BaseRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_ranking_list.*

import butterknife.BindView
import butterknife.ButterKnife
import com.site.vs.videostation.R
import com.zhusx.core.app.Lib_BaseFragment

/**
 * 排行榜子Fragment
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2016/12/17 16:11
 */

class RankingFragment : BaseFragment() {

    internal var type: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater,container,savedInstanceState)
        return inflater!!.inflate(R.layout.fragment_ranking_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view!!)
        type = arguments!!.getInt(Lib_BaseFragment._EXTRA_String_ID, 1)
        initViews()
        initPresent()
    }

    private fun initViews() {
        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerView.addItemDecoration(DefaultItemDecoration(activity, 1))
        recyclerView.adapter = object : Lib_BaseRecyclerAdapter<Move>() {
            override fun __bindViewHolder(viewHolder: Lib_BaseRecyclerAdapter._ViewHolder, i: Int, move: Move) {
                setImageURI(viewHolder.getView(R.id.iv_image), move.litpic)
                viewHolder.setText(R.id.tv_name, move.title)
                viewHolder.setText(R.id.tv_area, move.area + "/" + move.year)
                viewHolder.setText(R.id.tv_actor, move.actor)
                if (type == 1) {
                    viewHolder.setText(R.id.tv_score, move.title)
                } else {
                    viewHolder.getView<View>(R.id.tv_score).visibility = View.GONE
                }
                viewHolder.setText(R.id.tv_ranking, i.toString())
                when (i) {
                    0 -> {
                        viewHolder.setText(R.id.tv_ranking, (i + 1).toString())
                        (viewHolder.getView<View>(R.id.iv_ranking) as ImageView).setImageResource(R.drawable.top_list1)
                    }
                    1 -> {
                        viewHolder.setText(R.id.tv_ranking, (i + 1).toString())
                        (viewHolder.getView<View>(R.id.iv_ranking) as ImageView).setImageResource(R.drawable.top_list2)
                    }
                    2 -> {
                        viewHolder.setText(R.id.tv_ranking, (i + 1).toString())
                        (viewHolder.getView<View>(R.id.iv_ranking) as ImageView).setImageResource(R.drawable.top_list3)
                    }
                    else -> {
                        viewHolder.setText(R.id.tv_ranking, (i + 1).toString())
                        (viewHolder.getView<View>(R.id.iv_ranking) as ImageView).setImageResource(R.drawable.top_list4)
                    }
                }
        //                viewHolder.rootView.setOnClickListener {
        //                    View.OnClickListener { v ->
        //                        move.startActivity(v.context)
        //                    }
        //                }
                viewHolder.rootView.setOnClickListener(object :View.OnClickListener{
                    override fun onClick(v: View?) {
                        move.startActivity(v!!.context)
                    }
                })
            }

            override fun __getLayoutResource(i: Int): Int {
                return R.layout.list_item_ranking_item
            }
        }
    }

    private fun initPresent() {
        val loadData = LoadData<RankingEntity>(LoadData.Api.Rank, this)
        loadData._setOnLoadingListener(PtrLoadingListener(recyclerView, loadData))
        loadData._refreshData(type)
    }
}
