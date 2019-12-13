package edu.tjrac.swant.biubiu.bean

import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * Created by wpc on 2019-11-29.
 */
class ZoneTitle : MultiItemEntity {
    override fun getItemType(): Int {
        return 0
    }

    var title: String? = ""

    constructor(title: String?) {
        this.title = title
    }

}