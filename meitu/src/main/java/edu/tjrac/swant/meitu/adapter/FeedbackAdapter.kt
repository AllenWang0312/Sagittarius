package edu.tjrac.swant.meitu.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import edu.tjrac.swant.meitu.bean.Feedback
import edu.tjrac.swant.meitu.R

/**
 * Created by wpc on 2019-11-18.
 */
class FeedbackAdapter(data: MutableList<Feedback>?) : BaseQuickAdapter<Feedback, BaseViewHolder>(R.layout.item_meitu_feedback, data) {
    override fun convert(helper: BaseViewHolder?, item: Feedback?) {

        helper?.setText(R.id.tv_content, item?.content)
        helper?.setText(R.id.tv_like, "likes:" + item?.likes)

    }

}