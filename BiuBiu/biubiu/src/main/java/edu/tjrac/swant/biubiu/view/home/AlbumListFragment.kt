package edu.tjrac.swant.biubiu.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import edu.tjrac.swant.baselib.common.base.BaseFragment
import edu.tjrac.swant.baselib.common.recycler.GridSpacingItemDecoration
import edu.tjrac.swant.baselib.util.SUtil
import edu.tjrac.swant.baselib.util.UiUtil
import edu.tjrac.swant.biubiu.R
import edu.tjrac.swant.biubiu.adapter.AlbumListAdapter
import edu.tjrac.swant.biubiu.bean.Album
import edu.tjrac.swant.biubiu.listener.AlbumListClickListener
import edu.tjrac.swant.biubiu.net.BR
import edu.tjrac.swant.biubiu.net.NESubscriber
import edu.tjrac.swant.biubiu.net.Net
import kotlinx.android.synthetic.main.swiper_recycler_view.view.*

/**
 * Created by wpc on 2019-09-05.
 */
class AlbumListFragment() : BaseFragment() {
    var platform: String? = null
    var column: String? = null

    constructor(platform: String, column: String) : this() {
        this.platform = platform
        this.column = column
    }

    var data: ArrayList<Album>? = ArrayList()
    var adapter: AlbumListAdapter? = null

    var pageSize = 20
    var pageNo = 1

    var v: View? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = layoutInflater.inflate(R.layout.swiper_recycler_view, container, false)

        adapter = AlbumListAdapter(R.layout.item_meitu_colum, data!!)

        adapter?.setOnItemClickListener(AlbumListClickListener(activity!!))
//        adapter?.setOnItemChildClickListener { ad, view, position ->
//            var item = data?.get(position)
//            var request = HashMap<String, String>()
//            request.put("user_id", "" + BiuBiuApp.loged?.id!!)
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
//        v?.recycler?.addItemDecoration(BaseDecoration(activity!!, LinearLayoutManager.VERTICAL))
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
        var map = HashMap<String, String>()
        map.set("pageSize", "" + pageSize)
        map.set("pageNo", "" + pageNo)

        if (!SUtil.isEmpty(platform)) map.set("platform", platform!!)
        if (!SUtil.isEmpty(column)) map.set("column", column!!)

        Net.instance.getApiService().getColumList(map)
                .compose(edu.tjrac.swant.biubiu.net.RxUtil.applySchedulers())
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