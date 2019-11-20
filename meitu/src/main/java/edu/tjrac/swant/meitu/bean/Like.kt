package edu.tjrac.swant.meitu.bean

/**
 * Created by wpc on 2019-11-19.
 */
class Like :ColumInfo,ModelInfo{
    override fun getColumCover(): String? {
        return colum?.getColumCover()
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
        return colum?.title
    }

    var id:Int?=0
    var userid:Int?=0
    var modelid:Int?=0

    var model:Model?=null
    var columid:Int?=0

    var colum:Colum?=null
    var releation:String?=""

}