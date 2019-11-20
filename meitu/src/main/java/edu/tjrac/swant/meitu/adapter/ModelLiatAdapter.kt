package edu.tjrac.swant.meitu.adapter

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import edu.tjrac.swant.meitu.R
import edu.tjrac.swant.meitu.bean.ModelInfo

/**a
 * Created by wpc on 2019-09-05.
 */
class ModelLiatAdapter(var layoutid:Int, data: List<ModelInfo>?)
    : BaseQuickAdapter<ModelInfo, BaseViewHolder>(layoutid, data) {

    override fun convert(helper: BaseViewHolder, item: ModelInfo) {

        var iv_cover = helper.getView<ImageView>(R.id.iv_cover)
        Glide.with(mContext)
                .load(item.getCoverImg())
                .apply(RequestOptions().placeholder(R.mipmap.app_icon))
                .into(iv_cover)

        if(layoutid==R.layout.item_meitu_model){
//            helper.addOnClickListener(R.id.iv_heart)
            helper.setText(R.id.tv_hot,""+item?.getHotCot())
        }

        helper.setText(R.id.tv_title,
//                "" + item.id + "|" +
                item?.getNameStr())
    }
}
