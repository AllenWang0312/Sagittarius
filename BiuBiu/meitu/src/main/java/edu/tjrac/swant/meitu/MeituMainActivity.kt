package edu.tjrac.swant.meitu

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.design.internal.BottomNavigationItemView
import android.support.design.internal.BottomNavigationMenuView
import android.support.design.widget.BottomNavigationView
import android.support.v4.view.ViewPager
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import edu.tjrac.swant.baselib.common.adapter.FragmentsPagerAdapter
import edu.tjrac.swant.baselib.common.base.BaseActivity
import edu.tjrac.swant.meitu.bean.Tab
import edu.tjrac.swant.meitu.bean.stuct.UserCenter
import edu.tjrac.swant.meitu.fragment.HomeFollowFragment
import edu.tjrac.swant.meitu.fragment.HomeHomeFragment
import edu.tjrac.swant.meitu.net.BR
import edu.tjrac.swant.meitu.net.NESubscriber
import edu.tjrac.swant.meitu.net.Net
import edu.tjrac.swant.meitu.net.RxUtil
import edu.tjrac.swant.meitu.view.mine.FeedbackListActivity
import edu.tjrac.swant.meitu.view.mine.MeituCollectionsActivity
import edu.tjrac.swant.meitu.view.mine.MeituVisitHistroyActivity
import edu.tjrac.swant.meitu.view.mine.SettingActivity
import kotlinx.android.synthetic.main.activity_meitu_main.*
import kotlinx.android.synthetic.main.fragment_meitu_mine.*
import kotlinx.android.synthetic.main.fragment_meitu_mine.view.*


@SuppressLint("RestrictedApi")
class MeituMainActivity : BaseActivity(), BottomNavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener, View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fl_visit -> {
                startActivity(Intent(this, MeituVisitHistroyActivity::class.java))
            }
            R.id.tv_focus -> {
                startActivity(Intent(this, MeituCollectionsActivity::class.java)
                        .putExtra("title", "我的关注"))
            }
            R.id.fl_likes -> {
                startActivity(Intent(this, MeituCollectionsActivity::class.java))
            }
            R.id.fl_feedback -> {
                startActivity(Intent(this, FeedbackListActivity::class.java))
            }
            R.id.iv_setting -> {
                startActivity(Intent(this, SettingActivity::class.java))
            }
//            R.id.iv_cover -> {
//                startActivity(Intent(activity!!, UserInfoActivity::class.java))
//            }
        }
    }

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

    override fun onResume() {
        super.onResume()
        Net.instance.getApiService().getUserCenter()
                .compose(RxUtil.applySchedulers())
                .subscribe(object : NESubscriber<BR<UserCenter>>(this) {
                    override fun onSuccess(t: BR<UserCenter>?) {
                        var user = t?.data?.user
                        Glide.with(this@MeituMainActivity).load(user?.portarit)
                                .apply(RequestOptions().circleCrop()).into(nav_head?.iv_cover!!)
                        cycler?.upDateUserPortrait(user?.portarit!!)
                        tv_name.text = user?.name
                        tv_account.text = user?.account
                    }
                })
    }

    var nav_head: View? = null

    var adapter: FragmentsPagerAdapter? = null
    var cycler: HomeFollowFragment? = null
    var home: HomeHomeFragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meitu_main)
        nav_head = nav_view.getHeaderView(0) as LinearLayout
        nav_head?.tv_focus?.setOnClickListener(this)
        nav_head?.fl_visit?.setOnClickListener(this)
        nav_head?.fl_likes?.setOnClickListener(this)
        nav_head?.fl_feedback?.setOnClickListener(this)
        nav_head?.iv_setting?.setOnClickListener(this)
        nav_head?.iv_cover?.setOnClickListener(this)

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
        cycler = HomeFollowFragment()
        adapter?.addFragment(cycler!!, resources.getString(R.string.cycler))
        home = HomeHomeFragment()
        adapter?.addFragment(home!!, resources.getString(R.string.home))
//        adapter?.addFragment(ModelListFragment(), resources.getString(R.string.model))
//        adapter?.addFragment(HomeHomeFragment(), "home_home")
//        adapter?.addFragment(ColumListFragment(), resources.getString(R.string.album))
//        adapter?.addFragment(MineFragment(), resources.getString(R.string.mine))
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
