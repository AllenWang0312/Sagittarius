package edu.tjrac.swant.wjzx.adapter

import android.graphics.drawable.StateListDrawable
import android.widget.FrameLayout
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import edu.tjrac.swant.wjzx.R
import edu.tjrac.swant.wjzx.bean.M

class MainManuAdapter(data: ArrayList<M>?) : BaseMultiItemQuickAdapter<M, BaseViewHolder>(data) {
    init {
        addItemType(-2, R.layout.deliver)
        addItemType(-1, R.layout.deliver)
        addItemType(0, R.layout.menu_tag)
        addItemType(1, R.layout.menu)
    }

    var lastPosition: Int? = null
    var selectPosition: Int? = 1
        set(value) {
            lastPosition = field
            field = value
        }

    fun select(resId: Int) {
        for (i in 0 until mData.size!!) {
            if (mData[i].title_res_id == resId) {
                selectPosition = i + headerViewsCount
                notifyDataSetChanged()
            }
        }
    }

    override fun convert(helper: BaseViewHolder, item: M?) {
        if (item?.type == 1) {
            helper?.setText(R.id.tv_title, item.title)
            var ll_menu = helper?.getView<FrameLayout>(R.id.ll_menu)
            if (item?.checkedable) {
                helper?.setTextColor(R.id.tv_title, mContext?.resources?.getColor(R.color.text_color_primary)!!)
                val states = StateListDrawable()
                states.addState(
                        intArrayOf(-android.R.attr.state_checked),
                        mContext.resources.getDrawable(item.icon_res!!)
                )
                states.addState(
                        intArrayOf(android.R.attr.state_checked),
                        mContext.resources.getDrawable(item.accent_icon_res!!)
                )
                helper?.setImageDrawable(R.id.iv_icon, states)
                var selected = helper?.position == selectPosition
                helper?.setChecked(R.id.iv_icon, selected)
//                helper?.setVisible(R.id.iv_bg, selected)
                if (selected) {
//                    ll_menu?.background = mContext.resources.getDrawable(R.drawable.nav_item_bg_selected)
                    ll_menu?.setBackgroundColor(0x40ff8b17)
                } else {
                    ll_menu?.setBackgroundColor(0xffffff)
//                    ll_menu?.background = null
                }


            } else {
                helper?.setTextColor(R.id.tv_title, mContext?.resources?.getColor(R.color.text_color_summary)!!)
                helper?.setImageDrawable(R.id.iv_icon, mContext.resources.getDrawable(item.icon_res!!))
            }
        }
    }

}