package edu.tjrac.swant.map

import android.content.Context
import android.content.SharedPreferences
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.UiSettings
import com.amap.api.maps.model.*
import edu.tjrac.swant.baselib.common.base.BaseFragment
import edu.tjrac.swant.wjzx.Config
import edu.tjrac.swant.wjzx.NearUser
import edu.tjrac.swant.wjzx.R
import edu.tjrac.swant.wjzx.Random
import edu.tjrac.swant.wjzx.adapter.MyInfoAdapter
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.android.synthetic.main.fragment_map.view.*

class MapFragment : BaseFragment(), AMap.OnMyLocationChangeListener,
    View.OnClickListener {
    companion object {
        var mPosition: Location? = null
        val TAG = "mainactivity"
    }

    override fun onMyLocationChange(loc: Location?) {
        Log.i(TAG, "onMyLocationChange" + loc?.latitude.toString() + "_" + loc?.longitude)
        //从location对象中获取经纬度信息，地址描述信息，建议拿到位置之后调用逆地理编码接口获取（获取地址描述数据章节有介绍）
        if (mPosition == null) {
//            resetCamera(position)
        }
        mPosition = loc

    }

    var nearby: ArrayList<NearUser>? = null
    //    var map_types = arrayOf("普通地图", "夜景地图", "导航地图", "卫星图")
    var lock = true
    var aMap: AMap? = null
    var myLocationStyle: MyLocationStyle? = null

    var mUiSettings: UiSettings? = null


    var lastBearing = 0f;
    var rotateAnimation: RotateAnimation? = null
    var sp: SharedPreferences? = null
    var isNightMode: Boolean? = null
        get() {
            if (field == null) {
                field = sp?.getBoolean(Config.SP.ISNIGHT_MODE, false)
            }
            return field
        }
        set(value) {
            field = value
            sp?.edit()?.putBoolean(Config.SP.ISNIGHT_MODE, value!!)?.commit()
        }

    fun startIvCompass(cp: CameraPosition?) {
        var bearing = 315 - cp?.bearing!!
        Log.i(TAG, "bearing:" + bearing.toString())
        rotateAnimation =
            RotateAnimation(lastBearing, bearing, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        rotateAnimation?.fillAfter = true;
        iv_compass.startAnimation(rotateAnimation)
        lastBearing = bearing
    }

    private fun randomData() {
        nearby = ArrayList()
        for (i in 0..10) {
            var near = NearUser("", Random.name())
            aMap?.addMarker(MarkerOptions().position(near.location).title(near.userName).snippet("DefaultMarker"))
            nearby?.add(near)
        }
    }

    override fun onResume() {
        super.onResume()
        v.map?.onResume()
    }

    override fun onPause() {
        super.onPause()
        v.map?.onPause()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
//            R.id.lav_switcher -> {
//                if (!isNightMode!!) {
//                    delegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//                } else {
//                    delegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//                }
//                isNightMode = !isNightMode!!
//                recreate()
//            }
            R.id.bt_scan -> {
                randomData()
            }
            R.id.bt_location -> {
                lock = !lock
                if (lock) {
                    bt_location.setImageDrawable(resources.getDrawable(R.drawable.ic_my_location_white_18dp))
                    myLocationStyle?.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE)
                } else {
                    bt_location.setImageDrawable(resources.getDrawable(R.drawable.ic_location_searching_white_18dp))
                    myLocationStyle?.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER)
                }
                aMap?.myLocationStyle = myLocationStyle
            }
        }
    }

    lateinit var v: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_map, container, false)
        sp = activity?.getSharedPreferences(Config.SP.CACHE, Context.MODE_PRIVATE)
        v.map?.onCreate(savedInstanceState)
        aMap = v.map?.map

        mUiSettings = aMap?.uiSettings
        mUiSettings?.isZoomControlsEnabled = false//不显示缩放
        mUiSettings?.isMyLocationButtonEnabled = false//设置默认定位按钮是否显示，非必需设置。
        mUiSettings?.isCompassEnabled = true//指南针
        mUiSettings?.isScaleControlsEnabled = true;

//设置map样式
//        aMap?.setMapCustomEnable(true)
//定位设置
        aMap?.isMyLocationEnabled = true;
        myLocationStyle = MyLocationStyle()
