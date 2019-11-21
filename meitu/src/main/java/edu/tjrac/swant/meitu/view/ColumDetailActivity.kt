package edu.tjrac.swant.meitu.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import edu.tjrac.swant.baselib.common.base.BaseActivity
import edu.tjrac.swant.image.ImagePreviewActivity
import edu.tjrac.swant.meitu.R
import edu.tjrac.swant.meitu.adapter.ImageAdapter
import edu.tjrac.swant.meitu.bean.Colum
import edu.tjrac.swant.meitu.net.BR
import edu.tjrac.swant.meitu.net.NESubscriber
import edu.tjrac.swant.meitu.net.Net
import kotlinx.android.synthetic.main.activity_model_info.*

class ColumDetailActivity : BaseActivity() {

    var model_id: Int? = 0
    var colum_id: Int? = 0

    var data: ArrayList<String>? = ArrayList()
    var adapter: ImageAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_colum_detail)
        if (intent?.hasExtra("id")!!) colum_id = intent.getIntExtra("id", 0)
        if (intent?.hasExtra("model_id")!!) model_id = intent.getIntExtra("model_id", 0)

        adapter = ImageAdapter(data)
        adapter?.setOnItemClickListener { adapter, view, position ->
            startActivity(Intent(this, ImagePreviewActivity::class.java)
                    .putExtra("images", data)
                    .putExtra("index", position))

        }
        recycler.layoutManager = GridLayoutManager(this, 2)
        recycler?.adapter = adapter

        Net.instance.getApiService().getColumDetails(model_id, colum_id)
                .compose(edu.tjrac.swant.meitu.net.RxUtil.applySchedulers())
                .subscribe(object : NESubscriber<BR<Colum>>(this) {
                    override fun onSuccess(t: BR<Colum>?) {
//                             startActivity(Intent(activity!!,ImagePreviewActivity::class.java)
//                                     .putExtra("images",t?.data))
                        data?.addAll(t?.data?.images!!)
                        adapter?.notifyDataSetChanged()
                    }
                })
    }
}