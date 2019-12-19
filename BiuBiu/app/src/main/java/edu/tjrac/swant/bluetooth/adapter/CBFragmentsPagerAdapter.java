package edu.tjrac.swant.bluetooth.adapter;

import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.CheckBox;

import java.util.ArrayList;

/**
 * Created by wpc on 2016/11/18.
 */
public class CBFragmentsPagerAdapter extends BLEFragmentsPagerAdapter {

    ViewPager vp;
     boolean smooth;

    public CBFragmentsPagerAdapter(FragmentManager fm) {
        super(fm);
        smooth=false;
    }

    public void setSmooth(boolean smooth) {
        this.smooth = smooth;
    }

    public void bindToViewPager(ViewPager vp) {
        this.vp = vp;
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < tabs.size(); i++) {
                    tabs.get(i).setChecked(i == position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    ArrayList<CheckBox> tabs;

    public void setUpWithCheckBoxs(CheckBox... checkBoxs) {

        tabs = new ArrayList<>();
        for (CheckBox item : checkBoxs) {
            tabs.add(item);
        }

        for (int i = 0; i < tabs.size(); i++) {
            final int finalI = i;
            tabs.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox item = tabs.get(finalI);
                    vp.setCurrentItem(finalI,smooth);
                    unCheckOthers(finalI);

                }
            });
        }
    }

    private void unCheckOthers(int index) {
        for (int i = 0; i < tabs.size(); i++) {
            tabs.get(i).setChecked(index == i);
        }
    }
}