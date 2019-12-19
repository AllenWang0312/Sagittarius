package edu.tjrac.swant.todo.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import edu.tjrac.swant.baselib.common.adapter.V4FragmentsPagerAdapter
import edu.tjrac.swant.baselib.common.base.BaseActivity
import edu.tjrac.swant.baselib.common.base.BaseBarFragment
import edu.tjrac.swant.baselib.common.base.BaseWebViewFragment
import edu.tjrac.swant.todo.adapter.GroupWebViewPageTransation
import edu.tjrac.swant.todo.bean.WebInfo
import edu.tjrac.swant.wjzx.R
import kotlinx.android.synthetic.main.activity_group_web_viewer.*
import java.util.*

class GroupWebViewerActivity : BaseActivity() {


    internal var adapter: V4FragmentsPagerAdapter?=null
    internal var infos: List<WebInfo>?=null
    internal var current: BaseBarFragment?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_group_web_viewer);
        infos = intent.getParcelableArrayListExtra<WebInfo>("infos")

        adapter = V4FragmentsPagerAdapter(supportFragmentManager)
        for (item in infos!!) {
            adapter!!.addFragment(BaseWebViewFragment(item.url, R.layout.activity_service_text), item.title)
        }
        viewpager.setOffscreenPageLimit(infos!!.size)

        viewpager.setAdapter(adapter)
        viewpager.setPageMargin(40)
        viewpager.setPageTransformer(true, GroupWebViewPageTransation())
        viewpager.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                current = adapter!!.getItem(position) as BaseBarFragment
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

    }

    override fun onBackPressed() {
        if (current!!.backable()) {
            current!!.onBack()
            return
        }
        super.onBackPressed()
    }

    companion object {


        fun start(context: Context, infos: ArrayList<WebInfo>) {
            context.startActivity(Intent(context, GroupWebViewerActivity::class.java)
                    .putExtra("infos", infos)
                    .addFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT or Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        }
    }
}
