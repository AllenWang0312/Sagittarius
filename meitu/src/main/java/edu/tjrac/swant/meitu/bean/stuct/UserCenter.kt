package edu.tjrac.swant.meitu.bean.stuct

import edu.tjrac.swant.meitu.bean.RoleRecord
import edu.tjrac.swant.meitu.bean.User

/**
 * Created by wpc on 2019-12-09.
 */
class UserCenter {
    var user: User? = null
    var follow: Int? = 0
    var followed: Int? = 0
    var cycle: Int? = 0

    var roles: ArrayList<RoleRecord>? = null

}