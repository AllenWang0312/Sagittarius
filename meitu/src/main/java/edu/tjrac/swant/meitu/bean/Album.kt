package edu.tjrac.swant.meitu.bean

import edu.tjrac.swant.meitu.Config

/**
 * Created by wpc on 2019-09-05.
 */
class Album : AlbumInfo {
    var id: Int? = 0
    var modelid: Int? = 0

    var title: String? = ""
    var subs: String? = ""

    var group: String? = ""
    var groupid: Int? = 0

    var no: String? = ""
    var nums: Int? = 0
    var time: String? = ""
    var html: String? = ""

    var get: Boolean? = false
    var images: ArrayList<String>? = null
    var hot: Int? = 0


    override fun getAlbumCover(): String {
//        return
        return Config.URL.FILE_SERVER + "/muri/" + modelid + "/" + id + "/0.jpg"
    }

    override fun getTitleStr(): String {
        return title!!
    }
}