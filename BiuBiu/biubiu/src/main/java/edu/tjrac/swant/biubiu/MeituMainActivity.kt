package edu.tjrac.swant.biubiu

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.viewpager.widget.ViewPager
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import edu.tjrac.swant.baselib.common.adapter.V4FragmentsPagerAdapter
import edu.tjrac.swant.baselib.common.base.BaseFragmentActivity
import edu.tjrac.swant.baselib.util.UiUtil
import edu.tjrac.swant.biubiu.bean.Tab
import edu.tjrac.swant.biubiu.bean.stuct.UserCenter
import edu.tjrac.swant.biubiu.net.BR
import edu.tjrac.swant.biubiu.net.NESubscriber
import edu.tjrac.swant.biubiu.net.Net
import edu.tjrac.swant.biubiu.net.RxUtil
import edu.tjrac.swant.biubiu.view.MeituSearchActivity
import edu.tjrac.swant.biubiu.view.account.FeedbackListActivity
import edu.tjrac.swant.biubiu.view.account.MeituLoginActivity
import edu.tjrac.swant.biubiu.view.home.ScopeFragments
import edu.tjrac.swant.biubiu.view.main.HomeFragment
import edu.tjrac.swant.biubiu.view.mine.MeituCollectionsActivity
import edu.tjrac.swant.biubiu.view.mine.MeituVisitHistroyActivity
import edu.tjrac.swant.biubiu.view.phone.PhoneFragments
import edu.tjrac.swant.biubiu.view.trend.TrendFragments
import kotlinx.android.synthetic.main.activity_meitu_main.*
import kotlinx.android.synthetic.main.fragment_meitu_mine.iv_cover
import kotlinx.android.synthetic.main.fragment_meitu_mine.tv_account
import kotlinx.android.synthetic.main.fragment_meitu_mine.tv_name
import kotlinx.android.synthetic.main.fragment_meitu_mine.view.fl_feedback
import kotlinx.android.synthetic.main.fragment_meitu_mine.view.fl_likes
import kotlinx.android.synthetic.main.fragment_meitu_mine.view.fl_visit
import kotlinx.android.synthetic.main.fragment_meitu_mine.view.iv_cover
import kotlinx.android.synthetic.main.fragment_meitu_mine.view.iv_setting
import kotlinx.android.synthetic.main.fragment_meitu_mine.view.tv_focus
import kotlinx.android.synthetic.main.nav_header_meitu_main.*
import kotlinx.android.synthetic.main.nav_header_meitu_main.view.*
import kotlinx.android.synthetic.main.search_view.*


@SuppressLint("RestrictedApi")
@Route(path = "/module/biubiu/main" )
class MeituMainActivity : BaseFragmentActivity(),
//        BottomNavigationView.OnNavigationItemSelectedListener,
        ViewPager.OnPageChangeListener, View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.bt_login -> {
                finish()
                startActivity(Intent(this, MeituLoginActivity::class.java))
            }
            R.id.iv_portrait -> {
                drawer_layout.openDrawer(Gravity.START)
            }
            R.id.fl_search -> {
                startActivity(Intent(this, MeituSearchActivity::class.java))
            }
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
            R.id.fl_test->{
                ARouter.getInstance().build("/app/demo")
                        .navigation()
            }
            R.id.iv_setting -> {
                ARouter.getInstance().build("/biubiu/setting")
                        .navigation()
//                startActivity(Intent(this, SettingActivity::class.java))
            }
