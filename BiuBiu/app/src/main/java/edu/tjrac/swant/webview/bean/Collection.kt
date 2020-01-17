package edu.tjrac.swant.webview.bean

import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * Created by wpc on 2020-01-13.
 */
class Collection : MultiItemEntity, Node() {
    override fun getItemType(): Int {
        return 0
    }


}