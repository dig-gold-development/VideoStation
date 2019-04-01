package com.site.vs.videostation.entity

import android.content.Context
import android.content.Intent


import com.site.vs.videostation.ui.detail.view.DetailActivity

import java.io.Serializable

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2016/12/16 17:52
 */

class Move : Serializable {
    var id: String? = null
    var name: String? = null
    var litpic: String? = null
    var title: String? = null
    var year: String? = null
    var area: String? = null
    var actor: String? = null

    fun startActivity(context: Context) {
        val intent = Intent(context, DetailActivity::class.java)
        intent.putExtra(DetailActivity.ID, id)
        context.startActivity(intent)
    }

}
