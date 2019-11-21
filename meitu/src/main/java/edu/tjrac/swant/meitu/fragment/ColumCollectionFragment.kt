package edu.tjrac.swant.meitu.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import edu.tjrac.swant.baselib.common.base.BaseFragment
import edu.tjrac.swant.meitu.R
import edu.tjrac.swant.meitu.adapter.ColumLiatAdapter
import edu.tjrac.swant.meitu.bean.Like
import edu.tjrac.swant.meitu.net.BR
import edu.tjrac.swant.meitu.net.NESubscriber
import edu.tjrac.swant.meitu.net.Net
import edu.tjrac.swant.meitu.view.ColumDetailActivity
import edu.tjrac.swant.meitu.view.ColumWebViewActivity
import kotlinx.android.synthetic.main.fragment_follow_models.view.*

/**
 * Created by wpc on 2019-11-13.
 */
class ColumCollectionFragment : BaseFragment() {

    lateinit var v: View

    var adapter: ColumLiatAdapter? = null
    var data: ArrayList<Like>? = ArrayList()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_follow_models, container, false)
        adapter = ColumLiatAdapter(R.layout.item2_meitu_colum, data)
        adapter?.setOnItemClickListener{ad, view, position ->
            var item =data?.get(position)
            if (item?.colum?.get!!){
                startActivity(Intent(activity!!, ColumDetailActivity::class.java)
                        .putExtra("model_id",item.modelid)
                        .putExtra("id",item.id))

//                GalleryFragment
            }else{
                startActivity(Intent(activity, ColumWebViewActivity::class.java)
                        .putExtra("colum_id",item.id)
                        .putExtra("url", "https://m.meituri.com/a/" + item?.colum?.id + "/")
                        .putExtra("tital", item?.colum?.title!!))

            }
        }
        v.recycler.layoutManager = LinearLayoutManager(activity!!)
        v.recycler.adapter = adapter
        Net.instance.getApiService().getFavouriteColumsList()
                .compose(edu.tjrac.swant.meitu.net.RxUtil.applySchedulers())
                .subscribe(object : NESubscriber<BR<ArrayList<Like>>>(this) {
                    override fun onSuccess(t: BR<ArrayList<Like>>?) {
                        if (t?.data?.size!! > 0) {
                            data?.addAll(t?.data!!)
                            adapter?.notifyDataSetChanged()
                        }
                    }
                })
        return v
    }
}