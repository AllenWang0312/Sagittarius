package edu.tjrac.swant.meitu.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import edu.tjrac.swant.baselib.common.base.BaseFragment
import edu.tjrac.swant.baselib.common.recycler.GridSpacingItemDecoration
import edu.tjrac.swant.baselib.util.UiUtil
import edu.tjrac.swant.meitu.R
import edu.tjrac.swant.meitu.adapter.ColumLiatAdapter
import edu.tjrac.swant.meitu.bean.Album
import edu.tjrac.swant.meitu.net.BR
import edu.tjrac.swant.meitu.net.NESubscriber
import edu.tjrac.swant.meitu.net.Net
import edu.tjrac.swant.meitu.view.ColumDetailActivity
import edu.tjrac.swant.meitu.view.ColumWebViewActivity
import kotlinx.android.synthetic.main.swiper_recycler_view.view.*

/**
 * Created by wpc on 2019-09-05.
 */
class ColumListFragment : BaseFragment() {

    var data: ArrayList<Album>? = ArrayList()
    var adapter: ColumLiatAdapter? = null

    var pageSize = 20
    var pageNo = 1

    var v: View? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = layoutInflater.inflate(R.layout.swiper_recycler_view, container, false)

        adapter = ColumLiatAdapter(R.layout.item_meitu_colum, data!!)

        adapter?.setOnItemClickListener { ad, view, position ->
            var item = data?.get(position)
            if (item?.get!!) {
                startActivity(Intent(activity!!, ColumDetailActivity::class.java)
                        .putExtra("model_id", item.modelid)
                        .putExtra("id", item.id))

//                GalleryFragment
            } else {
                startActivity(Intent(activity, ColumWebViewActivity::class.java)
                        .putExtra("colum_id", item.id)
                        .putExtra("url", "https://m.meituri.com/a/" + item?.id + "/")
                        .putExtra("tital", item?.title!!))
            }
        }
//        adapter?.setOnItemChildClickListener { ad, view, position ->
//            var item = data?.get(position)
//            var request = HashMap<String, String>()
//            request.put("user_id", "" + App.loged?.id!!)
//            request.put("model_id", "" + item?.modelid!!)
//            request.put("colum_id", "" + item?.id)
//            Net.instance.getApiService().like(request)
//                    .compose(RxUtil.applySchedulers())
//                    .subscribe(object : NESubscriber<BR<Int>>(this) {
//                        override fun onSuccess(t: BR<Int>?) {
//                            item.hot = t?.data
//                            adapter?.notifyItemChanged(position)
//                        }
//                    })
//        }
//        var layoutManager = LinearLayoutManager(this)
//        var layoutManager = GridLayoutManager(activity!!, 3)
        var layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
//        var layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
// 绑定布局管理器
        v?.recycler?.layoutManager = layoutManager
        var dp8 = UiUtil.dp2px(activity!!, 8).toInt()
        v?.recycler?.addItemDecoration(GridSpacingItemDecoration(2, dp8, dp8));
        v?.swiper?.setOnRefreshListener {
            pageNo = 1
            initData()
        }
        adapter?.setOnLoadMoreListener {
            pageNo++
            initData()
        }

        v?.recycler?.adapter = adapter
        initData()
        return v
    }

    private fun initData() {
        if (pageNo == 1) {
            data?.clear()
            adapter?.notifyDataSetChanged()
        }
        Net.instance.getApiService().getColumList(pageSize, pageNo, null)
                .compose(edu.tjrac.swant.meitu.net.RxUtil.applySchedulers())
                .subscribe(object : NESubscriber<BR<ArrayList<Album>>>(this) {
                    override fun onSuccess(t: BR<ArrayList<Album>>?) {
                        if (null != t?.data) {
                            onGetDataSuccess(t?.data!!)
                        }
                    }

                    override fun onError(e: Throwable?) {
                        super.onError(e)
//                    adapter.loadMoreEnd()
                    }

                    override fun onCompleted() {
                        super.onCompleted()
                        if (v?.swiper?.isRefreshing!!) {
                            v?.swiper?.isRefreshing = false
                        }
                    }
                })
    }

    private fun onGetDataSuccess(data: ArrayList<Album>) {
        if (data?.size > 0) {
            this.data?.addAll(data)
            adapter?.loadMoreComplete()
        } else {
            adapter?.loadMoreEnd()
        }
    }
}