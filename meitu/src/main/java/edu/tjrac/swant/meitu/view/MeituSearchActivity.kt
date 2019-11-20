package edu.tjrac.swant.meitu.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import edu.tjrac.swant.baselib.common.base.BaseActivity
import edu.tjrac.swant.baselib.util.UiUtil
import edu.tjrac.swant.meitu.R
import edu.tjrac.swant.meitu.adapter.ColumLiatAdapter
import edu.tjrac.swant.meitu.bean.Colum
import edu.tjrac.swant.meitu.bean.Tags
import edu.tjrac.swant.meitu.net.BR
import edu.tjrac.swant.meitu.net.NESubscriber
import edu.tjrac.swant.meitu.net.Net
import kotlinx.android.synthetic.main.activity_meitu_search.*

class MeituSearchActivity : BaseActivity() {

    var mInflater: LayoutInflater? = null
    var pageNo = 1
    var pageSize = 20
    var tag: String? = null

    var adapter: ColumLiatAdapter? = null
    var data: ArrayList<Colum>? = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mInflater = LayoutInflater.from(this)
        setContentView(R.layout.activity_meitu_search)
        gethottags()
        adapter = ColumLiatAdapter(R.layout.item_meitu_colum, data)
        adapter?.setOnItemClickListener{ad, view, position ->
            var item =data?.get(position)
            if (item?.get!!){
                startActivity(Intent(this, ColumDetailActivity::class.java)
                        .putExtra("model_id",item.modelid)
                        .putExtra("id",item.id))

//                GalleryFragment
            }else{
                startActivity(Intent(this, ColumWebViewActivity::class.java)
                        .putExtra("colum_id",item.id)
                        .putExtra("url", "https://m.meituri.com/a/" + item?.id + "/")
                        .putExtra("tital", item?.title!!))
            }
        }
        adapter?.setOnLoadMoreListener {
            pageNo++
            search()
        }
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
    }

    private fun gethottags() {
        Net.instance.getApiService().getHotTags()
                .compose(edu.tjrac.swant.meitu.net.RxUtil.applySchedulers())
                .subscribe(object : NESubscriber<BR<ArrayList<Tags>>>(this) {
                    override fun onSuccess(t: BR<ArrayList<Tags>>?) {
                        if (null != t?.data) {
                            onGetTagsSuccess(t?.data!!)
                        }
                    }

                    override fun onError(e: Throwable?) {
                        super.onError(e)
//                    adapter.loadMoreEnd()
                    }

                    override fun onCompleted() {
                        super.onCompleted()
                    }
                })
    }


    private fun onGetTagsSuccess(data: ArrayList<Tags>) {
        var dp12 = UiUtil.dp2px(this!!, 12).toInt()
        var dp4 = UiUtil.dp2px(this!!, 4).toInt()
        tfl.removeAllViews()

        for (item in data) {
            var v = mInflater?.inflate(R.layout.search_page_flowlayout_tv, null) as TextView
            v.text = item?.shortname!!
            var layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
            layoutParams.setMargins(0, 0, dp12, dp4)
            v.layoutParams = layoutParams
            v.setOnClickListener {
                tag = item?.shortname!!
                search()
            }
            tfl.addView(v)
        }
    }

    private fun search() {
        Net.instance.getApiService().getColumList(pageSize, pageNo, tag)
                .compose(edu.tjrac.swant.meitu.net.RxUtil.applySchedulers())
                .subscribe(object : NESubscriber<BR<ArrayList<Colum>>>(this) {
                    override fun onSuccess(t: BR<ArrayList<Colum>>?) {
                        if (pageNo == 1) {
                            data?.clear()
                        }
                        if (null != t?.data) {
                            if (t?.data!!.size > 0) {
                                data?.addAll(t?.data!!)
                                adapter?.loadMoreComplete()
                            } else {
                                adapter?.loadMoreEnd()
                            }
                        }
                    }
                })
    }
}
