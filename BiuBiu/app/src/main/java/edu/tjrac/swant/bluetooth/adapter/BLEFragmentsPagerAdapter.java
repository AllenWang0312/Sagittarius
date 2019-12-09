package edu.tjrac.swant.bluetooth.adapter;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.tjrac.swant.baselib.common.base.BaseFragment;
import edu.tjrac.swant.bluetooth.view.BLEFragment;
import edu.tjrac.swant.bluetooth.view.ConnectDeviceFragment;


/**
 * Created by wpc on 2016/11/18.
 */
public class BLEFragmentsPagerAdapter extends FragmentPagerAdapter {

    private final List<BaseFragment> mFragments = new ArrayList<>();
//    private final List<String> mFragmentTitles = new ArrayList<>();

    private BLEFragment parent;
    private TabLayout mTabLayout;
    private Context mContext;

    public BLEFragmentsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public BLEFragmentsPagerAdapter(Context context, BLEFragment parent, FragmentManager fm) {
        super(fm);
        this.parent = parent;
        mContext = context;
    }

    public BLEFragmentsPagerAdapter(Context context, BLEFragment parent, FragmentManager fm, TabLayout tablayout) {
        this(context, parent, fm);
        this.mTabLayout = tablayout;
    }

    HashMap<String, Integer> device_map;

    public void addFragment(BaseFragment fragment) {

        mFragments.add(fragment);
//        mTabLayout.addTab(new TabLayout.Tab().setText());

//        mFragmentTitles.add(title);
    }

    @Override
    public Fragment getItem(int i) {
        return mFragments.get(i);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragments.get(position).getTitle();
    }

//    public Fragment getFragmentByTitle(String title) {
//        int i = mFragmentTitles.indexOf(title);
//        return mFragments.get(i);
//    }



    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public void remove(String address) {
        int removeIndex = -1;
        for (int i = 0; i < mFragments.size(); i++) {
            if (mFragments.get(i) instanceof ConnectDeviceFragment) {
                if (((ConnectDeviceFragment) mFragments.get(i)).getDevice().getAddress().equals(address)) {
                    removeIndex = i;
                }
            }
        }
        if (removeIndex >= 0) {
            mFragments.remove(removeIndex);
            notifyDataSetChanged();
        }
    }
}