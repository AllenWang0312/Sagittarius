package edu.tjrac.swant.meitu

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.design.internal.BottomNavigationItemView
import android.support.design.internal.BottomNavigationMenuView
import android.support.design.widget.BottomNavigationView
import android.support.v4.view.ViewPager
import android.view.MenuItem
import edu.tjrac.swant.baselib.common.adapter.FragmentsPagerAdapter
import edu.tjrac.swant.baselib.common.base.BaseActivity
import edu.tjrac.swant.meitu.bean.Tab
import edu.tjrac.swant.meitu.fragment.HomeFollowFragment
import edu.tjrac.swant.meitu.fragment.HomeHomeFragment
import edu.tjrac.swant.meitu.fragment.MineFragment
import edu.tjrac.swant.meitu.net.BR
import edu.tjrac.swant.meitu.net.NESubscriber
import edu.tjrac.swant.meitu.net.Net
import edu.tjrac.swant.meitu.net.RxUtil
import kotlinx.android.synthetic.main.activity_meitu_main.*


@SuppressLint("RestrictedApi")
class MeituMainActivityV2 : BaseActivity(), BottomNavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {

    override fun onPageSelected(position: Int) {
        var menuItem = bnv.getMenu().getItem(position)
        menuItem.setChecked(true)
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
//            R.id.tab_four -> vp.setCurrentItem(3)
        }
        return false
    }

//    var bottomNavigationView: BottomNavigationView? = null
//    private val menuItem: MenuItem? = null

    override fun initStatusBar() {
        super.initStatusBar()
    }

    var adapter: FragmentsPagerAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meitu_main_v2)

//        setSupportActionBar(toolbar)
//        toolbar.setTitle("meituri")
//
//        var layoutparams = toolbar.layoutParams as AppBarLayout.LayoutParams
//        layoutparams.setMargins(0, UiUtil.getStatusBarHeight(this), 0, 0)
//        barlayout.setPadding(0, UiUtil.getStatusBarHeight(this), 0, 0)
        disableShiftMode(bnv);
        bnv.setOnNavigationItemSelectedListener(this);
        vp.addOnPageChangeListener(this);
        bnv.selectedItemId = R.id.tab_one;

        adapter = FragmentsPagerAdapter(supportFragmentManager)
        adapter?.addFragment(HomeHomeFragment(), resources.getString(R.string.home))
//        adapter?.addFragment(ModelListFragment(), resources.getString(R.string.model))
        adapter?.addFragment(HomeFollowFragment(), "cycler")
//        adapter?.addFragment(HomeHomeFragment(), "home_home")

//        adapter?.addFragment(ColumListFragment(), resources.getString(R.string.album))
        adapter?.addFragment(MineFragment(), resources.getString(R.string.mine))
        vp.offscreenPageLimit = 3
        vp.adapter = adapter;

        Net.instance.getApiService().followedTabs()
                .compose(RxUtil.applySchedulers())
                .subscribe(object : NESubscriber<BR<ArrayList<Tab>>>(this) {
                    override fun onSuccess(t: BR<ArrayList<Tab>>?) {

                    }
                })
    }


//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        getMenuInflater().inflate(R.menu.menu_meitu, menu)
//        return true
//    }

    fun disableShiftMode(navigationView: BottomNavigationView) {
        val menuView = navigationView.getChildAt(0) as BottomNavigationMenuView
        try {
            val shiftingMode = menuView.javaClass.getDeclaredField("mShiftingMode")
            shiftingMode.isAccessible = true
            shiftingMode.setBoolean(menuView, false)
            shiftingMode.isAccessible = false

            for (i in 0 until menuView.childCount) {
                val itemView = menuView.getChildAt(i) as BottomNavigationItemView
//                itemView.setShiftingMode(false)
                itemView.setChecked(itemView.itemData.isChecked)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}