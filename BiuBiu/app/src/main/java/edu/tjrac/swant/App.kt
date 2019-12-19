package edu.tjrac.swant

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import edu.tjrac.swant.biubiu.BiuBiuApp

class App : BiuBiuApp() {
    companion object {

        var context: Context? = null
        var sp: SharedPreferences? = null
            get() {
                if (field == null) {
                    field = context?.getSharedPreferences(Config.SP.CACHE, Context.MODE_PRIVATE)
                }
                return field
            }
        var needReStart = false
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
    }


    override fun onCreate() {
        super.onCreate()
        context = this
        if (isNightMode!!) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}