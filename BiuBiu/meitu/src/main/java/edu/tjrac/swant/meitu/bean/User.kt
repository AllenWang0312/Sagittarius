package edu.tjrac.swant.meitu.bean

/**
 * Created by wpc on 2019-11-13.
 */

class User {
    var id: Int? = 0
    var account: String? = ""
    var name: String? = ""
    var portarit: String? = ""
    var email: String? = ""
    var pwd: String? = ""
    var tel: String? = ""
    var birthday: String? = ""
    var token: String? = ""
    var type: Int? = 0

    constructor(id: Int?) {
        this.id = id
    }
}