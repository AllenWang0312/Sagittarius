package edu.tjrac.swant.meitu.adapter

import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import edu.tjrac.swant.meitu.R
import edu.tjrac.swant.meitu.bean.VisitHistroy

/**
 * Created by wpc on 2019-12-04.
 */
class VisitHistoryAdapter(data: MutableList<VisitHistroy>?)
    : BaseQuickAdapter<VisitHistroy, BaseViewHolder>(R.layout.item_visit_histroy, data) {

    override fun convert(helper: BaseViewHolder?, item: VisitHistroy?) {
        helper?.setText(R.id.tv_name, item?.album?.title)
                ?.setText(R.id.tv_name, item?.date)
        Glide.with(mContext).load(item?.album?.getAlbumCover())
    }

}