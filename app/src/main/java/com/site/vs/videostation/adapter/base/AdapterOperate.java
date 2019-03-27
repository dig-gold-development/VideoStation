package com.site.vs.videostation.adapter.base;

/**
 * @author zhangbb
 * @date 2016/10/19
 */
public interface AdapterOperate<T> {

    void onUpdate(BaseAdapterHelper helper, T item, int position);

    int getLayoutResId(T item);

}
