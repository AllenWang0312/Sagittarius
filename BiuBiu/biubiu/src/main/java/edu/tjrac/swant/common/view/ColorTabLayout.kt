package edu.tjrac.swant.common.view

import android.content.Context
import android.graphics.Canvas
import androidx.viewpager.widget.ViewPager
import android.util.AttributeSet

/**
 * Created by wpc on 2019-12-12.
 */
class ColorTabLayout : ColorfulTabRelativeLayout {









    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        super.onPageScrolled(position, positionOffset, positionOffsetPixels)
        this.process = position+positionOffset
        this.position=position
        invalidate()
    }
    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {}

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

    }

    override fun onDrawForeground(canvas: Canvas?) {
        super.onDrawForeground(canvas)
        //        var rect = Rect()
//        if (offset!! < 0.5) {
//            rect.left = childs?.get(position)?.x!!.toInt()
//            rect.top=paddingBottom!!+indicatorHeightHolder-((indicatorHeightHolder-indicatorHeight)/2)
//            rect.right=
//        } else {
//
//        }
    }
    override fun setupWithViewPager(vp: ViewPager) {
        super.setupWithViewPager(vp)

    }
}
