package edu.tjrac.swant.meitu.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import edu.tjrac.swant.baselib.common.base.BaseActivity
import edu.tjrac.swant.baselib.util.UiUtil
import edu.tjrac.swant.meitu.R
import edu.tjrac.swant.meitu.adapter.AlbumListAdapter
import edu.tjrac.swant.meitu.bean.Album
import edu.tjrac.swant.meitu.bean.Tags
import edu.tjrac.swant.meitu.net.BR
import edu.tjrac.swant.meitu.net.NESubscriber
import edu.tjrac.swant.meitu.net.Net
import kotlinx.android.synthetic.main.activity_meitu_search.*

class MeituSearchActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.iv_back->{
                finish()
            }
        }
    }

    var mInflater: LayoutInflater? = null
    var pageNo = 1
    var pageSize = 20
    var tag: String? = null

    var adapter: AlbumListAdapter? = null
    var data: ArrayList<Album>? = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mInflater = LayoutInflater.from(this)
        setContentView(R.layout.activity_meitu_search)
        iv_back?.setOnClickListener(this)
        gethottags()
        adapter = AlbumListAdapter(R.layout.item_meitu_colum, data)
        adapter?.setOnItemClickListener{ad, view, position ->
            var item =data?.get(position)
            if (item?.get!!){
                startActivity(Intent(this, AlbumDetailActivity::class.java)
                        .putExtra("model_id",item.model_id)
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
            v.text = item?.name!!
            var layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
            layoutParams.setMargins(0, 0, dp12, dp4)
            v.layoutParams = layoutParams
            v.setOnClickListener {
                tag = item?.name!!
                search()
            }
            tfl.addView(v)
        }
    }

    private fun search() {
        Net.instance.getApiService().getColumList(pageSize, pageNo, tag)
                .compose(edu.tjrac.swant.meitu.net.RxUtil.applySchedulers())
                .subscribe(object : NESubscriber<BR<ArrayList<Album>>>(this) {
                    override fun onSuccess(t: BR<ArrayList<Album>>?) {
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
