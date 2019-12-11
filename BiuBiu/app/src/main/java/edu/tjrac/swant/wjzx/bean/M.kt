package edu.tjrac.swant.wjzx.bean

import com.chad.library.adapter.base.entity.MultiItemEntity
import edu.tjrac.swant.baselib.common.base.BaseApplication
import edu.tjrac.swant.baselib.util.SUtil

class M(var type: Int = 0) : MultiItemEntity {
    override fun getItemType(): Int {
        return type!!
    }

    var title_res_id: Int = 0
    var title: String? = ""
        get() {
            if (SUtil.isEmpty(field)) {
                field = BaseApplication.instance?.resources?.getString(title_res_id)
            }
            return field
        }
    var icon_res: Int? = 0
    var accent_icon_res: Int? = 0
    var checkedable: Boolean = false

    constructor(type: Int, title_res_id: Int, icon_res: Int?) : this() {
        this.type = type
        this.title_res_id = title_res_id
        this.icon_res = icon_res
    }
    constructor(type: Int, title_res: String, icon_res: Int?) : this() {
        this.type = type
        this.title = title_res
        this.icon_res = icon_res
    }

    constructor(type: Int, title_res_id: Int, icon_res: Int?, accent_icon_res: Int?) : this() {
        this.type = type
        this.title_res_id = title_res_id
        this.icon_res = icon_res
        this.accent_icon_res = accent_icon_res
        checkedable = true
    }
}
