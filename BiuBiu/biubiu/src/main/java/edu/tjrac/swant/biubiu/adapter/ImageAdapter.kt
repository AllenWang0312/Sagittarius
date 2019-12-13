package edu.tjrac.swant.biubiu.adapter

import android.graphics.drawable.Drawable
import android.widget.FrameLayout
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import edu.tjrac.swant.biubiu.R

/**
 * Created by wpc on 2019/11/21.
 */
class ImageAdapter(data: MutableList<String>?) : BaseQuickAdapter<String, BaseViewHolder>(
        R.layout.iv_100dp, data) {
    override fun convert(helper: BaseViewHolder?, item: String?) {
        var iv_cover = helper?.getView<ImageView>(R.id.iv)
        if(iv_cover!=null)
            Glide.with(mContext).load(item)
//                .into(iv!!)
                .into(object : SimpleTarget<Drawable>() {
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        iv_cover.layoutParams = FrameLayout.LayoutParams(iv_cover.width, iv_cover.width * resource.intrinsicHeight / resource.intrinsicWidth)
                        iv_cover.setImageDrawable(resource)
                    }
                })

    }

}