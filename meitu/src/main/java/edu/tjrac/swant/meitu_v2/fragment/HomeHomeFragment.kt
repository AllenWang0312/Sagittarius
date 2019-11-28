package edu.tjrac.swant.meitu_v2.fragment

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import edu.tjrac.swant.baselib.common.base.BaseFragment
import edu.tjrac.swant.meitu.R
import edu.tjrac.swant.meitu.adapter.ColumLiatAdapter
import edu.tjrac.swant.meitu.adapter.CompanyListAdapter
import edu.tjrac.swant.meitu.adapter.ModelLiatAdapter
import edu.tjrac.swant.meitu.bean.Colum
import edu.tjrac.swant.meitu.bean.HomeBean
import edu.tjrac.swant.meitu.net.BR
import edu.tjrac.swant.meitu.net.NESubscriber
import edu.tjrac.swant.meitu.net.Net
import edu.tjrac.swant.meitu.net.RxUtil
import kotlinx.android.synthetic.main.meitu_home_head.view.*
import kotlinx.android.synthetic.main.swiper_recycler_view.view.*

/**
 * Created by wpc on 2019-11-28.
 */

class HomeHomeFragment:BaseFragment() {

    var head: View? = null
    var v: View? = null
    var adapter: ColumLiatAdapter? = null
    var data:ArrayList<Colum>?= ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = layoutInflater.inflate(R.layout.swiper_recycler_view, container, false)

        v?.swiper?.setOnRefreshListener {
            pageNo=1
            initData(true)
        }
        adapter= ColumLiatAdapter(R.layout.item_meitu_colum,data)
        adapter?.setOnLoadMoreListener{
            pageNo++
            initData(false)
        }
        v?.recycler?.layoutManager= GridLayoutManager(activity!!,2)
        v?.recycler?.adapter =adapter


        initData(true)

        return v
    }

    var pageSize=20
    var pageNo=1

    private fun initData(refreshHead:Boolean) {
        if(pageNo==1){
            data?.clear()
            adapter?.notifyDataSetChanged()
        }
        if(refreshHead){
            Net.instance.getApiService().getHomeData("cn")
                    .compose(RxUtil.applySchedulers())
                    .subscribe(object : NESubscriber<BR<HomeBean>>(this) {
                        override fun onSuccess(t: BR<HomeBean>?) {
                            head = layoutInflater.inflate(R.layout.meitu_home_head, null)

                            if(t?.data?.companys?.size!!>0){
                                head?.fl_com_head?.visibility= View.VISIBLE
                                head?.recycler_companys?.visibility= View.VISIBLE
                                head?.recycler_companys?.layoutManager= LinearLayoutManager(activity!!, LinearLayoutManager.HORIZONTAL,true)
                                head?.recycler_companys?.adapter= CompanyListAdapter(R.layout.item_meitu_company,t?.data?.companys)
                            }else{
                                head?.fl_com_head?.visibility= View.GONE
                                head?.recycler_companys?.visibility= View.GONE
                            }
                            if(t?.data?.models?.size!!>0){
                                head?.fl_mol_head?.visibility= View.VISIBLE
                                head?.recycler_models?.visibility= View.VISIBLE
                                head?.recycler_models?.layoutManager= LinearLayoutManager(activity!!, LinearLayoutManager.HORIZONTAL,true)
                                head?.recycler_models?.adapter= ModelLiatAdapter(R.layout.item_meitu_model_hor,t?.data?.models)
                            }else{
                                head?.fl_mol_head?.visibility= View.GONE
                                head?.recycler_models?.visibility= View.GONE
                            }
                            adapter?.setHeaderView(head)

                        }
                    })
        }
        Net.instance.getApiService().getColumList(pageSize,pageNo,null)
                .compose(RxUtil.applySchedulers())
                .subscribe(object : NESubscriber<BR<ArrayList<Colum>>>(this) {
                    override fun onSuccess(t: BR<ArrayList<Colum>>?) {
                        if (null != t?.data) {
                            onGetDataSuccess(t?.data!!)
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

    private fun onGetDataSuccess(data: java.util.ArrayList<Colum>) {
        if(data?.size!!>0){
            this.data?.addAll(data)
            adapter?.loadMoreComplete()
        }else{
            adapter?.loadMoreEnd()
        }
    }
}