package edu.tjrac.swant.biubiu.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import edu.tjrac.swant.biubiu.R
import edu.tjrac.swant.biubiu.bean.Feedback

/**
 * Created by wpc on 2019-11-18.
 */
class FeedbackAdapter(data: ArrayList<Feedback>?) : BaseQuickAdapter<Feedback, BaseViewHolder>(R.layout.item_meitu_feedback, data) {
    override fun convert(helper: BaseViewHolder, item: Feedback?) {

        helper.setText(R.id.tv_content, item?.content)
        helper.setText(R.id.tv_like, "likes:" + item?.likes)

    }

}