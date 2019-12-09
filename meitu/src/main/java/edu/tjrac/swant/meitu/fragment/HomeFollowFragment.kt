package edu.tjrac.swant.meitu.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.entity.MultiItemEntity
import edu.tjrac.swant.baselib.common.base.BaseFragment
import edu.tjrac.swant.baselib.util.TimeUtils
import edu.tjrac.swant.baselib.util.TimeUtils.YMD
import edu.tjrac.swant.meitu.R
import edu.tjrac.swant.meitu.adapter.ZoneListAdapter
import edu.tjrac.swant.meitu.bean.Tab
import edu.tjrac.swant.meitu.bean.Zone
import edu.tjrac.swant.meitu.bean.ZoneTitle
import edu.tjrac.swant.meitu.net.BR
import edu.tjrac.swant.meitu.net.NESubscriber
import edu.tjrac.swant.meitu.net.Net
import edu.tjrac.swant.meitu.net.RxUtil
import edu.tjrac.swant.meitu.view.AlbumDetailActivity
import kotlinx.android.synthetic.main.fragment_home_follow.*
import kotlinx.android.synthetic.main.fragment_home_follow.view.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by wpc on 2019-11-28.
 */

class HomeFollowFragment : BaseFragment() {

    var v: View? = null

    var follows = ArrayList<Tab>()
    var adapter: ZoneListAdapter? = null

    var data = ArrayList<MultiItemEntity>()

    var end = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_home_follow, container, false)
        initTime()
        swiper?.setOnRefreshListener {
            data?.clear()
            adapter?.notifyDataSetChanged()
            initTime()
            pageNo = 1
            initData()
        }
        adapter = ZoneListAdapter(data)
        adapter?.setOnItemChildClickListener{ada, view, position ->
            when(view?.id){
                R.id.tv_like->{

                }
                R.id.tv_comment->{

                }
                R.id.tv_share->{

                }
            }
        }
        adapter?.setOnItemClickListener { ada, view, position ->
            var item = adapter?.getItem(position)
            if (item is Zone) {
                if (item.type == 4) {
                    startActivity(Intent(activity!!, AlbumDetailActivity::class.java)
                            .putExtra("album_id", item.album_id)
                            .putExtra("model_id", item.model_id)
                    )
                }
            }
        }
        adapter?.setOnLoadMoreListener {
            loadMore()
        }
        var head = layoutInflater.inflate(R.layout.head_home_follow, null)
        adapter?.setHeaderView(head)

        v?.recycler?.layoutManager = LinearLayoutManager(activity!!)
        v?.recycler?.adapter = adapter

        initData()
        return v
    }

    private fun loadMore() {
        if (end) {
            nextMonth()
            data?.add(ZoneTitle(year + "-" + month))
            end = false
        } else {
            pageNo++
        }
        initData()
    }

    private fun initTime() {
        var timestr = TimeUtils.getTimeWithFormat(time, YMD)
        year = timestr.substring(0, 4)
        month = timestr.substring(5, 7)
        data?.add(ZoneTitle("$year-$month"))
    }

    private fun nextMonth() {
        var yi = year.toInt()
        var mi = month.toInt()
        if (mi > 0) {
            mi--
        } else {
            mi = 12
            yi--
        }
        year = yi.toString()
        month = mi.toString()
        if (month.length == 1) {
            month = "0$month"
        }
        pageNo = 1
    }

    var pageNo = 1
    var pageSize = 20

    var time: Date? = Date()
    var year: String = "2019"
    var month: String = "12"

    fun initData() {
        Net.instance.getApiService().getZoneHistroy(
                year, month, pageNo, pageSize)
                .compose(RxUtil.applySchedulers())
                .subscribe(object : NESubscriber<BR<ArrayList<Zone>>>(this) {
                    override fun onSuccess(t: BR<ArrayList<Zone>>?) {
                        if (null != t?.data && t?.data?.size!! > 0) {
                            data?.addAll(t?.data!!)
                            end = false
                        } else {
                            end = true
                        }
//                        loadMore()
                        adapter?.loadMoreComplete()
                    }

                    override fun onError(e: Throwable?) {
                        super.onError(e)
                    }

                    override fun onCompleted() {
                        super.onCompleted()
                        if (swiper.isRefreshing) {
                            swiper.isRefreshing = false
                        }
                    }
                })

    }
}