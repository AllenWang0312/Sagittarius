package edu.tjrac.swant.unicorn

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import edu.tjrac.swant.baselib.util.StringUtils
import edu.tjrac.swant.unicorn.DrawUtil.drawBaseRightBottomText
import edu.tjrac.swant.unicorn.DrawUtil.drawBaseTopCenterText
import edu.tjrac.swant.wjzx.R
import java.util.*


/**
 * Created by wpc on 2018/1/30 0030.
 */

class BaseChartView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    internal var paddingLeft: Int = 0
    internal var paddingRight = 20
    internal var paddingTop = 20
    internal var paddingBottom: Int = 0

    internal var x_colum_num = 10
    internal var y_colum_num = 5


     var linePaint: Paint? = null
     var textPaint: Paint? = null
     var pointPaint: Paint? = null

    internal var textSize = 32
    internal var height: Int = 0
    internal var width: Int = 0
    internal var x_length: Int = 0
    internal var y_length: Int = 0
    internal var x_d: Float = 0.toFloat()
    internal var y_d: Float = 0.toFloat()

    internal var x_v_d: Float = 0.toFloat()
    internal var y_v_d: Float = 0.toFloat()
    internal var x_v_p: Float = 0.toFloat()
    internal var y_v_p: Float = 0.toFloat()

    internal var textcolor_common: Int = 0
    internal var textcolor_Title: Int = 0

    internal var titleTextSize = 32
    internal var left_text_space = 84
    internal var bottom_text_space = 48

    internal var line = false

     var doits: HashMap<Int, ArrayList<ChartValue>>? = null
    internal var lineWidths: HashMap<Int, Int?>? = null
    internal var doitRs: HashMap<Int, Int?>? = null

    internal var x_from = 0f
    internal var x_to: Float = 0.toFloat()
    internal var x_colum: Float = 0.toFloat()
    internal var y_from = 0f
    internal var y_to: Float = 0.toFloat()
    internal var y_colum: Float = 0.toFloat()
    internal var x_title_color: Int = 0
    internal var y_title_color: Int = 0
    internal var x_title = ""
    internal var y_title = ""

    internal fun setColumNum(x_colum_num: Int, y_colum_num: Int) {
        this.x_colum_num = x_colum_num
        this.y_colum_num = y_colum_num
    }

    override fun onDraw(canvas: Canvas) {
        paddingLeft = titleTextSize + left_text_space
        paddingBottom = titleTextSize + bottom_text_space

        textcolor_common = context.resources.getColor(R.color.black)


        linePaint = Paint()
        textPaint = TextPaint()
        pointPaint = Paint()

        textPaint!!.textSize = textSize.toFloat()

        height = canvas.height
        width = canvas.width

        x_length = width - paddingLeft - paddingRight
        y_length = height - paddingTop - paddingBottom

        x_d = (x_length / x_colum_num).toFloat()
        y_d = (y_length / y_colum_num).toFloat()
        x_v_d = (x_to - x_from) / x_colum
        y_v_d = (y_to - y_from) / y_colum

        x_v_p = x_length / (x_to - x_from)
        y_v_p = y_length / (y_to - y_from)

        drawXY(canvas)
        drawTitles(canvas)
        drawLines(canvas)
        super.onDraw(canvas)
    }

     fun drawLines(canvas: Canvas) {
        for (key in doits!!.keys) {
            drawLine(canvas, key, lineWidths?.get(key), doitRs?.get(key), doits?.get(key)!!)
        }
    }

     fun drawLine(canvas: Canvas, color: Int?, width: Int?, doitR: Int?, chartValues: ArrayList<ChartValue>) {
        if (line) {
            linePaint!!.color = color!!
            linePaint!!.strokeWidth = width!!.toFloat()
        }
        pointPaint!!.color = color!!
        for (i in chartValues.indices) {
            val point = getPointF(chartValues[i])
            canvas.drawPoint(point.x, point.y, pointPaint!!)
        }
    }

     fun drawDoit(item: PointF, canvas: Canvas, line: Boolean) {
        if (item.x > paddingLeft && item.x < width - paddingRight
                && item.y > paddingTop && item.y < height - paddingBottom) {
            canvas.drawPoint(item.x, item.y, pointPaint!!)
        }
    }

    fun clean() {
        if (doits != null && doits!!.size > 0) {
            doits!!.clear()
            lineWidths?.clear()
            doitRs?.clear()
        }
    }

     fun drawXY(canvas: Canvas) {
        linePaint!!.strokeWidth = 2f
        //画竖线
        for (i in 0..x_colum_num) {
            canvas.drawLine(paddingLeft + i * x_d, paddingTop.toFloat(), paddingLeft + i * x_d, (height - paddingBottom).toFloat(), linePaint!!)
            drawBaseTopCenterText(canvas, (x_from + i * x_v_d).toString() + "", paddingLeft + i * x_d,
                    (height - paddingBottom + textSize).toFloat(),
                    textPaint!!)

        }
        //画横线
        for (i in 0..y_colum_num) {
            canvas.drawLine(paddingLeft.toFloat(), paddingTop + i * y_d, (width - paddingRight).toFloat(), paddingTop + i * y_d, linePaint!!)
            //            canvas.drawText((y_from+i*y_v_d) + "",
            //                    paddingLeft - 64,
            //                    paddingTop + i * y_d,
            //                    textPaint);
            drawBaseRightBottomText(canvas, (y_to - i * y_v_d).toString() + "",
                    paddingLeft.toFloat(),
                    paddingTop + i * y_d,
                    textPaint!!)
        }
        //        canvas.drawLine();
    }


     fun drawTitles(canvas: Canvas) {
        if (!StringUtils.isEmpty(y_title)) {
            textPaint!!.color = y_title_color
            //            L.i("drawtitles", "y:" + y_title);

            val length = textPaint!!.measureText(y_title)
            val p = Path()
            p.moveTo(titleTextSize.toFloat(), paddingTop.toFloat() + (y_length / 2).toFloat() + length / 2)
            p.lineTo(titleTextSize.toFloat(), paddingTop + y_length / 2 - length / 2)
            canvas.drawTextOnPath(y_title, p, 0f, 0f, textPaint!!)
        }
        if (!StringUtils.isEmpty(x_title)) {
            textPaint!!.color = x_title_color
            //            L.i("drawtitles", "x:" + x_title);
            val length = textPaint!!.measureText(x_title)
            canvas.drawText(x_title, paddingLeft + x_length / 2 - length / 2, height.toFloat(), textPaint!!)
        }
    }

    fun setStyle(build: StyleBuilder) {

        this.line = build.line

        this.doits = build.doits

        this.lineWidths = build.lineWidths
        this.doitRs = build.doitRs

        this.x_from = build.x_from
        this.x_to = build.x_to
        this.x_colum = build.x_colum
        this.x_title_color = build.x_title_color

        this.y_from = build.y_from
        this.y_to = build.y_to
        this.y_colum = build.y_colum

        this.y_title_color = build.y_title_color

        this.x_title = build.x_title
        this.y_title = build.y_title

        invalidate()

    }

    class StyleBuilder {

        internal var doits: HashMap<Int, ArrayList<ChartValue>>? = null
        internal var lineWidths: HashMap<Int, Int?>?=null
        internal var doitRs: HashMap<Int, Int?>?=null

         var line: Boolean = false
         var dashPath: Boolean = false
        internal var x_from = 0f
        internal var x_to: Float = 0.toFloat()
        internal var x_colum: Float = 0.toFloat()
        internal var y_from = 0f
        internal var y_to: Float = 0.toFloat()
        internal var y_colum: Float = 0.toFloat()
        internal var x_title = ""
        internal var y_title = ""
        internal var x_title_color: Int = 0
        internal var y_title_color: Int = 0
        internal var commit = false

        fun setXInfo(from: Float, to: Float, colum: Int, title: String, titleColor: Int): StyleBuilder {
            x_from = from
            x_to = to
            x_colum = colum.toFloat()
            x_title = title
            x_title_color = titleColor
            return this
        }

        fun setYInfo(from: Float, to: Float, colum: Int, title: String, titleColor: Int): StyleBuilder {
            y_from = from
            y_to = to
            y_colum = colum.toFloat()
            y_title = title
            y_title_color = titleColor
            return this
        }

        //        public StyleBuilder commit(){
        //          commit=true;
        //          return this;
        //        }

        fun addLine(color: Int, line: ArrayList<ChartValue>): StyleBuilder {
            return this.addLine(color, 3, 5, line)
        }

        fun addLine(color: Int, lineWidth: Int, doitR: Int, line: ArrayList<ChartValue>): StyleBuilder {
            if (doits == null) {
                doits = HashMap()
                lineWidths = HashMap()
                doitRs = HashMap()
            }
            doits!![color] = line
            lineWidths?.set(color,lineWidth)
            doitRs?.set(color,doitR)
            return this
        }

        fun line(bool: Boolean): StyleBuilder {
            line = bool
            return this
        }

        fun dashPath(b: Boolean): StyleBuilder {
            dashPath = b
            return this
        }
    }


    class ChartValue(var x: Float, var y: Float)


    internal fun getPointF(value: ChartValue): PointF {
        return PointF(paddingLeft + (value.x - x_from) * x_v_p, height.toFloat() - paddingBottom.toFloat() - (value.y - y_from) * y_v_p)
    }
}
