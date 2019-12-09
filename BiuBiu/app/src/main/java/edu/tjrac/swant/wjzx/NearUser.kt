package edu.tjrac.swant.wjzx

import com.amap.api.maps.model.LatLng
import edu.tjrac.swant.map.MapFragment.Companion.mPosition

class NearUser {
    companion object {
        val SAFE = 0
        val VIGILANT = 1
        val WARNING = -1
    }

    var location: LatLng? = null
    var userPortrait: String? = ""
    var userName: String? = ""
    var userId: Int? = 0
    var safeState: Int? = 0//0 安全 1 开启 -1 警报

    constructor(userPortrait: String?, userName: String?) {
        this.userPortrait = userPortrait
        this.userName = userName

        userId = (Math.random() * 100).toInt()
        safeState = (-1 + Math.random() * 2).toInt()
        location = LatLng(mPosition?.latitude!! + 0.001*Math.random(), mPosition?.longitude!! + 0.001*Math.random())
    }
}