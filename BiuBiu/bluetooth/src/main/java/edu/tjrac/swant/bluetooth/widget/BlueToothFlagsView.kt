package edu.tjrac.swant.bluetooth.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

import edu.tjrac.swant.baselib.util.SUtil

import edu.tjrac.swant.bluetooth.BlueToothHelper
import edu.tjrac.swant.bluetooth.R

/**
 * Created by wpc on 2018/2/2 0002.
 */

class BlueToothFlagsView : View {

    internal var mFlag: Int = 0
    private var mFlags: BooleanArray? = null
    internal var values: Array<String>?=null
    internal var textPaint: Paint?=null

    internal var padding = 32
    internal var textSize = 32


    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun onDraw(canvas: Canvas) {
        values = context.resources.getStringArray(R.array.bluetooth_service_type_flags)
        textPaint = Paint()
        textPaint?.textSize = textSize.toFloat()

        canvas.drawText("00"
                + Integer.toBinaryString(mFlag)
                + " = 0x"
                + SUtil.getHexString(mFlag, 2), padding.toFloat(), (textSize + padding).toFloat(),
                textPaint!!
        )

        mFlags = BlueToothHelper.getFlags(mFlag)
        for (i in mFlags!!.indices) {

            if (mFlags!![5 - i]) {
                textPaint?.color = context.resources.getColor(R.color.black)
                textPaint?.isFakeBoldText = true
            } else {
                textPaint?.color = context.resources.getColor(R.color.gray)
                textPaint?.isFakeBoldText = false
            }
            canvas.drawText(values?.get(i)!!, 100f, (86 + i * 40).toFloat(), textPaint!!)

        }
        super.onDraw(canvas)
    }

    fun setFlag(flag: Int) {
        mFlag = flag
        invalidate()
    }
}
