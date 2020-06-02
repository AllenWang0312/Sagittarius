package edu.tjrac.swant.webview

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import edu.tjrac.swant.baselib.util.SUtil
import edu.tjrac.swant.webview.bean.Website
import edu.tjrac.swant.wjzx.R

/**
 * Created by wpc on 2020-01-13.
 */
class WebSiteAdapter(data: MutableList<MultiItemEntity>?) : BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>(data) {

    init {
        addItemType(1, R.layout.item_website)

    }

    override fun convert(helper: BaseViewHolder, item: MultiItemEntity?) {
        if (item is Website) {
            helper?.setText(R.id.tv_name, item?.name)
//                ?.setText(R.id.iv_icon,item?.icon_url)
            if (!SUtil.isEmpty(item.icon_url)) {
                var icon = helper?.getView<ImageView>(R.id.iv_icon)
                Glide.with(mContext).load(item?.icon_url).into(icon!!)
            }
        }
    }

}