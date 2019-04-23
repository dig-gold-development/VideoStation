package com.site.vs.videostation.entity

/**
 * Created by mac on 2019/3/21.
 */
data class HomePageEntity(
        val arts_list: DataListBean,
        val comic_list: DataListBean,
        val korean_list: DataListBean,
        val move_list: DataListBean,
        val slide_list: List<SlideListBean>,
        val tv_list: DataListBean
)

data class DataBean(
        val id: String,
        val pic: String,
        val title: String
)

data class SlideListBean(
        val id: String,
        val litpic: String,
        val title: String
)


data class DataListBean(

        val `data`: List<DataBean>,
        val title: String
)

