package edu.tjrac.swant.wjzx.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.amap.api.maps.AMap
import com.amap.api.maps.model.Marker
import edu.tjrac.swant.wjzx.R

class MyInfoAdapter(var context: Context) : AMap.InfoWindowAdapter {
    /**
     * 监听自定义infowindow窗口的infocontents事件回调
     */
    override fun getInfoContents(marker: Marker): View? {
        return null
        //示例没有采用该方法。
    }

    var infoWindow: View? = null

    /**
     * 监听自定义infowindow窗口的infowindow事件回调
     */
    override fun getInfoWindow(marker: Marker): View? {
        if (infoWindow == null) {
            infoWindow = LayoutInflater.from(context).inflate(
                R.layout.custom_info_window, null
            )
        }
        render(marker, infoWindow)
        return infoWindow
        //加载custom_info_window.xml布局文件作为InfoWindow的样式
        //该布局可在官方Demo布局文件夹下找到
    }

    fun render(marker: Marker, view: View?) {

    }

}