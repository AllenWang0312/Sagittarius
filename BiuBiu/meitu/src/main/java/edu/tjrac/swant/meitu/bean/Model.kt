package edu.tjrac.swant.meitu.bean

import edu.tjrac.swant.meitu.Config

/**
 * Created by wpc on 2019-09-05.
 */
class Model : ModelInfo, Tab() {
    override fun get(): Boolean? {
        return get
    }

    override fun getID(): Int? {
        return id
    }

    override fun getCoverImg(): String? {
        return cover
//        return Config.URL.FILE_SERVER + "/murl/cover/" + id + ".jpg"
    }

    fun getPortraitImg(): String? {
//        return cover
        return Config.URL.FILE_SERVER + "/murl/portrait/" + id + ".jpg"
    }

    override fun getHotCot(): Int? {
        return hot
    }

    override fun getNameStr(): String? {
        return name
    }


    var cover: String? = null

    var nicknames: String? = null
    var jobs: String? = null
    var interest: String? = null

    var birthday: String? = null
    var constellation: String? = null
    var height: String? = null
    var weight: String? = null
    var dimensions: String? = null

    //    var cup: String? = null
    var address: String? = null

    var more: String? = null

    var tags: String? = null

    var get: Boolean? = false
}