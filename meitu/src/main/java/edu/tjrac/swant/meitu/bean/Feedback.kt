package edu.tjrac.swant.meitu.bean

/**
 * Created by wpc on 2019-11-18.
 */
class Feedback {
    var id: Int? = 0
    var user_id: Int? = 0
    var portarit: String? = ""
    var username: String? = ""
    var content: String? = ""
    var images: ArrayList<String>? = null

    var state: Int? = 1
    var likes: Int? = 0
}