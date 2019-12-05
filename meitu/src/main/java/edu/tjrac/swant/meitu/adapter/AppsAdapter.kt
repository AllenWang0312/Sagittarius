package edu.tjrac.swant.meitu.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import edu.tjrac.swant.meitu.R
import edu.tjrac.swant.meitu.bean.App

/**
 * Created by wpc on 2019-12-05.
 */
class AppsAdapter(data: MutableList<App>?)
    : BaseQuickAdapter<App, BaseViewHolder>(R.layout.item_apps_icon, data) {
    override fun convert(helper: BaseViewHolder?, item: App?) {
        helper?.setText(R.id.tv_name, item?.name)

    }
}