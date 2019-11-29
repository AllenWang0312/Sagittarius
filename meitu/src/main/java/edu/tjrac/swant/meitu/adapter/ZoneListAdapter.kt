package edu.tjrac.swant.meitu.adapter

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import edu.tjrac.swant.meitu.R
import edu.tjrac.swant.meitu.bean.Zone
import edu.tjrac.swant.meitu.bean.ZoneTitle

/**
 * Created by wpc on 2019-11-29.
 */
class ZoneListAdapter(data: MutableList<MultiItemEntity>?) : BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>(data) {
    init {
        addItemType(0, R.layout.item_zone_title)
        addItemType(1,R.layout.item_zone_cyc_album_kuan)
    }

    override fun convert(helper: BaseViewHolder?, item: MultiItemEntity?) {
        if(item is ZoneTitle){

        } else if(item is Zone){

        }
    }

}