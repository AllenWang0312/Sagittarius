package edu.tjrac.swant.filesystem

import android.content.Context
import android.widget.ImageView

import com.bumptech.glide.Glide
import edu.tjrac.swant.wjzx.R

import java.io.File


/**
 * 类描述:
 * 创建人: Administrator
 * 创建时间: 2018/3/16 0016 上午 10:34
 * 修改人:
 * 修改时间:
 * 修改备注:
 */

object FileSystemHelper {

    fun loadFileIconToImageView(file: File, fileImage: ImageView, mContext: Context) {
        if (file.isDirectory) {
            Glide.with(mContext).load(R.drawable.file_dir).into(fileImage)
        } else {
            val filename = file.name
            val end = filename.substring(filename.lastIndexOf(".") + 1, filename.length)
            when (end.toLowerCase()) {
                "png", "jpg", "jpeg", "gif", "mp4", "avi" -> Glide.with(mContext).load(file).into(fileImage)
                "mp3", "wav" -> Glide.with(mContext).load(R.drawable.file_mic).into(fileImage)
                "doc" -> Glide.with(mContext).load(R.drawable.file_doc).into(fileImage)

                else ->
                    //                    int resid = ResourcesUtils.getDrawableId(mContext, "file_" + end);
                    //                    Drawable drawable = mContext.getDrawable(resid);
                    //                    if (drawable != null) {
                    //                        Glide.with(mContext).load(drawable).into(fileImage);
                    //                    } else {
                    Glide.with(mContext).load(R.drawable.file).into(fileImage)
            }//                    }
        }

    }
}
