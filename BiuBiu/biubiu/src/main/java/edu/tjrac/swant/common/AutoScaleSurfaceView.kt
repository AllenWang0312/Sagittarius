package edu.tjrac.swant.common

import android.content.Context
import android.content.pm.ActivityInfo
import android.util.AttributeSet
import android.view.SurfaceView
import android.view.WindowManager

/**
 * Created by wpc on 2020-04-13.
 */
class AutoScaleSurfaceView : SurfaceView {
    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val mgr = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val lastOrientation = mgr.defaultDisplay.rotation
        if (lastOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            val height = widthMeasureSpec * 9 / 16
            setMeasuredDimension(widthMeasureSpec, height)
        } else {
            val wm = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
//                        Log.i("width",""+wm.defaultDisplay.width)
            setMeasuredDimension(wm.defaultDisplay.width, wm.defaultDisplay.height)
        }
    }
}
