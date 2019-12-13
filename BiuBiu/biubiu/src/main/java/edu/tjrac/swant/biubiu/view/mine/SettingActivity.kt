package edu.tjrac.swant.biubiu.view.mine

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatDelegate
import android.view.View
import android.widget.RadioButton
import edu.tjrac.swant.baselib.common.base.BaseApplication
import edu.tjrac.swant.baselib.common.base.BaseBarActivity
import edu.tjrac.swant.biubiu.BiuBiuApp
import edu.tjrac.swant.biubiu.BiuBiuApp.Companion.followSystem
import edu.tjrac.swant.biubiu.BiuBiuApp.Companion.isNightMode
import edu.tjrac.swant.biubiu.BiuBiuApp.Companion.languageSetting
import edu.tjrac.swant.biubiu.BiuBiuApp.Companion.needReStart
import edu.tjrac.swant.biubiu.MeituMainActivity
import edu.tjrac.swant.biubiu.R
import edu.tjrac.swant.biubiu.view.account.MeituLoginActivity
import kotlinx.android.synthetic.main.activity_setting.*
import java.util.*


class SettingActivity : BaseBarActivity() {

    //    var sp: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
//        sp = getSharedPreferences(Config.SP.CACHE, Context.MODE_PRIVATE)
//        var isNightMode = sp?.getBoolean(Config.SP.ISNIGHT_MODE, false)

        //            recreate()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        setToolbar(findViewById(R.id.toolbar))


        sw_night.isChecked = isNightMode!!
        initLanguageSettingUI(followSystem, languageSetting)

        sw_night.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            isNightMode = isChecked
            recreate()
        }

        sw_language.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                changeLanguage(Locale.getDefault())
            } else {
                for (i in 0 until rg_languages.childCount) {
                    if ((rg_languages.getChildAt(i) as RadioButton).isChecked) {
//        languageSetting=i
                        if (i == 0) {
                            changeLanguage(Locale.SIMPLIFIED_CHINESE)
                        } else if (i == 1) {
                            changeLanguage(Locale.TRADITIONAL_CHINESE)
                        } else if (i == 2) {
                            changeLanguage(Locale.ENGLISH)
                        }
                    }
                }
            }
            followSystem = isChecked
            recreate()
//            initLanguageSettingUI(isChecked)
        }
        rg_languages.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_0 -> {
                    languageSetting = 0
                    changeLanguage(Locale.SIMPLIFIED_CHINESE)
                    recreate()
                }
                R.id.rb_1 -> {
                    languageSetting = 1
                    changeLanguage(Locale.TRADITIONAL_CHINESE)
                    recreate()
                }
                R.id.rb_2 -> {
                    languageSetting = 2
                    changeLanguage(Locale.ENGLISH)
                    recreate()
                }
            }
//            recreate()
        }
        bt_exit.setOnClickListener {
            BiuBiuApp.token = ""
            BiuBiuApp.loged = null
            BaseApplication.instance?.exit()
            startActivity(Intent(this, MeituLoginActivity::class.java))
        }
    }

    private fun initLanguageSettingUI(set: Boolean?, languageSetting: Int?) {
        if (set!!) {
            sw_language.isChecked = true
            rb_0.isEnabled = false
            rb_1.isEnabled = false
            rb_2.isEnabled = false
        } else {
            sw_language.isChecked = false
            rb_0.isEnabled = true
            rb_1.isEnabled = true
            rb_2.isEnabled = true
        }
        (rg_languages.getChildAt(languageSetting!!) as RadioButton).isChecked = true
    }

    override fun setToolbar(tool: View) {
        super.setToolbar(tool)
        enableBackIcon()
        title = getString(R.string.settings)
    }

    fun changeLanguage(locale: Locale) {
// 获得屏幕参数：主要是分辨率，像素等。
        val metrics = resources.getDisplayMetrics()
// 获得配置对象
        val config = resources.getConfiguration()
//区别17版本（其实在17以上版本通过 config.locale设置也是有效的，不知道为什么还要区别）
//在这里设置需要转换成的语言，也就是选择用哪个values目录下的strings.xml文件
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale)//设置简体中文
        } else {
            config.locale = locale//设置简体中文
        }
        resources.updateConfiguration(config, metrics)
    }

    override fun onBackPressed() {
        if (needReStart) {
            BaseApplication.instance?.exit()
            startActivity(Intent(this, MeituMainActivity::class.java))
            needReStart = false
        } else {
            super.onBackPressed()
        }
    }
}