//        myLocationStyle?.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_navigation_grey_400_18dp))
        myLocationStyle?.strokeColor(0x9d6fd3)
        myLocationStyle?.radiusFillColor(0x409d6fd3)
        myLocationStyle?.strokeWidth(10f)
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW);//只定位一次。
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE) ;//定位一次，且将视角移动到地图中心点。
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW) ;//连续定位、且将视角移动到地图中心点，定位蓝点跟随设备移动。（1秒1次定位）
        myLocationStyle?.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE);//连续定位、且将视角移动到地图中心点，地图依照设备方向旋转，定位点会跟随设备移动。（1秒1次定位）
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）默认执行此种模式。
//以下三种模式从5.1.0版本开始提供
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，并且蓝点会跟随设备移动。
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动。
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，地图依照设备方向旋转，并且蓝点会跟随设备移动。
        //初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        // 连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle?.interval(2000) //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。

        aMap?.myLocationStyle = myLocationStyle//设置定位蓝点的Style
        aMap?.setOnMyLocationChangeListener(this)
//        aMap?.setOnMapLoadedListener {
//            //            mPosition = aMap?.myLocation!!
////            Log.i(TAG, "onMyLocationChange" + mPosition?.latitude.toString() + "_" + mPosition?.longitude)
////            resetCamera(mPosition!!)
//            if (!isNightMode!!) {
//                lav_switcher.setMinAndMaxProgress(0.0f, 0.5f)//to night
//                setMapStyle(1)
//            } else {
//                lav_switcher.setMinAndMaxProgress(0.5f, 1.0f)
//                setMapStyle(0)
//            }
//            lav_switcher.playAnimation()
//        }
        aMap?.setOnCameraChangeListener(object : AMap.OnCameraChangeListener {
            override fun onCameraChangeFinish(cameraPosition: CameraPosition?) {
                startIvCompass(cameraPosition);
            }

            override fun onCameraChange(p0: CameraPosition?) {
            }

        });
        aMap?.setInfoWindowAdapter(MyInfoAdapter(activity!!))

//        aMap.setTrafficEnabled(true);//显示交通状况
//        aMap?.setMapLanguage(AMap.CHINESE)//设置语言

//        aMap?.setCustomMapStylePath(Config.AMAP_STYLE_PATH);
//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
//        resetCamera(mPosition)
//        v.lav_switcher.setOnClickListener(this)
        v.bt_location.setOnClickListener(this)
        v.bt_scan.setOnClickListener(this)
//        spinner.adapter=
//        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//            }
//
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                setMapStyle(position)
////aMap?.mapType = AMap.MAP_TYPE_NORMAL
//                //MAP_TYPE_NAVI导航
//                //MAP_TYPE_NIGHT夜景
//                //MAP_TYPE_NORMAL白昼
//                //MAP_TYPE_SATELLITE卫星
//            }
//        }

        return v
    }

    private fun setMapStyle(position: Int) {
        var options = CustomMapStyleOptions()
        options.styleDataPath = Config.STATIC_CACHE_PATH + "/" + position + "/style.data"
        options.styleExtraPath = Config.STATIC_CACHE_PATH + "/" + position + "/style_extra.data"
        aMap?.setCustomMapStyle(options)
    }

    private fun resetCamera(latLng: LatLng) {
        //参数依次是：视角调整区域的中心点坐标、希望调整到的缩放级别、俯仰角0°~45°（垂直与地图时为0）、偏航角 0~360° (正北方为0)
        val mCameraUpdate = CameraUpdateFactory.newCameraPosition(CameraPosition(latLng, 18f, 0f, 0f))
        aMap?.moveCamera(mCameraUpdate)
    }

    private fun resetCamera(latLng: Location) {
        //参数依次是：视角调整区域的中心点坐标、希望调整到的缩放级别、俯仰角0°~45°（垂直与地图时为0）、偏航角 0~360° (正北方为0)
        val mCameraUpdate = CameraUpdateFactory.newCameraPosition(
            CameraPosition(
                LatLng(latLng.latitude, latLng.longitude),
                18f,
                0f,
                0f
            )
        )
        aMap?.moveCamera(mCameraUpdate)
    }

    override fun onDestroy() {
        super.onDestroy()
        v.map?.onDestroy()
    }

//    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
//        super.onSaveInstanceState(outState, outPersistentState)
//        map.onSaveInstanceState(outState)
//    }

}
