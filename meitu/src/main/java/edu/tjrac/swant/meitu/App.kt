package edu.tjrac.swant.meitu

import android.content.Context
import android.content.SharedPreferences
import edu.tjrac.swant.baselib.common.base.BaseApplication
import edu.tjrac.swant.meitu.bean.User

/**
 * Created by wpc on 2019-11-20.
 */
open class App : BaseApplication() {
    companion object{
        var context: Context? = null
        var sp: SharedPreferences? = null
            get() {
                if (field == null) {
                    field = context?.getSharedPreferences(Config.SP.CACHE, Context.MODE_PRIVATE)
                }
                return field
            }
        var token: String? = ""
            set(value) {
                sp?.edit()?.putString(Config.SP.TOKEN, value)?.commit()
                field = token
            }
            get() {
                field=sp?.getString(Config.SP.TOKEN,"")
                return field
            }
        var loged: User? = User(1)
    }


    override fun onCreate() {
        context=this
        super.onCreate()
    }
}