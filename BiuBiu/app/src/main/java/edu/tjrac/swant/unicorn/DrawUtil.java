package edu.tjrac.swant.unicorn;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by wpc on 2018/2/3 0003.
 * 自定义视图时 draw text 对齐工具
 */

public class DrawUtil {
    //右下定点对齐x,y
    public static void drawBaseRightBottomText(Canvas canvas, String s, float x, float y, Paint textPaint) {
        float length = textPaint.measureText(s);
        canvas.drawText(s, x - length, y, textPaint);
    }
    //文字右边中间对齐x,y
    public static void drawBaseRightCenterText(Canvas canvas, String s, float x, float y, Paint textPaint) {
        float length = textPaint.measureText(s);
        canvas.drawText(s, x - length, y+textPaint.getTextSize()/2, textPaint);
    }
    //文字中间对齐x,y
    public static void drawBaseTopCenterText(Canvas canvas, String s, float x, float y, Paint textPaint) {
        float length = textPaint.measureText(s);
        canvas.drawText(s, x - length/2, y+textPaint.getTextSize(), textPaint);
    }
}
