package edu.tjrac.swant.common.view

import android.content.Context
import android.graphics.Typeface
import androidx.viewpager.widget.ViewPager
import android.util.AttributeSet
import android.util.Log
import android.widget.RelativeLayout
import android.widget.TextView
import edu.tjrac.swant.baselib.uncom.ColorUtils
import java.lang.ref.WeakReference

/**
 * Created by wpc on 2019-12-12.
 */
open class ColorfulTabRelativeLayout : RelativeLayout, ViewPager.OnPageChangeListener {
    var vp: WeakReference<ViewPager>? = null

    var fp: Int = -1
    var tp: Int = -1
    var process: Float = 0f

    var from: TextView? = null
    var to: TextView? = null
    var position: Int = 0

    fun scrollTo(pos: Int) {
        fp = position
        tp = pos
        vp?.get()?.setCurrentItem(pos, true)
    }

    override fun onPageScrollStateChanged(state: Int) {

    }


    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        this.position = position
        Log.d("onPageScrolled", "pasition:" + position + "positionOffset:" + positionOffset + "positionOffsetPixels:" + positionOffsetPixels)
        var precent: Float? = 0f

        post {
            //            if (positionOffset > 0) {//position -> position+1
            process = position + positionOffset
            if (tp >= 0) {
                from = childs?.get(fp)
                to = childs?.get(tp)
                precent = ColorUtils.getProgress(fp.toFloat(), tp.toFloat(), process)
                from?.setTextColor(ColorUtils.getPrecentColor(selected?.textColor!!, normal?.textColor!!, precent!!))
                from?.setTextSize(selected?.textSize!! + (normal?.textSize!! - selected?.textSize!!) * precent!!)
                to?.setTextColor(ColorUtils.getPrecentColor(normal?.textColor!!, selected?.textColor!!, precent!!))
                to?.setTextSize(normal?.textSize!! + (selected?.textSize!! - normal?.textSize!!) * precent!!)
            } else {
                from = childs?.get(position)
                precent = positionOffset
                if (position + 1 < childs?.size!!) {
                    to = childs?.get(position + 1)
                    to?.setTextColor(ColorUtils.getPrecentColor(normal?.textColor!!, selected?.textColor!!, precent!!))
                    to?.setTextSize(normal?.textSize!! + (selected?.textSize!! - normal?.textSize!!) * precent!!)
                }
                from?.setTextColor(ColorUtils.getPrecentColor(selected?.textColor!!, normal?.textColor!!, precent!!))
                from?.setTextSize(selected?.textSize!! + (normal?.textSize!! - selected?.textSize!!) * precent!!)
            }


//                if (multiColor) {
//                }
//            } else {///position-1 <- position
//                if (position > 1) {
//                    var fp = childs?.get(position)
//                    var tp = childs?.get(position - 1)
//                    fp?.setTextColor(ColorUtils.getPrecentColor(selected?.textColor!!, normal?.textColor!!, -positionOffset))
//                    fp?.setTextSize(selected?.textSize!! + (normal?.textSize!! - selected?.textSize!!) * -positionOffset)
//                    tp?.setTextColor(ColorUtils.getPrecentColor(normal?.textColor!!, selected?.textColor!!, -positionOffset))
//                    tp?.setTextSize(normal?.textSize!! + (selected?.textSize!! - normal?.textSize!!) * -positionOffset)
//                }
//
//            }
        }
        if (position == tp) {
            fp = -1
            tp = -1
        }
    }

    override fun onPageSelected(position: Int) {

    }

    var multiColor = false

    var normal: TabItemState? = null
    var selected: TabItemState? = null
    var itemPadding = 20
    var indicatorHeight = 10
    var indicatorHeightHolder = 20

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {}


    var childs: ArrayList<TextView>? = ArrayList()

    open fun setupWithViewPager(vp: ViewPager) {
        this.vp = WeakReference(vp)
        var adapter = vp.adapter
        if (adapter != null && adapter?.count!! > 0) {
            post {
                for (i in 0 until adapter?.count!!) {
                    var item = TextView(context)
                    item.setId(i + 1);//设置这个View 的id
                    item.setText(adapter?.getPageTitle(i))
                    item.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//加粗
                    var lp = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
                    if (i == 0) {

                        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);//与父容器的左侧对齐
                        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);//与父容器的上侧对齐
//                    lp.leftMargin = 30;

                        item.textSize = selected?.textSize!!
                        item.setTextColor(selected?.textColor!!)
                    } else {
                        lp.addRule(RelativeLayout.RIGHT_OF, childs?.get(i - 1)?.id!!);//设置item3在     //chlidView1的下面
//                    lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);//与父容器的左侧对齐
                        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);//与父容器的上侧对齐
                        lp.leftMargin = itemPadding;

                        item.textSize = normal?.textSize!!
                        item.setTextColor(normal?.textColor!!)
                    }
                    lp.bottomMargin = indicatorHeightHolder
                    item.setLayoutParams(lp);//设置布局参数
                    item.setOnClickListener {
                        //                        vp.setCurrentItem(i, true)
                        scrollTo(i)
                    }
                    childs?.add(item)
                    addView(item)
                }
            }
        }
        vp.addOnPageChangeListener(this)
    }


}
