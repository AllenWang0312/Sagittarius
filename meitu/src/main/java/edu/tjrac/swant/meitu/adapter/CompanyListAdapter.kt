package edu.tjrac.swant.meitu.adapter

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import edu.tjrac.swant.baselib.util.StringUtils
import edu.tjrac.swant.meitu.R
import edu.tjrac.swant.meitu.bean.Company

/**
 * Created by wpc on 2019-11-26.
 */
class CompanyListAdapter(layoutResId: Int, data: MutableList<Company>?) : BaseQuickAdapter<Company, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder?, item: Company?) {
        if(!StringUtils.isEmpty(item?.logo)){
            var iv_cover=helper?.getView<ImageView>(R.id.iv_cover)
            Glide.with(mContext).load(item?.logo).into(iv_cover!!)
        }
        helper?.setText(R.id.tv_name, item?.name)

    }

}