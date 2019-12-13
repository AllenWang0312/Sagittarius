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
import edu.tjrac.swant.biubiu.bean.AlbumInfo

/**
 * Created by wpc on 2019-09-05.
 */
class AlbumListAdapter(var layoutId: Int, data: List<AlbumInfo>?)
    : BaseQuickAdapter<AlbumInfo, BaseViewHolder>(layoutId, data) {

    override fun convert(helper: BaseViewHolder, item: AlbumInfo) {
        var iv_cover = helper.getView<ImageView>(R.id.iv_cover)
        Glide.with(mContext).load(item.getAlbumCover())
//                .into(iv_cover)
                .into(object :SimpleTarget<Drawable>(){
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    iv_cover.layoutParams=FrameLayout.LayoutParams(iv_cover.width,iv_cover.width*resource.intrinsicHeight/resource.intrinsicWidth)
                        iv_cover.setImageDrawable(resource)
                    }

                })
        helper.setText(R.id.tv_title,
                item.getTitleStr())
    }
}
