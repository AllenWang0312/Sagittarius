package edu.tjrac.swant

open class Config {

    companion object {

        val packageName = "edu.tjrac.swant.wjzx"
        val CACHE_PATH = "/storage/emulated/0/Android/data/$packageName"
        val STATIC_CACHE_PATH = "/storage/emulated/0/$packageName"
        var MD5_SALT = "sgsotj"
//        open val AMAP_STYLE_PATH="$STATIC_CACHE_PATH/style.data"
//        open val AMAP_STYLE_EXTRA_PATH="$STATIC_CACHE_PATH/style_extra.data"
    }

    open class SP {
        companion object {
            open val CACHE = "CACHE"

            val ISNIGHT_MODE = "is_night_mode"
            val LANGUAGE_FOLLOW_SYSTEM = "follow_system"
            val LANGUAGE_SETTING = "language_setting"
            val GallerySetting = "gallery_setting"
            val REPUSHTASK = "repush_tasks"

            //media
            //rtsp
            val CACHE_URL = "cache_url"
            var CACHE_TITLE = "cache_title"
        }
    }

}