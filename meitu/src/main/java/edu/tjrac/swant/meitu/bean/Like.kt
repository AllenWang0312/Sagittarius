package edu.tjrac.swant.meitu.bean

/**
 * Created by wpc on 2019-11-19.
 */
class Like :AlbumInfo,ModelInfo{
    override fun get(): Boolean? {
        return model?.get
    }

    override fun getID(): Int? {
        return modelid
    }

    override fun getAlbumCover(): String? {
        return album?.getAlbumCover()
    }

    override fun getHotCot(): Int? {
       return model?.getHotCot()
    }

    override fun getNameStr(): String? {
        return model?.getNameStr()
    }

    override fun getCoverImg(): String? {
        return model?.getCoverImg()
    }

    override fun getTitleStr(): String? {
        return album?.title
    }

    var id:Int?=0
    var userid:Int?=0
    var modelid:Int?=0

    var model:Model?=null
    var columid:Int?=0

    var album:Album?=null
    var releation:String?=""

}