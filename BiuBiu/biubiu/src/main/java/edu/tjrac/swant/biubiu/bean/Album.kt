package edu.tjrac.swant.biubiu.bean

/**
 * Created by wpc on 2019-09-05.
 */
class Album : AlbumInfo {
    var id: Int? = 0
    var model_id: Int? = 0
    var model: Model? = null

    var group_id: Int? = 0

    var title: String? = ""
    var subs: String? = ""
    var org: String? = ""
    var group: String? = ""


    var tags: String? = ""


    var no: String? = ""
    var nums: Int? = 0
    var time: String? = ""
    var html: String? = ""

    var images: ArrayList<String>? = null
    var hot: Int? = 0
    var get: Boolean? = false

    var cover: ImageInfo? = null

    override fun getAlbumCover(): String? {
        return cover?.url
//        return Config.URL.FILE_SERVER + "/muri/" + model_id + "/" + id + "/0.jpg"
    }


    override fun getTitleStr(): String {
        return title!!
    }
}