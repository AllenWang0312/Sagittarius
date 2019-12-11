package edu.tjrac.swant.meitu.adapter

import android.app.Activity
import android.content.Intent
import android.support.v4.app.Fragment
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import edu.tjrac.swant.meitu.BuildConfig
import edu.tjrac.swant.meitu.R
import edu.tjrac.swant.meitu.bean.Model
import edu.tjrac.swant.meitu.view.ModelInfoActivity

/**a
 * Created by wpc on 2019-09-05.
 */
class ModelLiatPortraitAdapter
    : BaseQuickAdapter<Model, BaseViewHolder> {

    constructor (layoutid: Int, data: List<Model>?) : super(layoutid, data) {

        this?.setOnItemClickListener { ad, view, position ->
            var item = data?.get(position)
            if (mContext is Activity||mContext is Fragment) {
                mContext.startActivity(Intent(mContext!!, ModelInfoActivity::class.java)
                        .putExtra("model_id", item?.getID())
                        .putExtra("get", item?.get())
                )
            }
//            BaseWebViewActivity.start(activity!!, item?.name!!, "https://m.meituri.com/t/" + item.id + "/")
        }
    }

    override fun convert(helper: BaseViewHolder, item: Model) {

        var iv_cover = helper.getView<ImageView>(R.id.iv_cover)
        Glide.with(mContext)
                .load(item.getPortraitImg())
                .apply(RequestOptions().placeholder(R.mipmap.app_icon))
                .into(iv_cover)
//                .into(object : SimpleTarget<Drawable>() {
//                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
//                        iv_cover.layoutParams = FrameLayout.LayoutParams(iv_cover.width, iv_cover.width * resource.intrinsicHeight / resource.intrinsicWidth)
//                        iv_cover.setImageDrawable(resource)
//                    }
//
//                })

        helper.setText(R.id.tv_title,
                if(BuildConfig.DEBUG){
                    "" + item.getID() + "|"
                }else{
                    ""
                }+
                        item?.getNameStr())
    }
}
