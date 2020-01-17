package edu.tjrac.swant.webview.bean

import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * Created by wpc on 2020-01-13.
 */
class Website(var url: String) : MultiItemEntity,Node(){
    override fun getItemType(): Int {
        return 1
    }

    var cname: String? = null
    var icon_url:String?=null

}
