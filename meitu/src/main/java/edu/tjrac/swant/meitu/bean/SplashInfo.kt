package edu.tjrac.swant.meitu.bean

import edu.tjrac.swant.baselib.util.TimeUtils

/**
 * Created by wpc on 2019-11-19.
 */
class SplashInfo {

    var id: Int? = 0
    var enable: Boolean? = false
    var start: String? = null
    var end: String? = null
    var art_url: String? = ""

    var user: User? = null
    var author: User? = null
    var model: User? = null


    fun getStartTime(): Long {
        return TimeUtils.getCNDate(start).time
    }

    fun getEndTime(): Long {
        return TimeUtils.getCNDate(end).time
    }
}