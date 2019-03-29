package com.site.vs.videostation.ui.homepage

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


import com.site.vs.videostation.base.BaseFragment

import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.site.vs.videostation.R
import com.zhusx.core.app.Lib_BaseFragment
import kotlinx.android.synthetic.main.fragment_ranking.*

/**
 * 排行榜
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2016/12/16 17:12
 */

class MainRankingFragment : BaseFragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_ranking, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view!!)
        initView()
    }

    private fun initView() {
        viewPager.adapter = object : FragmentPagerAdapter(childFragmentManager) {
            internal var fragments = arrayOfNulls<Fragment>(4)

            internal var sts = arrayOf("电影", "电视剧", "综艺", "动漫")

            override fun getItem(position: Int): Fragment {
                if (fragments[position] == null) {
                    fragments[position] = RankingFragment()
                    val b = Bundle()
                    when (position) {
                        0 -> b.putInt(Lib_BaseFragment._EXTRA_String_ID, 24)
                        1 -> b.putInt(Lib_BaseFragment._EXTRA_String_ID, 21)
                        2 -> b.putInt(Lib_BaseFragment._EXTRA_String_ID, 23)
                        3 -> b.putInt(Lib_BaseFragment._EXTRA_String_ID, 25)
                    }
                    fragments[position]?.setArguments(b)
                }
                return fragments[position]!!
            }

            override fun getPageTitle(position: Int): CharSequence {
                return sts[position]
            }

            override fun getCount(): Int {
                return 4
            }
        }
        tabLayout.setupWithViewPager(viewPager)
    }

    @OnClick(R.id.iv_search, R.id.iv_history)
    fun onClick(v: View) {
        when (v.id) {
            R.id.iv_search -> {
            }
            R.id.iv_history -> {
            }
        }//                startActivity(new Intent(getActivity(), SearchActivity.class));
        //                startActivity(new Intent(v.getContext(), HistoryActivity.class));
    }
}