//            R.id.iv_cover -> {
//                startActivity(Intent(activity!!, UserInfoActivity::class.java))
//            }
        }
    }

    override fun onPageSelected(position: Int) {
//        var menuItem = bnv.getMenu().getItem(position)
//        menuItem.setChecked(true)
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

//    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        val itemId = item.itemId
//        when (itemId) {
//            R.id.tab_one -> vp.setCurrentItem(0)
//            R.id.tab_two -> vp.setCurrentItem(1)
//            R.id.tab_three -> vp.setCurrentItem(2)
//            R.id.tab_four -> vp.setCurrentItem(3)
//        }
//        return false
//    }

//    var bottomNavigationView: BottomNavigationView? = null
//    private val menuItem: MenuItem? = null

    override fun onResume() {
        super.onResume()
        Net.instance.getApiService().getUserCenter()
                .compose(RxUtil.applySchedulers())
                .subscribe(object : NESubscriber<BR<UserCenter>>(this) {
                    override fun onSuccess(t: BR<UserCenter>?) {
                        onGetUserInfoSuccess(t?.data)
                    }

                    override fun onError(e: Throwable?) {
                        super.onError(e)
                        onGetUserInfoSuccess(null)
                    }

                })
    }

    private fun onGetUserInfoSuccess(data: UserCenter?) {
        if (null != data?.user) {
            tv_name.visibility = View.VISIBLE
            tv_account.visibility = View.VISIBLE
            iv_cover?.visibility = View.VISIBLE
            bt_login?.visibility = View.INVISIBLE

            var user = data?.user
            Glide.with(this@MeituMainActivity).load(user?.portarit)
                    .apply(RequestOptions().circleCrop()).into(nav_head?.iv_cover!!)
            Glide.with(this@MeituMainActivity).load(user?.portarit)
                    .apply(RequestOptions().circleCrop()).into(iv_portrait)
            tv_name.text = user?.name
            tv_account.text = user?.account
        } else {
            tv_name.visibility = View.INVISIBLE
            tv_account.visibility = View.INVISIBLE
            iv_cover?.visibility = View.INVISIBLE
            bt_login?.visibility = View.VISIBLE
            bt_login?.setOnClickListener(this)
        }

    }

    var nav_head: View? = null

    var adapter: V4FragmentsPagerAdapter? = null
    var scopes: ScopeFragments? = null
    var phone: PhoneFragments? = null
    var trends: TrendFragments? = null
    var home: HomeFragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meitu_main)
        fl_search?.setOnClickListener(this)
        iv_portrait?.setOnClickListener(this)
        initDraw()


//        UiUtil.disableShiftMode(bnv);
//        bnv.setOnNavigationItemSelectedListener(this)
//        bnv.selectedItemId = R.id.tab_one

        vp.addOnPageChangeListener(this)

        adapter = V4FragmentsPagerAdapter(supportFragmentManager!!)

        home = HomeFragment()
        adapter?.addFragment(home!!, resources.getString(R.string.home))
        trends = TrendFragments()
        adapter?.addFragment(trends!!, resources.getString(R.string.cycler))
        scopes = ScopeFragments()
        adapter?.addFragment(scopes!!, resources.getString(R.string.scope))
        phone = PhoneFragments()
        adapter?.addFragment(phone!!, resources.getString(R.string.phone))
        adapter?.setUpWithRadioGroup(vp, rg, 2)
        vp.offscreenPageLimit = 4
        vp.adapter = adapter

        Net.instance.getApiService().followedTabs()
                .compose(RxUtil.applySchedulers())
                .subscribe(object : NESubscriber<BR<ArrayList<Tab>>>(this) {
                    override fun onSuccess(t: BR<ArrayList<Tab>>?) {

                    }
                })
    }

    private fun initDraw() {
        nav_head = layoutInflater.inflate(R.layout.nav_header_meitu_main, null)
        var lp = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
        lp.topMargin = UiUtil.getStatusBarHeight(this)
        nav_head?.layoutParams = lp
        nav_view.addView(nav_head)

        nav_head?.tv_focus?.setOnClickListener(this)
        nav_head?.fl_visit?.setOnClickListener(this)
        nav_head?.fl_likes?.setOnClickListener(this)
        nav_head?.fl_feedback?.setOnClickListener(this)
        nav_head?.iv_setting?.setOnClickListener(this)
        nav_head?.iv_cover?.setOnClickListener(this)
        nav_head?.fl_test?.setOnClickListener(this)

    }


}
