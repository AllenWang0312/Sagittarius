package edu.tjrac.swant.filesystem

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import edu.tjrac.swant.baselib.util.FileUtils.extensionName
import edu.tjrac.swant.filesystem.bean.MediaInfo
import java.io.File
import java.util.*

/**
 * Created by wpc on 2018/1/25 0025.
 */

object MediaUtil {
    enum class MediaType {
        all, file, doc, image, music, video
    }

    fun getMediaMaps(context: Context, type: MediaType): HashMap<String, ArrayList<String>>? {
        val map = HashMap<String, ArrayList<String>>()
        var media_type: Uri? = null
        when (type) {
            MediaUtil.MediaType.doc -> media_type = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            MediaUtil.MediaType.image -> media_type = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            MediaUtil.MediaType.video -> media_type = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            MediaUtil.MediaType.music -> media_type = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }
        val contentResolver = context.contentResolver
        val cursor = contentResolver.query(media_type!!, null, null, null, null)
        if (cursor == null || cursor.count <= 0) {
            return null // 没有图片
        }
        while (cursor.moveToNext()) {
            val index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            val path = cursor.getString(index) // 文件地址
            val file = File(path)
            if (!map.keys.contains(file.parent)) {
                map?.set(file.parent, ArrayList())
            }
            map[file.parent]!!.add(file.absolutePath)
        }
        return map
    }


    fun getMediaDirs(context: Context, type: MediaType): List<String> {
        val paths = ArrayList<String>()
        val media = getMediaUris(context, type)
        for (item in media!!) {
            val file = File(item)
            if (!paths.contains(file.parent)) {
                paths.add(file.parent)
            }
        }
        return paths
    }

    fun getMediaUris(context: Context, type: MediaType): List<String>? {
        val uris = ArrayList<String>()
        val media_type = ArrayList<Uri>()
        when (type) {
            MediaUtil.MediaType.image -> media_type.add(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            MediaUtil.MediaType.video -> media_type.add(MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
            MediaUtil.MediaType.music -> media_type.add(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)
            MediaUtil.MediaType.all -> {
                media_type.add(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                media_type.add(MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
                media_type.add(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)
            }
        }

        for (uri in media_type) {
            val contentResolver = context.contentResolver
            val cursor = contentResolver.query(uri, null, null, null, null)
            if (cursor == null || cursor.count <= 0) {
                return null // 没有图片
            }
            while (cursor.moveToNext()) {
                val index = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                val path = cursor.getString(index) // 文件地址
                val file = File(path)
                if (file.exists()) {
                    uris.add(file.absolutePath)
                }
            }
        }
        return uris
    }

    fun getMediaInfos(context: Context, type: MediaType): List<MediaInfo>? {
        val uris = ArrayList<MediaInfo>()
        var media_type: Uri? = null
        when (type) {
            MediaUtil.MediaType.image -> media_type = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            MediaUtil.MediaType.video -> media_type = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            MediaUtil.MediaType.music -> media_type = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }
        val contentResolver = context.contentResolver
        val cursor = contentResolver.query(
                media_type!!,
                arrayOf(
                        MediaStore.Images.Media.DATA,
                        MediaStore.Images.Media.SIZE,
                        MediaStore.Images.Media.DISPLAY_NAME,
                        MediaStore.Images.Media.WIDTH,
                        MediaStore.Images.Media.HEIGHT
                ), null, null, null
        )
        if (cursor == null || cursor.count <= 0) {
            return null // 没有图片
        }
        while (cursor.moveToNext()) {

            uris.add(
                    MediaInfo(
                            type,
                            cursor.getString(2),
                            cursor.getString(0),
                            cursor.getLong(1),
                            cursor.getInt(3),
                            cursor.getInt(4)
                    )


            )

        }
        return uris
    }

    fun TypeEqual(media_type: MediaType, item: File): Boolean {
        val end = extensionName(item.name)
        when (media_type.ordinal) {
            1 -> return true
            2 -> return Config.support_image_type.contains(end.toLowerCase())
            3 -> return Config.support_music_type.contains(end.toLowerCase())
            4 -> return Config.support_video_type.contains(end.toLowerCase())
        }
        return false
    }
}
