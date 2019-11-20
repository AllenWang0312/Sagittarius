package edu.tjrac.swant.todo.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import edu.tjrac.swant.baselib.common.base.BaseActivity
import edu.tjrac.swant.todo.adapter.WebWorkGroupAdapter
import edu.tjrac.swant.todo.bean.WebInfo
import edu.tjrac.swant.wjzx.R
import kotlinx.android.synthetic.main.activity_web_work_space.*
import java.util.*

class WebWorkSpaceActivity : BaseActivity() {

    internal var groups: MutableList<WebWorkGroupAdapter.WebWorkGroupInfo> = ArrayList()
    internal var adapter: WebWorkGroupAdapter?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_work_space)
        val webs = intent.getParcelableExtra<WebWorkGroupAdapter.WebWorkGroupInfo>("groups")
        if (webs != null) {
            groups.add(webs)
        }
        recycler.setLayoutManager(GridLayoutManager(this@WebWorkSpaceActivity, 3))

        adapter = WebWorkGroupAdapter(groups)
        adapter!!.setOnItemChildClickListener { adapter, view, position ->
            GroupWebViewerActivity.start(mContext, groups[position].infos)
            startActivity(Intent(mContext, PicInPicMarkDownActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT or Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        }
        recycler.setAdapter(adapter)
    }

    companion object {
        fun debugStart(context: Context) {
            val infos = ArrayList<WebInfo>()
            infos.add(WebInfo("https://www.baidu.com", "百度"))
            infos.add(WebInfo("https://www.google.com", "谷歌"))
            infos.add(WebInfo("https://www.baidu.com", "百度"))
            infos.add(WebInfo("https://www.google.com", "谷歌"))
            infos.add(WebInfo("https://www.baidu.com", "百度"))
            infos.add(WebInfo("https://www.google.com", "谷歌"))
            val group = WebWorkGroupAdapter.WebWorkGroupInfo(
                    "搜索器", infos
            )
            context.startActivity(Intent(context, WebWorkSpaceActivity::class.java)
                    .putExtra("groups", group)
            )
        }
    }
}
