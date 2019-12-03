package edu.tjrac.swant.meitu

/**
 * Created by wpc on 2019-11-20.
 */
class Config{
    open class URL {
        companion object {
            val HTTP = false
//            val HOST = "122.51.205.29"//release
            val HOST = "10.10.29.249"//work
//          val HOST = "192.168.0.100"//home
            val API_SERVER = if(HTTP){"https://"}else{"http://"}+HOST + ":8080"
            var FILE_SERVER = if(HTTP){"https://"}else{"http://"}+HOST + ":8081"
            var MODEL_DIR = FILE_SERVER + "/t/"
        }
    }

    open class SP{
        companion object{
            val CACHE = "CACHE"

            val FIRST_TIME = "first_time"
            val TOKEN="token"

            val ISNIGHT_MODE = "is_night_mode"
            val LANGUAGE_FOLLOW_SYSTEM = "follow_system"
            val LANGUAGE_SETTING = "language_setting"
        }

    }
}