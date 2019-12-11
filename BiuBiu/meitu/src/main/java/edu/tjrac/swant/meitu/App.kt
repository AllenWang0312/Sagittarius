package edu.tjrac.swant.meitu

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatDelegate
import android.telephony.TelephonyManager
import android.util.Log
import com.google.gson.Gson
import edu.tjrac.swant.baselib.common.base.BaseApplication
import edu.tjrac.swant.baselib.util.SUtil
import edu.tjrac.swant.meitu.bean.User

/**
 * Created by wpc on 2019-11-20.
 */
open class App : BaseApplication() {
    companion object {
        var context: Context? = null
        var device: String? = "device"
        var needReStart = false
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
                field = value
                Log.i("token", value)
            }
            get() {
                if (SUtil.isEmpty(field)) {
                    field = sp?.getString(Config.SP.TOKEN, "")
                }
                return field
            }
        var loged: User? = null
            get() {
                if (null == field) {
                    var user = sp?.getString(Config.SP.LOGINED_USER, "")
                    field = Gson().fromJson(user, User::class.java)
                }
                return field
            }
            set(value) {
                field = value
                var user: String
                if (field == null) {
                    user = ""
                } else {
                    user = Gson().toJson(field)
                }

                sp?.edit()?.putString(Config.SP.LOGINED_USER, user!!)?.commit()
            }

        var isNightMode: Boolean? = null
            get() {
                if (null == field) {
                    field = sp?.getBoolean(Config.SP.ISNIGHT_MODE, true)
                }
                return field
            }
            set(value) {
                field = value
                needReStart = true
                sp?.edit()?.putBoolean(Config.SP.ISNIGHT_MODE, value!!)?.commit()
            }
        var followSystem: Boolean? = null
            get() {
                if (null == field) {
                    field = sp?.getBoolean(Config.SP.LANGUAGE_FOLLOW_SYSTEM, false)
                }
                return field
            }
            set(value) {
                field = value
                needReStart = true
                sp?.edit()?.putBoolean(Config.SP.LANGUAGE_FOLLOW_SYSTEM, value!!)?.commit()
            }
        var languageSetting: Int? = 0
            get() {
                if (null == field) {
                    field = sp?.getInt(Config.SP.LANGUAGE_SETTING, 0)
                }
                return field
            }
            set(value) {
                field = value
                needReStart = true
                sp?.edit()?.putInt(Config.SP.LANGUAGE_SETTING, value!!)?.commit()
            }
        var hot_line: String? = "18814837150"
        @JvmStatic
        fun getCachePath(): String {
            return instance?.cacheDir!!.absolutePath
        }
    }


    @SuppressLint("MissingPermission")
    override fun onCreate() {
        context = this

        var tm = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        device = tm.getDeviceId()

        super.onCreate()
        if (isNightMode!!) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}