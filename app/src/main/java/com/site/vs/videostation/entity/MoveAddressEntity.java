package com.site.vs.videostation.entity;

import java.util.List;

/**
 * @author dxplay120
 * @date 2016/12/19
 */
public class MoveAddressEntity {

    /**
     * success : 1
     * code : 200
     * title : 双生
     * part : 1
     * url : https://iqiyi.com-l-iqiyi.com/20190228/21756_7d95af2b/index.m3u8
     * type : ckm3u8
     * info : [{"flag":"ckm3u8","flag_name":"源1","site":0,"part":1,"video":["HD高清$https://iqiyi.com-l-iqiyi.com/20190228/21756_7d95af2b/index.m3u8$ckm3u8"]},{"flag":"zuidam3u8","flag_name":"源2","site":1,"part":1,"video":["HD1280高清国语中字版$https://bili.meijuzuida.com/20190607/16979_e72740da/index.m3u8$zuidam3u8"]},{"flag":"ckplayer","flag_name":"ckplayer","site":2,"part":1,"video":["第1集$https://cdn.812zy.com/20180601/LD0VuNPw/index.m3u8$ckplayer"]}]
     */

    public int success;
    public int code;
    public String title;
    public int part;
    public String url;
    public String type;
    public List<InfoBean> info;

    public static class InfoBean {
        /**
         * flag : ckm3u8
         * flag_name : 源1
         * site : 0
         * part : 1
         * video : ["HD高清$https://iqiyi.com-l-iqiyi.com/20190228/21756_7d95af2b/index.m3u8$ckm3u8"]
         */

        public String flag;
        public String flag_name;
        public int site;
        public int part;
        public List<String> video;
    }
}
