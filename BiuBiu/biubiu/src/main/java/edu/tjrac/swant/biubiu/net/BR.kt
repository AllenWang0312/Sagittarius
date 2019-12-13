package edu.tjrac.swant.biubiu.net

/**
 * Created by wpc on 2019-09-05.
 */
open class BR<T> {

     var code: Int? = 0//默认0为正常
     var msg: String? = null//如果请求出错 给出错误原因
     var toast:String? = null//显示弹出toast
     var version: Float? = 1.0f //用于需要缓存的接口


     var data: T? = null
}