package com.site.vs.videostation.entity;

import java.util.List;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2016/12/16 17:40
 */

public class ChannelEntity {
    public Category lulu_category;
    public Channel channel_total;
    public List<Hot> hot_channel;
    public Bg category_bg;

    public static class Channel {
        public int move_total;
        public int tv_total;
        public int comic_total;
        public int arts_total;
    }

    public static class Hot {
        public String id;
        public String name;
        public String pid;
        public String pic;
    }

    public static class Bg {
        public String move_bg;
        public String tv_bg;
        public String arts_bg;
        public String comic_bg;
    }

}
