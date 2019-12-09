package edu.tjrac.swant.bluetooth.view

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanResult
import android.os.Build
import android.os.Bundle
import android.os.ParcelUuid
import android.support.annotation.RequiresApi
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import edu.tjrac.swant.baselib.common.base.BaseFragment
import edu.tjrac.swant.wjzx.R

/**
 * Created by wpc on 2018/2/1 0001.
 */
@SuppressLint("ValidFragment")
class ConnectDeviceFragment(private val parent: BLEFragment, private val result: ScanResult)//        result.getSecondaryPhy();
    : BaseFragment() {

    private var mUuids: Array<ParcelUuid>? = null
    var device: BluetoothDevice? = null
        private set

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_connected_devices, container, false)
        return view
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.device = result.device
        mUuids = device!!.uuids

        val sb = StringBuffer()
        sb.append(mUuids!!.toString())

        //        parent.refeshTitle(mDevice);
        super.onViewCreated(view, savedInstanceState)
    }

   override fun getTitle(): String {
        return device!!.name!! + "\n" + device!!.address
    }

    override fun onBack() {

    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}
