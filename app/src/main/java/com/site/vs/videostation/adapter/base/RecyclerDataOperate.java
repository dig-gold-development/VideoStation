package com.site.vs.videostation.adapter.base;

import java.util.List;
import java.util.Set;

/**
 * 数据操作接口
 *
 * @author zhangbb
 * @date 2016/10/19
 */
public interface RecyclerDataOperate<T> {

    void add(T elem);

    void add(T elem, int index);

    void addAll(List<T> elem);

    void addAll(Set<T> elem);

    void replaceAll(List<T> elem);

    void remove(T elem);

    void removeAll();

}
