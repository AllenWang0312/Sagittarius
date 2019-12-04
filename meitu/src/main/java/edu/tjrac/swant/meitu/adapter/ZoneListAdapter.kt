package edu.tjrac.swant.meitu.adapter

import android.widget.LinearLayout
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import edu.tjrac.swant.baselib.util.UiUtil
import edu.tjrac.swant.meitu.R
import edu.tjrac.swant.meitu.bean.Zone
import edu.tjrac.swant.meitu.bean.ZoneTitle

/**
 * Created by wpc on 2019-11-29.
 */
class ZoneListAdapter(data: MutableList<MultiItemEntity>?) : BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>(data) {
    init {
        addItemType(0, R.layout.item_zone_title)
        addItemType(1, R.layout.item_zone_cyc_album_kuan)
    }

    override fun convert(helper: BaseViewHolder?, item: MultiItemEntity?) {
        if (item is ZoneTitle) {
            helper?.setText(R.id.tv_title, item.title)
        } else if (item is Zone) {
            helper?.setText(R.id.tv_name, item.model?.name)
                    ?.setText(R.id.tv_subs, item.album?.time)
                    ?.setText(R.id.tv_content, item.album?.title)

            var ll_content=helper?.getView<LinearLayout>(R.id.ll_content)
            if(null!=item?.album?.images&&item?.album?.images?.size!!>0){
                var dp4=UiUtil.dp2px(mContext,4).toInt()
                UiUtil.inflateGallery(mContext,ll_content,dp4,item.album?.images!!)
            }
        }
    }

}