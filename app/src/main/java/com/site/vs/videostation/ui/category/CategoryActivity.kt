package com.site.vs.videostation.ui.category

import android.os.Bundle
import butterknife.ButterKnife
import com.site.vs.videostation.R
import com.site.vs.videostation.base.MVPBaseActivity
import com.site.vs.videostation.entity.CategoryDetailEntity
import com.site.vs.videostation.widget.loadingtips.DefaultLoadingLayout
import com.site.vs.videostation.widget.loadingtips.SmartLoadingLayout
import kotlinx.android.synthetic.main.activity_category_homepage.*
import java.util.*

/**
 * @author zhangbb
 * @date 2016/12/19
 */
class CategoryActivity : MVPBaseActivity<CategoryPresenter>(), CategoryDetailContract.View, CategoryFragment.FilterChangeListener {

    private var categoryFragment: CategoryFragment? = null
    private var tipsLayout: DefaultLoadingLayout? = null
    private var paraMap: MutableMap<String, String>? = null

    private var id: String? = null
    private var cid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_homepage)
        ButterKnife.bind(this)
        tipsLayout = SmartLoadingLayout.createDefaultLayout(this, flayout_category)
        categoryFragment = CategoryFragment()
        supportFragmentManager.beginTransaction().add(R.id.flayout_category, categoryFragment!!).commit()
        categoryFragment!!.setChangeListener(this)
        initData()
    }

    private fun initData() {
        showLoading()
        id = intent.getStringExtra(EXTRA_STRING_ID)
        cid = intent.getStringExtra(EXTRA_STRING_CID)

        when (Integer.parseInt(id!!)) {
            2 -> tv_title!!.text = "电视剧"
            3 -> tv_title!!.text = "动漫"
            4 -> tv_title!!.text = "综艺"
        }

        paraMap = HashMap()
        paraMap!!["cid"] = if (cid == null) "" else cid!!
        paraMap!!["year"] = ""
        paraMap!!["area"] = ""
        mPresenter.initCategoryBy(id, paraMap, 1, true)
    }

    override fun createPresenter() {
        mPresenter = CategoryPresenter()
        mPresenter.attachView(this)
    }

    override fun initCategorySuccess(entity: CategoryDetailEntity) {
        tipsLayout!!.onDone()
        categoryFragment!!.setHeaderData(entity, cid)
        initCategoryContentSuccess(entity)
    }

    override fun initCategoryContentSuccess(entity: CategoryDetailEntity?) {
        if (entity != null && entity.list != null) {
            categoryFragment!!.updateList(entity)
        }
    }

    override fun loadMoreVideoSuccess(entity: CategoryDetailEntity) {
        categoryFragment!!.appendList(entity)
    }

    override fun loadMoreVideoFail() {
        categoryFragment!!.loadMoreDataFail()
    }

    override fun initCategoryFail() {
        tipsLayout!!.onError()
        tipsLayout!!.setErrorButtonListener { mPresenter.initCategoryBy(id, paraMap, 1, true) }
    }

    override fun showLoading() {
        tipsLayout!!.onLoading()
    }

    override fun hideLoading() {
        tipsLayout!!.onDone()
    }

    override fun filterChange(map: MutableMap<String, String>) {
        this.paraMap = map
        mPresenter.initCategoryBy(id, map, 1, false)
    }

    override fun loadMore(pageIndex: Int) {
        if (paraMap != null)
            mPresenter.loadMoreVideo(id, paraMap, pageIndex)
    }

    fun searchMovies() {
        //        startActivity(new Intent(this, SearchActivity.class));
    }

    companion object {

        val EXTRA_STRING_ID = "extra_id"
        val EXTRA_STRING_CID = "extra_cid"
    }
}
