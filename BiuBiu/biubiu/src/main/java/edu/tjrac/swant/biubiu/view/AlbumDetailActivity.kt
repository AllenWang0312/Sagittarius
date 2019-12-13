package edu.tjrac.swant.biubiu.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import edu.tjrac.swant.baselib.common.base.BaseActivity
import edu.tjrac.swant.image.ImagePreviewActivity
import edu.tjrac.swant.biubiu.R
import edu.tjrac.swant.biubiu.adapter.ImageAdapter
import edu.tjrac.swant.biubiu.bean.Album
import edu.tjrac.swant.biubiu.net.BR
import edu.tjrac.swant.biubiu.net.NESubscriber
import edu.tjrac.swant.biubiu.net.Net
import kotlinx.android.synthetic.main.activity_album_detail.*

class AlbumDetailActivity : BaseActivity() {

    var model_id: Int? = 0
    var colum_id: Int? = 0

    var data: ArrayList<String>? = ArrayList()
    var adapter: ImageAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album_detail)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        if (intent?.hasExtra("album_id")!!) colum_id = intent.getIntExtra("album_id", 0)
        if (intent?.hasExtra("model_id")!!) model_id = intent.getIntExtra("model_id", 0)

        adapter = ImageAdapter(data)
        adapter?.setOnItemClickListener { ad, view, position ->
            startActivity(Intent(this, ImagePreviewActivity::class.java)
                    .putExtra("images", data)
                    .putExtra("index", position))

        }
//        recycler.layoutManager = GridLayoutManager(this, 2)
        recycler.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recycler?.adapter = adapter

        Net.instance.getApiService().getColumDetails(model_id, colum_id)
                .compose(edu.tjrac.swant.biubiu.net.RxUtil.applySchedulers())
                .subscribe(object : NESubscriber<BR<Album>>(this) {
                    override fun onSuccess(t: BR<Album>?) {
                        if (null != t?.data?.model) {
                            Glide.with(mContext).load(t?.data?.model?.cover).into(iv_portrait)
                        }
//                             startActivity(Intent(activity!!,ImagePreviewActivity::class.java)
//                                     .putExtra("images",t?.data))
                        if (null != t?.data?.images && t?.data?.images?.size!! > 0) {
                            data?.addAll(t?.data?.images!!)
                            adapter?.notifyDataSetChanged()
                        }
                    }
                })
    }
}
