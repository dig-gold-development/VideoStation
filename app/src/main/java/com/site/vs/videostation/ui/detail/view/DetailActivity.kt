package com.site.vs.videostation.ui.detail.view

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast


import com.site.vs.videostation.R
import com.site.vs.videostation.base.MVPBaseActivity
import com.site.vs.videostation.db.DBManager
import com.site.vs.videostation.entity.DetailEntity
import com.site.vs.videostation.entity.HistoryEntity
import com.site.vs.videostation.ui.detail.presentation.DetailContract
import com.site.vs.videostation.ui.detail.presentation.DetailPresenter
import com.site.vs.videostation.ui.video.VideoActivity
import com.site.vs.videostation.utils.UnitUtils
import com.site.vs.videostation.widget.FrescoImageView
import com.site.vs.videostation.widget.StickyNavLayout
import com.site.vs.videostation.widget.dialog.SelectOriginDialog
import com.zhusx.core.network.Lib_NetworkStateReceiver
import com.zhusx.core.utils._Networks

import java.util.ArrayList

import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.site.vs.videostation.widget.ShapeTextView
import kotlinx.android.synthetic.main.activity_detail.*

/**
 * @author dxplay120
 * @date 2016/12/17
 */
class DetailActivity : MVPBaseActivity<DetailPresenter>(), DetailContract.View, SelectOriginDialog.OriginSelectedListener {
    private var id: String? = null
    private var en: DetailEntity? = null

    internal var titleTv: TextView? = null
    internal var coverIv: FrescoImageView? = null

    internal var backgroundIv: FrescoImageView? = null
    internal var typeTv: TextView? = null
    internal var areaTv: TextView? = null
    internal var yearTv: TextView? = null
    internal var languageTv: TextView? = null
    internal var originTv: TextView? = null
    internal var originIv: FrescoImageView? = null


    internal var tabLayout: TabLayout? = null
    internal var viewPager: ViewPager? = null
    internal var collectImg: ImageView? = null
    internal var recordTv: TextView? = null
    internal var tipsIv: ImageView? = null
    internal var playTv: ShapeTextView? = null
    internal var loadingLayout: LinearLayout? = null
    internal var contentLayout: StickyNavLayout? = null

    internal var vodListFragment: VodListFragment? = null
    internal var selectOriginDialog: SelectOriginDialog? = null
    internal var originSel = 0
    internal var history: HistoryEntity? = null
    internal lateinit var  receiver: Lib_NetworkStateReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        ButterKnife.bind(this)
        if (intent != null && intent.getStringExtra(ID) != null)
            id = intent.getStringExtra(ID)
        mPresenter.getDetail(id)

        receiver = Lib_NetworkStateReceiver()
        receiver.registerNetworkStateReceiver(this)

        backIv.setOnClickListener(object:View.OnClickListener{
            override fun onClick(v: View?) {
                finish()
            }
        })

        playTv?.setOnClickListener(object:View.OnClickListener{
            override fun onClick(v: View?) {
                updateRecord()
                if (Lib_NetworkStateReceiver._Current_NetWork_Status == _Networks.NetType.Wifi)
                    startPlayVideo()
                else {
                    AlertDialog.Builder(this@DetailActivity).setMessage("非wifi环境观看视频会消耗流量，是否继续观看？").setPositiveButton("取消 ", null).setNegativeButton("确定") { dialog, which -> startPlayVideo() }.create().show()
                }
            }
        })

        collectImg?.setOnClickListener(object:View.OnClickListener{
            override fun onClick(v: View?) {
                if (en != null) {
                    collectImg!!.setImageResource(if (DBManager.swithFavorite(en)) R.drawable.nav_collection_current else R.drawable.nav_collection)
                }
            }
        })

