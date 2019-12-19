package edu.tjrac.swant.biubiu.view.account

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import edu.tjrac.swant.baselib.common.base.BaseFragment
import edu.tjrac.swant.biubiu.R
import edu.tjrac.swant.biubiu.adapter.AlbumListAdapter
import edu.tjrac.swant.biubiu.bean.Like
import edu.tjrac.swant.biubiu.listener.AlbumListClickListener
import edu.tjrac.swant.biubiu.net.BR
import edu.tjrac.swant.biubiu.net.NESubscriber
import edu.tjrac.swant.biubiu.net.Net
import kotlinx.android.synthetic.main.fragment_follow_models.view.*

/**
 * Created by wpc on 2019-11-13.
 */
class AlbumCollectionFragment : BaseFragment() {

    lateinit var v: View
    var get: Boolean? = false

    var adapter: AlbumListAdapter? = null
    var data: ArrayList<Like>? = ArrayList()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_follow_models, container, false)
        adapter = AlbumListAdapter(R.layout.item2_meitu_colum, data)
        adapter?.onItemClickListener = AlbumListClickListener(activity!!)
                v . recycler . layoutManager = LinearLayoutManager(activity!!)
                v . recycler . adapter = adapter
                Net.instance.getApiService().getFavouriteColumsList()
                        .compose(edu.tjrac.swant.biubiu.net.RxUtil.applySchedulers())
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