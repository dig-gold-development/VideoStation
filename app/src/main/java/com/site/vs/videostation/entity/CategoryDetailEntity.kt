package com.site.vs.videostation.entity

/**
 * @author zhangbb
 * @date 2016/12/19
 */
class CategoryDetailEntity {

    var category_list: List<CategoryFilterEntity>? = null
    var area_list: List<String>? = null
    var year_list: List<String>? = null
    var vod_list: List<VideoEntity>? = null
    var vod_count: Int = 0


}


class VideoEntity {
    var id: String? = null
    var name: String? = null
    var pic: String? = null
    var title: String? = null
}