        originTv?.setOnClickListener(object:View.OnClickListener{
            override fun onClick(v: View?) {
                if (selectOriginDialog == null)
                    selectOriginDialog = SelectOriginDialog(this@DetailActivity, en, this@DetailActivity)

                selectOriginDialog!!.setSel(originSel)
                selectOriginDialog!!.show()
            }
        })
    }



    override fun createPresenter() {
        mPresenter = DetailPresenter()
    }

    override fun getDetailSuccess(entity: DetailEntity) {
        en = entity
        titleTv!!.text = entity.name
        collectImg!!.setImageResource(if (DBManager.isFavorite(en!!.id)) R.drawable.nav_collection_current else R.drawable.nav_collection)
        updateRecord()
        coverIv!!.setImageURI(entity.pic)
        backgroundIv!!.setImageUrlWithBlur(entity.pic)
        backgroundIv!!.alpha = 0.5f
        typeTv!!.text = "类型：" + entity.keywords
        areaTv!!.text = "区域：" + entity.area
        originTv!!.text = entity.vod_url_list[0].origin.name
        originIv!!.setImageURI(entity.vod_url_list[0].origin.img_url)
        val lst = getItems(entity)
        viewPager!!.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getCount(): Int {
                return lst.size
            }

            override fun getItem(position: Int): Fragment {
                return lst[position].fragment
            }

            override fun getPageTitle(position: Int): CharSequence {
                return lst[position].title
            }
        }
        tabLayout!!.setupWithViewPager(viewPager)
        viewPager!!.offscreenPageLimit = 3
    }

    private fun updateRecord() {
        if (en != null) {
            history = DBManager.getHistory(en!!.id)
            if (history != null) {
                if (en!!.type == 1)
                    recordTv!!.text = "上次已观看到:" + UnitUtils.secToTime(history!!.playTime)
                else {
                    recordTv!!.text = "上次已观看到:" + history!!.playName
                }
                recordTv!!.visibility = View.VISIBLE
                tipsIv!!.visibility = View.VISIBLE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateRecord()
    }

    override fun getDetailFailed(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        finish()
    }


    private fun getItems(entity: DetailEntity): List<Item> {
        val lst = ArrayList<Item>()
        var fragment: Fragment = ProfileFragment()
        val bundle = Bundle()
        bundle.putSerializable("data", entity)
        fragment.arguments = bundle
        lst.add(Item("简介", fragment))
        when (entity.type) {
            1 -> {
                fragment = CommentsFragment()
                fragment.setArguments(bundle)
                lst.add(0, Item("影评（" + (if (entity.comment_list != null) entity.comment_list.size else 0) + "）", fragment))

                fragment = NearFragment()
                fragment.setArguments(bundle)
                lst.add(0, Item("类似", fragment))
            }
            2, 3 -> {
                fragment = NearFragment()
                fragment.setArguments(bundle)
                lst.add(0, Item("类似", fragment))

                vodListFragment = VodListFragment()
                vodListFragment?.let {
                    it.arguments = bundle
                    lst.add(0, Item("剧集", it))
                }

            }
            4 -> {
                vodListFragment = VodListFragment()
                vodListFragment?.let {
                    it.arguments = bundle
                    lst.add(0, Item("剧集", it))
                }

            }
        }
        return lst
    }


    private fun startPlayVideo() {
        if (history == null)
            VideoActivity.playVideo(this@DetailActivity, en!!.name, en!!.vod_url_list[originSel].list[0].play_url, en, originSel, 0,
                    0)
        else
            VideoActivity.playVideo(this@DetailActivity, en!!.name,
                    en!!.vod_url_list[history!!.originIndex].list[history!!.playIndex].play_url,
                    en, history!!.originIndex, history!!.playIndex,
                    history!!.playTime)
    }

    @OnClick(R.id.iv_share)
    fun shareMovieInfo() {
    }

    override fun onOriginSelected(sel: Int) {
        if (originSel != sel) {
            originSel = sel
            if (vodListFragment != null)
                vodListFragment!!.setIndex(originSel)
            originTv!!.text = en!!.vod_url_list[originSel].origin.name
            originIv!!.setImageURI(en!!.vod_url_list[originSel].origin.img_url)
        }
    }

    override fun showLoading() {
        super.showLoading()
        contentLayout!!.visibility = View.INVISIBLE
        loadingLayout!!.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        super.hideLoading()
        contentLayout!!.visibility = View.VISIBLE
        loadingLayout!!.visibility = View.INVISIBLE
    }

    internal inner class Item(var title: String, var fragment: Fragment)

    override fun onDestroy() {
        super.onDestroy()
        receiver.unRegisterNetworkStateReceiver(this)
    }

    companion object {

        val ID = "id"
    }
}
