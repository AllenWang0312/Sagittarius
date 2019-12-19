package edu.tjrac.swant.todo.adapter;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import android.view.View;

/**
 * Created by wpc on 2018/5/16.
 */

public class GroupWebViewPageTransation implements ViewPager.PageTransformer {


    int offset = 200;
    int pageScaleWidth = 100;
    int tanZ = 40;

    @Override
    public void transformPage(@NonNull View page, float position) {
        int width = page.getWidth();
        int height = page.getHeight();
        float scale = 0;
        if (position < -1) { // [负无穷，-1）:当前页面已经滑出左边屏幕，我们已经看不到了
//            page.setScaleX(pageScaleWidth / width);
        } else if (position <= -0.5) { // [-1, 0]：当前页面向左画出，已远离中心位置，但还未滑出左屏幕
//            page.setScaleX(1+(position*(1-pageScaleWidth/width)));
//            page.setTranslationX((0 - position) * (offset));
//            page.setPivotY(width / 2);

//            page.setPivotX(width);
//            page.setRotationY((float) ((position+0.5) * 150));
            page.setTranslationZ((float) (tanZ - (position + 0.5) * tanZ * 2));
            scale = (float) (1.25 + (position + 0.5) * 0.5);

        } else if (position <= 0.5) { // (0,0.5]:下一页面已经进入屏幕，但还在进入并未到达中间位置
//            page.setScaleX(1-(position*(1-pageScaleWidth/width)));
//            page.setTranslationX(position * offset);
//            page.setPivotY(-width / 2);

//            page.setRotationY(0);
            page.setTranslationZ(tanZ);
            scale = 1.25f;
        } else if (position <= 1) { // (1, 正无穷]：下一页面还未进入屏幕

//            page.setPivotX(0);
//            page.setRotationY((float) ((position-0.5) * 150));
            page.setTranslationZ((float) ((position - 0.5) * tanZ * 2) - tanZ);
            scale = (float) (1.25 - (position - 0.5) * 0.5);
        }

        page.setScaleY(scale);
        page.setScaleX(scale);

    }
}
