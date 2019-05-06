package com.site.vs.videostation.ui

import android.app.AlertDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import com.site.vs.videostation.R
import com.site.vs.videostation.base.BaseActivity
import com.site.vs.videostation.ui.homepage.MainChannelFragment
import com.site.vs.videostation.ui.homepage.MainHomeFragment
import com.site.vs.videostation.ui.homepage.MainRankingFragment
import com.zhusx.core.utils._Activitys._addFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    var fragments = arrayOfNulls<Fragment>(4)
    private var currentFragment: Fragment? = null

    var exitDialog: AlertDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            var i = 0
            when (checkedId) {
                R.id.radio_home -> {
                    i = 0
                    if (fragments[i] == null) {
                        fragments[i] = MainHomeFragment()
                    }
                }

                R.id.radio_channel -> {
                    i = 1
                    if (fragments[i] == null) {
                        fragments[i] = MainChannelFragment()
                    }
                }

                R.id.radio_rank -> {
                    i = 2
                    if (fragments[i] == null) {
                        fragments[i] = MainRankingFragment()
                    }
                }

                R.id.mine -> {
//                    CrashReport.testJavaCrash()
                }


            }

            if (fragments[i] == null) {
                fragments[i] = MainHomeFragment()
            }
            showFragment(fragments[i]!!)
        }



        radioGroup.check(R.id.radio_home)
    }


    fun showFragment(fragment: Fragment) {
        if (currentFragment != null) {
            if (currentFragment === fragment) {
                return
            }
        }

        _addFragment(this, R.id.content, currentFragment, fragment)
        currentFragment = fragment


    }

    override fun onBackPressed() {
        if (exitDialog == null) {
            exitDialog = AlertDialog.Builder(this).setTitle(title).setIcon(R.mipmap.ic_launcher)
                    .setMessage("确认退出" + title + "吗？").setNegativeButton("再看看", null)
                    .setPositiveButton("退出") { _, _ -> finish() }.create()

        }
        exitDialog!!.show()
    }
}

