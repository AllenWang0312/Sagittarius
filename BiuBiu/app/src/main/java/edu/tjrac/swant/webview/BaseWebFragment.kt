package edu.tjrac.swant.webview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.google.gson.Gson
import edu.tjrac.swant.baselib.common.base.BaseWebViewFragment
import edu.tjrac.swant.baselib.util.FileUtils
import edu.tjrac.swant.baselib.util.SUtil
import edu.tjrac.swant.webview.bean.ChromeCache
import edu.tjrac.swant.webview.bean.Website
import edu.tjrac.swant.wjzx.R
import java.io.File

class BaseWebFragment(url: String) : BaseWebViewFragment(url, R.layout.base_web_fragment) {

    var cachePath: String? = null
    var histroy: ChromeCache? = null

    constructor(cachePath: String, histroy: ChromeCache) : this(histroy.histroies?.last()?.url!!) {
        this.cachePath = cachePath
        this.histroy = histroy
    }

    var data: ArrayList<MultiItemEntity>? = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = super.onCreateView(inflater, container, savedInstanceState)
        initContent()
        return view
    }

    fun initContent() {

        var fl_web = v?.findViewById<FrameLayout>(R.id.fl_web)

        if (SUtil.isEmpty(url)) {
            var default = LayoutInflater.from(activity).inflate(R.layout.web_default_home, null)
            data?.add(Website("https://lottiefiles.com/"))
            data?.add(Website("https://m.fengniao.com/"))

            var recycler = default.findViewById<RecyclerView>(R.id.recycler)
            recycler?.layoutManager = GridLayoutManager(activity!!, 4)
            var adapter = WebSiteAdapter(data)
            adapter?.setOnItemClickListener { ad, view, position ->
                var item = adapter?.getItem(position)
                if (item is Website) {
                    url = item.url
                    webview?.loadUrl(item.url)
                    initContent()
                }
            }
            recycler?.adapter = adapter
            default?.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
            fl_web?.addView(default)
        } else {
            if (fl_web?.childCount == 3) {
                fl_web?.removeViewAt(2)
            }
            if (histroy != null) {
                for (i in histroy?.histroies!!) {
                    webview?.loadUrl(i.url)
                }
            } else {
                webview?.loadUrl(url)
            }
        }
    }


    override fun onDestroy() {
        var cache = File(activity?.getExternalFilesDir(null), "/cache/chrome/cache/")
//        var filename:String
        var cacheFile: File
        if (SUtil.isEmpty(cachePath)) {
            cacheFile = File(cache, "" + System.currentTimeMillis() + ".cache")
        } else {
            cacheFile = File(cachePath)
        }
        if (!cacheFile.exists()) {
            cacheFile.mkdirs()
            cacheFile.createNewFile()
        }
        FileUtils.writeByteArrayToFile(Gson().toJson(getHistroys()).toByteArray(), cacheFile)
        super.onDestroy()
    }

    fun getHistroys(): ChromeCache? {
        var histroy = ChromeCache()
        var list = webview?.copyBackForwardList()
        var size = list?.size!!
        if (size > 0) {
            for (i in 0 until size) {
                var item = list?.getItemAtIndex(i)
                histroy.histroies?.add(Histroy(item.title, item.url, item.favicon!!))
            }
            return histroy
        }
        return null
    }

    override fun getTitle(): String {
        var title = webview?.title
        if (!SUtil.isEmpty(title) && !title?.startsWith("http")!!) {
            return title
        } else {
            return super.getTitle()
        }
    }

}
