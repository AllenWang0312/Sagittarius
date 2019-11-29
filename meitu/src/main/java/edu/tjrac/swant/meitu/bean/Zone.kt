package edu.tjrac.swant.meitu.bean

import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * Created by wpc on 2019-11-28.
 */
class Zone : MultiItemEntity {
    override fun getItemType(): Int {
        return 1
    }

    var id: Int? = 0
    var type: Int? = 0 //

    var user_id: Int? = 0
    var company_id: Int? = 0
    var group_id: Int? = 0

    var model_id: Int? = 0
    var model: Model? = null
    var album_id: Int? = 0
    var album: Album? = null

    var icon: String? = ""
    var name: String? = ""
    var time: String? = ""
    var cover: String? = ""
    var images: ArrayList<String>? = null
}