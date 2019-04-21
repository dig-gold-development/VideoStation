package com.site.vs.videostation.ui.detail.view


import android.app.AlertDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.util.Log
import android.view.View
import android.widget.Toast
import butterknife.OnClick
import com.site.vs.videostation.R
import com.site.vs.videostation.base.MVPBaseActivity
import com.site.vs.videostation.db.DBManager
import com.site.vs.videostation.entity.DetailEntity
import com.site.vs.videostation.entity.HistoryEntity
import com.site.vs.videostation.ui.detail.presentation.DetailContract
import com.site.vs.videostation.ui.detail.presentation.DetailPresenter
import com.site.vs.videostation.ui.video.VideoActivity2
import com.site.vs.videostation.utils.UnitUtils
import com.site.vs.videostation.widget.dialog.SelectOriginDialog
import com.zhusx.core.network.Lib_NetworkStateReceiver
import com.zhusx.core.utils._Networks
import kotlinx.android.synthetic.main.activity_detail.*
import java.util.*

/**
 * @author dxplay120
 * @date 2016/12/17
 */
class DetailActivity : MVPBaseActivity<DetailPresenter>(), DetailContract.View, SelectOriginDialog.OriginSelectedListener {
    private var id: String? = null
    private var en: DetailEntity? = null


//    private var id_stickynavlayout_viewpager: ViewPager? = null
//    private  var id_stickynavlayout_indicator:TabLayout? = null

    internal var selectOriginDialog: SelectOriginDialog? = null

    internal var vodListFragment: VodListFragment? = null
    internal var originSel = 0
    internal var history: HistoryEntity? = null
    internal lateinit var receiver: Lib_NetworkStateReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        if (intent != null && intent.getStringExtra(ID) != null) id = intent.getStringExtra(ID)
        mPresenter.getDetail(id)

        receiver = Lib_NetworkStateReceiver()
        receiver.registerNetworkStateReceiver(this)

        backIv.setOnClickListener { finish() }

        playTv.setOnClickListener {
            updateRecord()
            if (Lib_NetworkStateReceiver._Current_NetWork_Status == _Networks.NetType.Wifi) startPlayVideo()
            else AlertDialog.Builder(this@DetailActivity).setMessage("非wifi环境观看视频会消耗流量，是否继续观看？").setPositiveButton(
                    "取消 ", null).setNegativeButton("确定") { _, which -> startPlayVideo() }.create().show()

        }

        collectImg?.setOnClickListener {
            if (en != null) {
                collectImg!!.setImageResource(if (DBManager.swithFavorite(
                                en)) R.drawable.nav_collection_current else R.drawable.nav_collection)
            }
        }

        originTv.setOnClickListener {
            if (selectOriginDialog == null)
                selectOriginDialog = SelectOriginDialog(this, en, this)
            selectOriginDialog?.setSel(originSel)
            selectOriginDialog?.show()
        }


    }

    override fun onOriginSelected(sel: Int) {
        if (originSel != sel) {
            originSel = sel
            if (vodListFragment != null)
                vodListFragment?.setIndex(originSel)
            originTv.text = en!!.vod_url_list[originSel].sourceName

        }
    }


    override fun createPresenter() {
        mPresenter = DetailPresenter()
    }

    override fun getDetailSuccess(entity: DetailEntity) {
        en = entity
        titleTv!!.text = entity.name
        collectImg!!.setImageResource(
                if (DBManager.isFavorite(en!!.id)) R.drawable.nav_collection_current else R.drawable.nav_collection)
        updateRecord()
        coverIv!!.setImageURI(entity.pic)
        backgroundIv!!.setImageUrlWithBlur(entity.pic)
        backgroundIv!!.alpha = 0.5f
        typeTv!!.text = "类型：" + entity.keywords
        areaTv!!.text = "区域：" + entity.area
        originTv.setText(entity.vod_url_list[0].sourceName)

        val lst = getItems(entity)

        id_stickynavlayout_viewpager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
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
        id_stickynavlayout_indicator!!.setupWithViewPager(id_stickynavlayout_viewpager)
        id_stickynavlayout_viewpager.offscreenPageLimit = 3
    }

    private fun updateRecord() {
        if (en != null) {
            history = DBManager.getHistory(en!!.id)
            if (history != null) {
                if (en!!.type == 1) recordTv!!.text = "上次已观看到:" + UnitUtils.secToTime(history!!.playTime)
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
                lst.add(0, Item("影评（" + (if (entity.comment_list != null) entity.comment_list.size else 0) + "）",
                                fragment))

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
        en?.let {
            Log.e(" 视频的URL ","" + it.vod_url_list[originSel].list[0].play_url)
            if (history == null) VideoActivity2.playVideo(this@DetailActivity,
                                                         it.name,
                                                         it.vod_url_list[originSel].list[0].play_url,
                                                         it,
                                                         originSel,
                                                         0,
                                                         0)
            else VideoActivity2.playVideo(this@DetailActivity,
                                         it.name,
                                         it.vod_url_list[history!!.originIndex].list[history!!.playIndex].play_url,
                                         it,
                                         history!!.originIndex,
                                         history!!.playIndex,
                                         history!!.playTime)

        }

    }

    @OnClick(R.id.iv_share)
    fun shareMovieInfo() {
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
