package edu.tjrac.swant.meitu.view

import android.os.Bundle
import android.view.View
import edu.tjrac.swant.baselib.common.base.BaseWebViewActivity
import edu.tjrac.swant.meitu.App
import edu.tjrac.swant.meitu.R
import edu.tjrac.swant.meitu.net.BR
import edu.tjrac.swant.meitu.net.NESubscriber
import edu.tjrac.swant.meitu.net.Net
import edu.tjrac.swant.meitu.net.RxUtil

/**
 * Created by wpc on 2019-11-20.
 */
class AlbumWebViewActivity : BaseWebViewActivity() {
    var model_id = 0
    var album_id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.hasExtra("model_id")) model_id = intent.getIntExtra("model_id", 0)
        if (intent.hasExtra("album_id")) album_id = intent.getIntExtra("album_id", 0)

        Net.instance.getApiService().recordVisitHistroy(album_id)
                .compose(RxUtil.applySchedulers())
                .subscribe(object : NESubscriber<BR<Any>>(this) {
                    override fun onSuccess(t: BR<Any>?) {

                    }
                })
    }

    override fun setToolbar(view: View) {
        super.setToolbar(view)
        setRightIcon(R.drawable.favorite_border, View.OnClickListener {
            var request = HashMap<String, String>()
            request.put("user_id", "" + App?.loged?.id!!)
            request.put("model_id", "" + model_id)
            request.put("album_id", "" + album_id)
            Net.instance.getApiService().like(request)
                    .compose(RxUtil.applySchedulers())
                    .subscribe(object : NESubscriber<BR<Int>>(this) {
                        override fun onSuccess(t: BR<Int>?) {

                        }
                    })
        })
    }
}