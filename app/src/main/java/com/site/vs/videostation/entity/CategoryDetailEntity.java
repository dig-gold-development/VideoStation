package com.site.vs.videostation.entity;

import java.util.List;

/**
 * @author zhangbb
 * @date 2016/12/19
 */
public class CategoryDetailEntity {

    public int count;
    public List<VideoEntity> list;

    public static class VideoEntity {
        public String id;
        public String title;
        public String litpic;

    }

}
