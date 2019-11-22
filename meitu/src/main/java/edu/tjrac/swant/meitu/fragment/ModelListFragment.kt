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
import edu.tjrac.swant.meitu.adapter.ModelLiatAdapter
import edu.tjrac.swant.meitu.bean.Model
import edu.tjrac.swant.meitu.net.BR
import edu.tjrac.swant.meitu.net.NESubscriber
import edu.tjrac.swant.meitu.net.Net
import edu.tjrac.swant.meitu.view.MeituSearchActivity
import edu.tjrac.swant.meitu.view.ModelInfoActivity
import kotlinx.android.synthetic.main.swipe_tool_refresh_layout.view.*

/**
 * Created by wpc on 2019-09-05.
 */
class ModelListFragment : BaseFragment(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id!!) {
            R.id.iv_search -> {
                startActivity(Intent(activity, MeituSearchActivity::class.java))
            }
        }
    }

    var data: ArrayList<Model>? = ArrayList()
    var adapter: ModelLiatAdapter? = null

    var pageSize = 20
    var pageNo = 1

    var v: View? = null

//    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
//        inflater?.inflate(R.menu.menu_meitu,menu)
//        super.onCreateOptionsMenu(menu, inflater)
//    }
//    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
//        when (item?.itemId) {
//            R.id.menu_meitu_search -> {
//                startActivity(Intent(activity, MeituSearchActivity::class.java))
//            }
//        }
//        return true
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = layoutInflater.inflate(R.layout.swipe_tool_refresh_layout, container, false)
        v?.iv_search?.setOnClickListener(this)

        adapter = ModelLiatAdapter(R.layout.item_meitu_model, data!!)

//        var layoutManager = LinearLayoutManager(this)
        var layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
//        var layoutManager = GridLayoutManager(activity!!, 3)
// 绑定布局管理器
        v?.recycler?.layoutManager = layoutManager!!
        var dp4 = UiUtil.dp2px(activity!!, 4).toInt()
        v?.recycler?.addItemDecoration(GridSpacingItemDecoration(3, dp4, dp4));
        v?.swiper?.setOnRefreshListener {
            pageNo = 1
            initData()
        }
        adapter?.setOnLoadMoreListener {
            pageNo++
            initData()
        }
        adapter?.setOnItemClickListener { ad, view, position ->
            var item = data?.get(position)
            startActivity(Intent(activity!!, ModelInfoActivity::class.java)
                    .putExtra("model_id", item?.id)
            )
//            BaseWebViewActivity.start(activity!!, item?.name!!, "https://m.meituri.com/t/" + item.id + "/")
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
        Net.instance.getApiService().getModelList(pageSize, pageNo)
                .compose(edu.tjrac.swant.meitu.net.RxUtil.applySchedulers())
                .subscribe(object : NESubscriber<BR<ArrayList<Model>>>(this) {
                    override fun onSuccess(t: BR<ArrayList<Model>>?) {
                        if (null != t?.data) {
                            onGetDataSuccess(t?.data!!)
                        }
                    }

                    override fun onError(e: Throwable?) {
                        super.onError(e)
//                    adapter.loadMoreEnd()
                        if (v?.swiper?.isRefreshing!!) {
                            v?.swiper?.isRefreshing = false
                        }
                    }

                    override fun onCompleted() {
                        super.onCompleted()
                        if (v?.swiper?.isRefreshing!!) {
                            v?.swiper?.isRefreshing = false
                        }
                    }
                })
    }

    private fun onGetDataSuccess(data: ArrayList<Model>) {
        if (data?.size > 0) {
            this.data?.addAll(data)
            adapter?.loadMoreComplete()
        } else {
            adapter?.loadMoreEnd()
        }
    }
}