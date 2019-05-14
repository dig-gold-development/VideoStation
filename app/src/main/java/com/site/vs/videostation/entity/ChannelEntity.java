package com.site.vs.videostation.entity;


import java.util.List;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2016/12/16 17:40
 */

public class ChannelEntity {

    public List<Channel> hot;
    public List<Channel> channels;

    public static class Channel {
        public String tid;
        public String tname;
        public String total;

    }

}
