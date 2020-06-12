package edu.tjrac.swant.biubiu.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import edu.tjrac.swant.biubiu.R
import edu.tjrac.swant.biubiu.bean.VisitHistroy

/**
 * Created by wpc on 2019-12-04.
 */
class VisitHistoryAdapter(data: MutableList<VisitHistroy>?)
    : BaseQuickAdapter<VisitHistroy, BaseViewHolder>(R.layout.item_visit_histroy, data) {

    override fun convert(helper: BaseViewHolder, item: VisitHistroy?) {
//        if(null!=item?.album){
//            helper?.setText(R.id.tv_name, item?.album?.title)
//            Glide.with(mContext).load(item?.album?.getAlbumCover())
//        }

        helper ?.setText(R.id.tv_time, item?.date)

    }

}