package edu.tjrac.swant.webview

import android.os.Bundle
import android.view.View
import com.google.gson.Gson
import edu.tjrac.swant.baselib.common.adapter.SupportFragmentsPagerAdapter
import edu.tjrac.swant.baselib.common.base.BaseBarActivity
import edu.tjrac.swant.baselib.util.FileUtils
import edu.tjrac.swant.webview.bean.ChromeCache
import edu.tjrac.swant.wjzx.R
import kotlinx.android.synthetic.main.activity_chrome.*
import java.io.File

class ChromeActivity : BaseBarActivity() {
    var adapter: SupportFragmentsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chrome)
        setToolbar(findViewById(R.id.toolbar))

        adapter = SupportFragmentsPagerAdapter(supportFragmentManager)

        var cacheDir = File(getExternalFilesDir(null), "/cache/chrome/cache")
        if (cacheDir.exists() && cacheDir.isDirectory) {
            var caches = cacheDir.listFiles()
            if (caches.size > 0) {
                for (i in caches) {
                    try {
                        var histroy = Gson().fromJson<ChromeCache>(FileUtils.readFile(i), ChromeCache::class.java)
                        adapter?.addFragment(BaseWebFragment(i.absolutePath, histroy), histroy.histroies?.last()?.title!!)
                    } catch (e: Exception) {

                    }
                }
            } else {
                adapter?.addFragment(BaseWebFragment(""), "首页")
            }
        } else {
            adapter?.addFragment(BaseWebFragment(""), "首页")
        }
        vp?.adapter = adapter
        tab?.setupWithViewPager(vp)

    }

    override fun setToolbar(tool: View) {
        super.setToolbar(tool)
        enableBackIcon()

    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
