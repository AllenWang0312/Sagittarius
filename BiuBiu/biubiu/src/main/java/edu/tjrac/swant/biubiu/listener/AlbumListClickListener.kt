package edu.tjrac.swant.biubiu.listener

import android.app.Activity
import android.content.Intent
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import edu.tjrac.swant.biubiu.bean.Album
import edu.tjrac.swant.biubiu.view.AlbumDetailActivity
import edu.tjrac.swant.biubiu.view.AlbumWebViewActivity

/**
 * Created by wpc on 2019-12-10.
 */
open class AlbumListClickListener(
        var context: Activity
) : BaseQuickAdapter.OnItemClickListener {
    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        var item = adapter?.getItem(position)
        if (item is Album) {
            if (item.get!!) {
                context.startActivity(Intent(context, AlbumDetailActivity::class.java)
                        .putExtra("model_id", item.model_id)
                        .putExtra("id", item.id))

//                GalleryFragment
            } else {
                context.startActivity(Intent(context, AlbumWebViewActivity::class.java)
                        .putExtra("album_id", item.id)

                        .putExtra("url",
                                if (item.cloumn.equals("travel")) {
                                    "http://travel.fengniao.com/slide/535/" + item?.id + "_1.html"
                                } else {
                                    "https://m.meituri.com/a/" + item?.id + "/"
                                })

                        .putExtra("tital", item?.title!!))

            }
        }
    }


//    fun onItemClick(adapter: BaseQuickAdapter<MultiItemEntity, BaseViewHolder>?, view: View?, position: Int) {
//
//    }
}