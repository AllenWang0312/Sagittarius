package edu.tjrac.swant.meitu.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import edu.tjrac.swant.baselib.common.base.BaseFragment
import edu.tjrac.swant.meitu.view.ModelInfoActivity
import edu.tjrac.swant.meitu.adapter.ModelLiatAdapter
import edu.tjrac.swant.meitu.bean.Like
import edu.tjrac.swant.meitu.App
import edu.tjrac.swant.meitu.net.BR
import edu.tjrac.swant.meitu.net.NESubscriber
import edu.tjrac.swant.meitu.net.Net
import edu.tjrac.swant.meitu.R
import kotlinx.android.synthetic.main.fragment_follow_models.view.*

/**
 * Created by wpc on 2019-11-13.
 */
class FollowModelsFragment : BaseFragment() {

    lateinit var v: View

    var adapter: ModelLiatAdapter? = null
    var data: ArrayList<Like>? = ArrayList()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_follow_models, container, false)
        adapter = ModelLiatAdapter(R.layout.item2_meitu_model, data)
        adapter?.setOnItemClickListener { ad, view, position ->
            var item = data?.get(position)
            startActivity(Intent(activity!!, ModelInfoActivity::class.java)
                    .putExtra("model_id", item?.modelid))
        }
        v.recycler.layoutManager = LinearLayoutManager(activity!!)
        v.recycler.adapter = adapter
        Net.instance.getApiService().getFollowModelList(App.loged?.id!!)
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