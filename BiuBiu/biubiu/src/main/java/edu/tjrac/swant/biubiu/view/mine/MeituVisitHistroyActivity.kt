package edu.tjrac.swant.biubiu.view.mine

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import edu.tjrac.swant.baselib.common.base.BaseBarActivity
import edu.tjrac.swant.biubiu.R
import edu.tjrac.swant.biubiu.adapter.VisitHistoryAdapter
import edu.tjrac.swant.biubiu.bean.VisitHistroy
import edu.tjrac.swant.biubiu.net.BR
import edu.tjrac.swant.biubiu.net.NESubscriber
import edu.tjrac.swant.biubiu.net.Net
import edu.tjrac.swant.biubiu.net.RxUtil
import kotlinx.android.synthetic.main.activity_meitu_base_recycler_activity.*

class MeituVisitHistroyActivity : BaseBarActivity() {

    var adapter: VisitHistoryAdapter? = null
    var data: ArrayList<VisitHistroy>? = ArrayList()
    var pageNo = 1
    var pageSize = 20

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meitu_base_recycler_activity)
        setToolbar(findViewById(R.id.toolbar))
        adapter = VisitHistoryAdapter(data)
        adapter?.setOnLoadMoreListener {
            pageNo++
            initData()
        }
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
        initData()
    }

    private fun initData() {
        if (pageNo == 1) {
            data?.clear()
            adapter?.notifyDataSetChanged()
        }
        Net.instance.getApiService().getVisitHistroy(pageNo, pageSize)
                .compose(RxUtil.applySchedulers())
                .subscribe(object : NESubscriber<BR<ArrayList<VisitHistroy>>>(this) {
                    override fun onSuccess(t: BR<ArrayList<VisitHistroy>>?) {
                        if (t?.data?.size!! > 0) {
                            data?.addAll(t?.data!!)
                            adapter?.loadMoreComplete()
                        } else {
                            adapter?.loadMoreEnd()
                        }
                    }
                })
    }

    override fun setToolbar(tool: View) {
        super.setToolbar(tool)
        setTitle("浏览历史")
        enableBackIcon()

        setRightText("清空记录", View.OnClickListener {
            Net.instance.getApiService().cleanVisitHistroy()
                    .compose(RxUtil.applySchedulers())
                    .subscribe(object : NESubscriber<BR<Any>>(this) {
                        override fun onSuccess(t: BR<Any>?) {
                            pageNo = 1
                            initData()
                        }
                    })
        })
    }
}
