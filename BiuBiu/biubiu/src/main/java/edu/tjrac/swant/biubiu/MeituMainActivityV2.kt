package edu.tjrac.swant.biubiu

import android.annotation.SuppressLint
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.viewpager.widget.ViewPager
import android.view.MenuItem
import edu.tjrac.swant.baselib.common.adapter.V4FragmentsPagerAdapter
import edu.tjrac.swant.baselib.common.base.BaseActivity
import edu.tjrac.swant.biubiu.bean.Tab
import edu.tjrac.swant.biubiu.net.BR
import edu.tjrac.swant.biubiu.net.NESubscriber
import edu.tjrac.swant.biubiu.net.Net
import edu.tjrac.swant.biubiu.net.RxUtil
import kotlinx.android.synthetic.main.activity_meitu_main_v2.*


@SuppressLint("RestrictedApi")
class MeituMainActivityV2 : BaseActivity(), BottomNavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {

    override fun onPageSelected(position: Int) {
//        var menuItem = bnv.getMenu().getItem(position)
//        menuItem.setChecked(true)
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        when (itemId) {
            R.id.tab_one -> vp.setCurrentItem(0)
            R.id.tab_two -> vp.setCurrentItem(1)
            R.id.tab_three -> vp.setCurrentItem(2)
            R.id.tab_four -> vp.setCurrentItem(3)
        }
        return false
    }

    override fun initStatusBar() {
        super.initStatusBar()
    }

    var adapter: V4FragmentsPagerAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meitu_main_v2)

        vp.addOnPageChangeListener(this);

        vp.offscreenPageLimit = 4
        vp.adapter = adapter;

        Net.instance.getApiService().followedTabs()
                .compose(RxUtil.applySchedulers())
                .subscribe(object : NESubscriber<BR<ArrayList<Tab>>>(this) {
                    override fun onSuccess(t: BR<ArrayList<Tab>>?) {

                    }
                })
    }


}
