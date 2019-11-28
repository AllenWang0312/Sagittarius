package edu.tjrac.swant.meitu.adapter

import android.widget.CheckBox
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import edu.tjrac.swant.meitu.R
import edu.tjrac.swant.meitu.bean.Tab

/**
 * Created by wpc on 2019-11-27.
 */
class TabListAdapter(layoutResId: Int, data: List<Tab>?) : BaseQuickAdapter<Tab, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder, item: Tab) {
        var cb = helper.getView<CheckBox>(R.id.cb)
        cb.text = item.name

    }
}
