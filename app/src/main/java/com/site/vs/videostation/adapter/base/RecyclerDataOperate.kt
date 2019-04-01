package com.site.vs.videostation.adapter.base

/**
 * 数据操作接口
 *
 * @author zhangbb
 * @date 2016/10/19
 */
interface RecyclerDataOperate<T> {

    fun add(elem: T)

    fun add(elem: T, index: Int)

    fun addAll(elem: List<T>)

    fun addAll(elem: Set<T>)

    fun replaceAll(elem: List<T>)

    fun remove(elem: T)

    fun removeAll()

}
