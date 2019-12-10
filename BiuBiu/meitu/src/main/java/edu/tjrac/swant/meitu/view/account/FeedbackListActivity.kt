package edu.tjrac.swant.meitu.view.account

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import edu.tjrac.swant.baselib.common.base.BaseBarActivity
import edu.tjrac.swant.meitu.R
import edu.tjrac.swant.meitu.adapter.FeedbackAdapter
import edu.tjrac.swant.meitu.bean.Feedback
import edu.tjrac.swant.meitu.net.BR
import edu.tjrac.swant.meitu.net.NESubscriber
import edu.tjrac.swant.meitu.net.Net
import kotlinx.android.synthetic.main.activity_feedback_list.*

class FeedbackListActivity : BaseBarActivity() {
    var adapter: FeedbackAdapter? = null
    var data: ArrayList<Feedback> = ArrayList()
    var pageNo = 1
    var pageSize = 20

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback_list)
        setToolbar(findViewById(R.id.toolbar))
        bt.setOnClickListener{
            startActivity(Intent(this,FeedbackActivity::class.java))
        }
        adapter = FeedbackAdapter(data)
        adapter?.setOnLoadMoreListener {
            pageNo++
            initDate()
        }
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
        initDate()

    }

    private fun initDate() {
        if (pageNo == 1) {
            data?.clear()
        }
        Net.instance.getApiService().getFeedbackList(pageNo, pageSize)
                .compose(edu.tjrac.swant.meitu.net.RxUtil.applySchedulers())
                .subscribe(object : NESubscriber<BR<ArrayList<Feedback>>>(this) {
                    override fun onSuccess(t: BR<ArrayList<Feedback>>?) {
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
        enableBackIcon()
        setTitle("反馈")
    }
}
