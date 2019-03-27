package com.site.vs.videostation.entity;

import java.util.List;

/**
 * @author dxplay120
 * @date 2016/12/19
 */
public class MoveAddressEntity {
    public String file;
    public int play_type;
    public List<Definition> type_list;

    public static class Definition {
        public int hd;
        public String type;
    }
}
