package edu.tjrac.swant.biubiu.bean

import edu.tjrac.swant.baselib.util.TimeUtils

/**
 * Created by wpc on 2019-11-21.
 */
class Banner {

    var id: Int? = 0

    var title:String?=""

    var enable: Boolean? = false

    var start:String?=""
    var end:String?=""

    var src: String? = ""
    var link: String? = ""


    fun getStartTime(): Long {
        return TimeUtils.getCNDate(start).time
    }

    fun getEndTime(): Long {
        return TimeUtils.getCNDate(end).time
    }

}