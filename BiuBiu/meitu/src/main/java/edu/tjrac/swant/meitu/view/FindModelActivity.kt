package edu.tjrac.swant.meitu.view

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import edu.tjrac.swant.baselib.common.base.BaseBarActivity
import edu.tjrac.swant.baselib.common.indesOf
import edu.tjrac.swant.meitu.R
import edu.tjrac.swant.meitu.adapter.ModelLiatAdapter
import edu.tjrac.swant.meitu.bean.Model
import edu.tjrac.swant.meitu.net.BR
import edu.tjrac.swant.meitu.net.NESubscriber
import edu.tjrac.swant.meitu.net.Net
import edu.tjrac.swant.meitu.net.RxUtil
import kotlinx.android.synthetic.main.activity_find_model.*

/**
 * Created by wpc on 2019-12-10.
 */
class FindModelActivity : BaseBarActivity() {

    var contry: String? = "cn"
    var address = arrayListOf("cn", "cn_tw", "cn_hk", "cn_mo")
    var pageNo = 1
    var pageSize = 20
    var adapter: ModelLiatAdapter? = null
    var data: ArrayList<Model>? = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_model)
        setToolbar(findViewById(R.id.toolbar))
//        adapter = ModelLiatAdapter(R.layout.item2_meitu_model, data)
//        recycler?.layoutManager = LinearLayoutManager(mContext)


        adapter = ModelLiatAdapter(R.layout.item_meitu_model, data)
        recycler?.layoutManager = GridLayoutManager(mContext, 3)
        adapter?.setOnLoadMoreListener {
            pageNo++
            initData()
        }
        recycler?.adapter = adapter

        rg.setOnCheckedChangeListener { group, checkedId ->
            var index =group.indesOf(checkedId)
            contry = address[index]
            pageNo = 1
            initData()
        }
        if(intent?.hasExtra("contry")!!){
            contry=intent?.getStringExtra("contry")
        }
        rg.check(rg.getChildAt(address.indexOf(contry)).id)
    }

    override fun setToolbar(tool: View) {
        super.setToolbar(tool)
        enableBackIcon()
        setTitle("找模特")
    }

    private fun initData() {
        if (pageNo == 1) {
            data?.clear()
            adapter?.notifyDataSetChanged()
        }
        Net.instance.getApiService().getModelList(pageNo, pageSize, contry!!)
                .compose(RxUtil.applySchedulers())
                .subscribe(object : NESubscriber<BR<ArrayList<Model>>>(this) {
                    override fun onSuccess(t: BR<ArrayList<Model>>?) {
                        if (null != t?.data && t?.data?.size!! > 0) {
                            data?.addAll(t?.data!!)
                            adapter?.loadMoreComplete()
                        } else {
                            adapter?.loadMoreEnd()
                        }
                    }
                })
    }
}