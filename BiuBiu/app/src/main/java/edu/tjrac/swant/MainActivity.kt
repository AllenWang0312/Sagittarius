package edu.tjrac.swant

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import edu.tjrac.swant.assistant.AssistantFragment
import edu.tjrac.swant.baselib.common.base.BaseActivity
import edu.tjrac.swant.bluetooth.view.BLEFragment
import edu.tjrac.swant.map.MapFragment
import edu.tjrac.swant.todo.view.WebWorkSpaceActivity
import edu.tjrac.swant.wjzx.R
import edu.tjrac.swant.wjzx.adapter.MainManuAdapter
import edu.tjrac.swant.wjzx.bean.M
import edu.tjrac.swant.wjzx.view.fragment.NoteFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_nav_head.view.*

@Route(path = Router.app_main)
class MainActivity : BaseActivity() {

    var nav_recycler: RecyclerView? = null
    var nav_data: ArrayList<M>? = ArrayList()
    var nav_adapter: MainManuAdapter? = null

    @JvmField
    @Autowired(name = "fragment")
    var selected:Int?= R.string.map

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ARouter.getInstance().inject(this)

        nav_recycler = nav_view.getHeaderView(0) as RecyclerView
        nav_recycler?.layoutManager = LinearLayoutManager(this)
        nav_adapter = MainManuAdapter(nav_data!!)
        var head = layoutInflater.inflate(R.layout.main_nav_head, null)
        head.sw_locate.setOnCheckedChangeListener { buttonView, isChecked ->
            head.post {
                if (isChecked) {
                    head.mv.visibility = View.VISIBLE
                } else {
                    head.mv.visibility = View.INVISIBLE
                }
            }
        }
        nav_adapter?.addHeaderView(head)

        nav_adapter?.setOnItemClickListener { adapter, view, position ->
            var item = nav_adapter?.getItem(position)

            selectFragment(item?.title_res_id!!)
//            nav_recycler?.post {
//                nav_adapter?.selectPosition = position
//            }
            if (item?.checkedable!!) {
                nav_recycler?.post {
                    nav_adapter?.selectPosition = position + 1
                    nav_adapter?.notifyDataSetChanged()
                }
            }


        }
        nav_recycler?.adapter = nav_adapter
        initRecycler()
        nav_adapter?.notifyDataSetChanged()


//        setSupportActionBar(toolbar_bg)
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

//        if (noteFragment == null) {
//            noteFragment = NoteFragment()
//        }
//        changeFragment(nowFragment, noteFragment!!)
        selectFragment(selected!!)
        nav_adapter?.select(selected!!)
    }

    private fun selectFragment(title: Int) {
        when(title){
            R.string.bluetooth->{
                if(bleFragment==null){
                    bleFragment= BLEFragment()
                }
                changeFragment(nowFragment, bleFragment!!)
            }
            R.string.assistant->{
                if(assistantFragment==null){
                    assistantFragment= AssistantFragment()
                }
                changeFragment(nowFragment, assistantFragment!!)
            }
            R.string.map -> {
                if (mapFragment == null) {
                    mapFragment = MapFragment()
                }
                changeFragment(nowFragment, mapFragment!!)
            }
            R.string.note -> {
                if (noteFragment == null) {
                    noteFragment = NoteFragment()
                }
                changeFragment(nowFragment, noteFragment!!)
            }

            R.string.notice -> {

            }
            R.string.file_system -> {
                startActivity(Intent("edu.tjrac.swant.filesystem.FileSystemMainActivity"))
            }
            R.string.todo -> {
                //                    startActivity(Intent("edu.tjrac.swant.todo.view.WebWorkSpaceActivity"))
                WebWorkSpaceActivity.debugStart(this)
            }
//                R.string.sort_files -> {
//
//                }
            R.string.settings -> {
                //                    startActivity(Intent(this, SettingActivity::class.java))
            }
//                R.string.meitu -> {
//                    startActivity(Intent(this, MeituMainActivity::class.java))
//                }
        }

    }

    var nowFragment: Fragment? = null

    var assistantFragment: Fragment? = null
    var mapFragment: Fragment? = null
    var noteFragment: Fragment? = null
//    var galleryFragment: Fragment? = null
    var bleFragment:Fragment?=null


    private fun changeFragment(fromFragment: Fragment?, toFragment: Fragment) {
        if (toFragment != nowFragment) {
            nowFragment = toFragment
        }
        var ft = supportFragmentManager.beginTransaction()
        if (null != fromFragment) {
            ft.hide(fromFragment)
        }
        if (!toFragment.isAdded()) {
            ft.add(R.id.content, toFragment).commit()
        } else {
            ft.show(toFragment).commit()
        }

    }

    var tags: ArrayList<String>? = ArrayList()

    private fun initRecycler() {
        tags?.add("备忘")
        tags?.add("菜谱")
        if (!nav_data?.isEmpty()!!) {
            nav_data?.clear()
        }
        nav_data?.add(M(1, R.string.bluetooth, R.drawable.ic_bluetooth_grey_600_24dp, R.drawable.ic_bluetooth_white_24dp))
        nav_data?.add(M(1, R.string.map, R.drawable.ic_map_grey_600_24dp, R.drawable.ic_map_white_24dp)
        )
        nav_data?.add(M(1, R.string.note, R.drawable.ic_lightbulb_outline_grey_600_24dp, R.drawable.ic_lightbulb_outline_white_24dp, false)
        )
        nav_data?.add(M(1, R.string.notice, R.drawable.ic_notifications_none_grey_600_24dp, R.drawable.ic_notifications_none_white_24dp, false)
        )

        nav_data?.add(M(1, R.string.assistant, R.drawable.ic_lightbulb_outline_grey_600_24dp, R.drawable.ic_lightbulb_outline_white_24dp, false)
        )

        nav_data?.add(M(0))

        for (tag in tags!!) {
//            nav_data?.add(M(1, tag!!, R.drawable.ic_label_outline_grey_600_24dp))
        }
//        nav_data?.add(M(1, R.string.sort_files, R.drawable.ic_attach_file_grey_600_24dp))
        nav_data?.add(M(-1))
        nav_data?.add(M(1, R.string.file_system, R.drawable.ic_launch_grey_600_24dp))
        nav_data?.add(M(1, R.string.todo, R.drawable.ic_launch_grey_600_24dp))
        nav_data?.add(M(-1))

//        nav_data?.add(M(1,R.string.meitu,R.drawable.ic_launch_grey_600_24dp))
        nav_data?.add(M(-1))
//        nav_data?.add(M(1, R.string.settings, R.drawable.))
        nav_data?.add(M(1, R.string.help_feedback, R.drawable.ic_help_outline_grey_600_24dp))
//        nav_data?.add(M(1, R.string.file_system, R.drawable.ic_folder_shared_grey_600_24dp))
    }


    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


}
