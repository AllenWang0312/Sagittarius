package edu.tjrac.swant.bluetooth.view

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import edu.tjrac.swant.baselib.common.base.BaseFragment
import edu.tjrac.swant.baselib.uncom.ArrayUtil
import edu.tjrac.swant.bluetooth.adapter.BoundedDevicesAdapter

import edu.tjrac.swant.wjzx.R
import kotlinx.android.synthetic.main.found_swiper_recycler.*
import java.util.*

/**
 * Created by wpc on 2018/1/31 0031.
 */

@SuppressLint("ValidFragment")
class BondDevicesFragment(private val parent: BLEFragment, private val adapter: BluetoothAdapter, title: String) : BaseFragment() {

//    init {
//        this.title = title
//    }

    internal var bound_adapter: BoundedDevicesAdapter? = null

    internal var bounded_devices: Set<BluetoothDevice>? = null
    internal var devices: List<BluetoothDevice> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.bond_swiper_recycler, container, false)

        //        L.i("bounded devices", bounded_devices.toString());
        swiper!!.setOnRefreshListener { initData() }
        recycler!!.layoutManager = LinearLayoutManager(activity)

        bound_adapter = BoundedDevicesAdapter(devices)
        bound_adapter!!.onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { adapter, view, position ->
            if (view.id == R.id.iv_options) {

            } else {

            }
        }
        bound_adapter!!.bindToRecyclerView(recycler)
        bound_adapter!!.setEmptyView(R.layout.empty)
        recycler!!.adapter = bound_adapter
        initData()
        return view
    }

    internal fun initData() {
        bounded_devices = adapter.bondedDevices
        devices = ArrayUtil<BluetoothDevice>().asArray(bounded_devices)
        bound_adapter!!.notifyDataSetChanged()
        //        if(bound_adapter.getEmptyViewCount()>0){

        //        }
        swiper!!.isRefreshing = false
    }

    override fun onBack() {

    }
}
