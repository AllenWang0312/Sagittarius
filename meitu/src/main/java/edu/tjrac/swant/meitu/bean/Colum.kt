package edu.tjrac.swant.meitu.bean

import edu.tjrac.swant.meitu.Config

/**
 * Created by wpc on 2019-09-05.
 */
class Colum : ColumInfo {
    var id: Int? = 0
    var modelid: Int? = 0

    var title: String? = ""
    var subs: String? = ""
    var org: String? = ""
    var orgid: Int? = 0
    var no: String? = ""
    var nums: Int? = 0
    var time: String? = ""
    var html: String? = ""
    var hot: Int? = 0
    var images: ArrayList<String>? = null

    var get: Boolean? = false


    override fun getColumCover(): String {
//        return
        return Config.URL.FILE_SERVER + "/" + modelid + "/" + id + "/0.jpg"
    }

    override fun getTitleStr(): String {
        return title!!
    }
}