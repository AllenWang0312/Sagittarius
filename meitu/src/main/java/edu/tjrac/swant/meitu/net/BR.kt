package edu.tjrac.swant.meitu.net

/**
 * Created by wpc on 2019-09-05.
 */
class BR<T> {

//    internal var code: Int? = 0//默认0为正常

    internal var msg: String? = null//如果请求出错 给出错误原因
    internal var toast:String? = null//显示弹出toast
    internal var version: Float? = 1.0f //用于需要缓存的接口


    internal var data: T? = null
}