package com.site.vs.videostation.entity

import java.io.Serializable

/**
 * @author dxplay120
 * @date 2016/12/17
 */
class DetailEntity : Serializable {
    var id: String? = null
    var type: Int = 0
    var gold: String? = null
    var year: Int = 0
    var pic: String? = null
    var keywords: String? = null
    var content: String? = null
    var actor: String? = null
    var director: String? = null
    var area: String? = null
    var name: String? = null
    var vod_url_list: List<Origin>? = null
    var near_list: List<Near>? = null
    var comment_list: List<Comment>? = null

}



class Play : Serializable {
    var play_name: String? = null
    var play_url: String? = null
}

class OriginName : Serializable {
    var name: String? = null
    var img_url: String? = null
}



class Origin : Serializable {
    var origin: OriginName? = null
    var list: List<Play>? = null


}

class Comment : Serializable {
    var content: String? = null
    var creat_at: String? = null
    var username: String? = null
}

class Near : Serializable {
    var id: String? = null
    var name: String? = null
    var pic: String? = null
    var title: String? = null
    var year: Int = 0
    var area: String? = null
    var actor: String? = null
}