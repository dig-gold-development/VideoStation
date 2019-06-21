package com.site.vs.videostation.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhangbb
 * @date 2016/12/19
 */
public class SearchResultEntity implements Serializable {

    public List<SearchResult> list;
    public int count;

    public static class SearchResult implements Serializable {
        public String id;
        public String name;
        public String pic;
        public String title;
    }
}
