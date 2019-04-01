package com.site.vs.videostation.entity;

import java.util.List;

/**
 * @author zhangbb
 * @date 2016/12/19
 */
public class CategoryDetailEntity {

    public List<CategoryFilterEntity> category_list;
    public List<String> area_list;
    public List<String> year_list;
    public List<VideoEntity> vod_list;
    public int vod_count;

    public static class VideoEntity {
        public String id;
        public String name;
        public String pic;
        public String title;
    }

}
