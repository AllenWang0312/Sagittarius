package edu.tjrac.swant.biubiu.view

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import edu.tjrac.swant.baselib.common.base.BaseActivity
import edu.tjrac.swant.baselib.util.SUtil
import edu.tjrac.swant.biubiu.BiuBiuApp
import edu.tjrac.swant.biubiu.R
import edu.tjrac.swant.biubiu.adapter.AlbumListAdapter
import edu.tjrac.swant.biubiu.bean.Album
import edu.tjrac.swant.biubiu.bean.ModelDetail
import edu.tjrac.swant.biubiu.listener.AlbumListClickListener
import edu.tjrac.swant.biubiu.net.BR
import edu.tjrac.swant.biubiu.net.NESubscriber
import edu.tjrac.swant.biubiu.net.Net
import edu.tjrac.swant.biubiu.net.RxUtil
import kotlinx.android.synthetic.main.activity_model_info.*

class ModelInfoActivity : BaseActivity() {

    var model_id: Int? = 100
    var get: Boolean? = false

    var adapter: AlbumListAdapter? = null
    var data: ArrayList<Album>? = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_model_info)
        setSupportActionBar(toolbar)

        if (null != intent) {
            model_id = intent.getIntExtra("model_id", 100)
            get = intent.getBooleanExtra("get", false)
        }
        recycler.layoutManager = GridLayoutManager(this, 3)
        adapter = AlbumListAdapter(R.layout.item_meitu_colum, data)
        adapter?.setOnItemClickListener(AlbumListClickListener(this))
        recycler.adapter = adapter
        fab.setOnClickListener {
            var request = HashMap<String, String>()
            request.put("user_id", "" + BiuBiuApp.loged?.id)
            request.put("model_id", "" + model_id)
            Net.instance.getApiService().like(request)
                    .compose(RxUtil.applySchedulers())
                    .subscribe(object : NESubscriber<BR<Int>>(this) {
                        override fun onSuccess(t: BR<Int>?) {
                        }
                    })
        }
        initData()
    }

    private fun initData() {
        Net.instance.getApiService().getModelInfo(model_id!!)
                .compose(RxUtil.applySchedulers())
                .subscribe(object : NESubscriber<BR<ModelDetail>>(this) {
                    override fun onSuccess(t: BR<ModelDetail>?) {
                        var info = t?.data?.info
                        if (!SUtil.isEmpty(info?.cover)) {
                            Glide.with(this@ModelInfoActivity).load(info?.cover).into(iv_cover)
                            var info1 = StringBuffer()
                            if (!SUtil.isEmpty(info?.nicknames)) info1?.append(info?.nicknames + "\n")
                            if (!SUtil.isEmpty(info?.jobs)) info1?.append("工作:" + info?.jobs + "\n")
                            if (!SUtil.isEmpty(info?.interest)) info1?.append("爱好:" + info?.interest + "\n")
                            if (!SUtil.isEmpty(info?.height)) info1?.append("身高:" + info?.height + "\n")
                            if (!SUtil.isEmpty(info?.weight)) info1?.append("体重:" + info?.weight + "\n")
                            if (!SUtil.isEmpty(info?.birthday)) info1?.append("生日:" + info?.birthday + "\n")
                            if (!SUtil.isEmpty(info?.constellation)) info1?.append("星座:" + info?.constellation + "\n")
                            if (!SUtil.isEmpty(info?.dimensions)) info1?.append("cup:" + info?.dimensions + "\n")
                            if (!SUtil.isEmpty(info?.address)) info1?.append("地址:" + info?.address + "\n")
                            if (!SUtil.isEmpty(info?.tags)) info1?.append("标签:" + info?.tags + "\n")
                            tv_info1.text = info1
                            tv_info2.text = info?.more
                        }
                        setTitle(t?.data?.info?.name)

                        data?.addAll(t?.data?.albums!!)
                        adapter?.notifyDataSetChanged()
                    }
                })
    }

}
