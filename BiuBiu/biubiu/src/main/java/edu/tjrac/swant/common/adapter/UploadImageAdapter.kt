package edu.tjrac.swant.common.adapter

import androidx.recyclerview.widget.RecyclerView
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import edu.tjrac.swant.common.bean.FileInfo
import edu.tjrac.swant.biubiu.R

/**
 * Created by wpc on 2018-09-20.
 */

class UploadImageAdapter(
        internal var maxSize: Int,
        data: List<FileInfo>?,
        itemlayout: Int
) : BaseQuickAdapter<FileInfo, BaseViewHolder>(itemlayout, data) {

    var addImageRes: Int? = R.drawable.ic_add_image
    var itemWidth: Int? = -1

    override fun getItemCount(): Int {
        if (mData.size == maxSize + 1) {
            return maxSize
        } else {
            return mData.size
        }
    }

    override fun convert(helper: BaseViewHolder, item: FileInfo?) {
        val cover = helper.getView<ImageView>(R.id.iv_cover)
        val cancel = helper.getView<ImageView>(R.id.iv_cancel)
        if (itemWidth!! > 0) {
            var root = helper.getView<FrameLayout>(R.id.fl_root)
            root.layoutParams = RecyclerView.LayoutParams(itemWidth!!, itemWidth!!)
        }
        if (helper.position == mData.size - 1) {
            helper.setVisible(R.id.iv_cancel, false)
//                    .setVisible(R.id.tv_info, false)
            Glide.with(mContext).load(addImageRes)
                    .into(cover)
//            cover.setImageResource(R.drawable.add)
        } else {
            helper.setVisible(R.id.iv_cancel, true)
//                    .setVisible(R.id.tv_info, true)

            if (item?.url?.isEmpty()!!) {
//                helper.setText(R.id.tv_info, "等待上传")
                Glide.with(mContext).load(
                        //                        BitmapFactory.decodeFile(
                        item.absPath
//                        )
                ).apply(RequestOptions().centerCrop())
                        .into(cover)
            } else {
                Glide.with(mContext).load(
                        item.url)
                        .apply(RequestOptions().centerCrop())
                        .into(cover)
            }
        }
        cancel.setOnClickListener {
            mData.removeAt(helper.position)
            notifyItemRemoved(helper.position)
            if (null !== infoView) {
                infoView?.text = if (mData.size == maxSize + 1) {
                    "最多只支持4张图片哦"
                } else {
                    "还可上传" + (maxSize + 1 - mData.size) + "张图片，单张图片最大3兆"
                }
            }
        }
    }

    fun getImageSize(): Int {
        if (mData.get(mData.size - 1).type > 0) {
            return mData.size
        } else {
            return mData.size - 1
        }
    }

    var infoView: TextView? = null
}
