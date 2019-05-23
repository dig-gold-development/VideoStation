package com.site.vs.videostation.entity;


import com.zhusx.core.interfaces.IPageData;

import java.util.List;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2016/12/16 17:41
 */

public class RankingEntity implements IPageData<Move> {
    public int page_total;
    public List<Move> list;

    @Override
    public int getTotalPageCount() {
        return page_total;
    }

    @Override
    public List<Move> getListData() {
        return list;
    }
}
