package edu.tjrac.swant.meitu.adapter

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import edu.tjrac.swant.meitu.R

/**
 * Created by wpc on 2019/11/21.
 */
class ImageAdapter(data: MutableList<String>?) : BaseQuickAdapter<String, BaseViewHolder>(R.layout.iv_100dp, data) {
    override fun convert(helper: BaseViewHolder?, item: String?) {
        var iv = helper?.getView<ImageView>(R.id.iv)
        Glide.with(mContext).load(item).into(iv!!)

    }

}