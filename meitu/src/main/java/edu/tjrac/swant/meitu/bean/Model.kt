package edu.tjrac.swant.meitu.bean

/**
 * Created by wpc on 2019-09-05.
 */
class Model : ModelInfo {
    override fun getID(): Int? {
return id
    }

    override fun getCoverImg(): String? {
        return cover
    }

    override fun getHotCot(): Int? {
        return hot
    }

    override fun getNameStr(): String? {
        return name
    }

    var id: Int? = 0

    var cover: String? = null

    var name: String? = null
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

    var hot: Int? = null
}