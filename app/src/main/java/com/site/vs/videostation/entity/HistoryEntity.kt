package com.site.vs.videostation.entity

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2016/12/19 14:32
 */

class HistoryEntity {


    var id: String? = null
    var name: String? = null
    var pic: String? = null
    var title: String? = null

    var playTime: Int = 0

    var originIndex: Int = 0 // 来源
    var playIndex: Int = 0   // 剧集
    var playName: String? = null // 剧集名或者综艺多少期
}
