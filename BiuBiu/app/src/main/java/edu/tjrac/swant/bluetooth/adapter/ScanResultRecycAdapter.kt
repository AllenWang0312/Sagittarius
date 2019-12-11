package edu.tjrac.swant.bluetooth.adapter

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanResult
import android.content.SharedPreferences
import android.view.View
import android.widget.LinearLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import edu.tjrac.swant.baselib.util.SUtil
import edu.tjrac.swant.bluetooth.BlueToothHelper
import edu.tjrac.swant.wjzx.R
import java.util.*

/**
 * Created by wpc on 2018/1/26 0026.
 */
@SuppressLint("NewApi")
class ScanResultRecycAdapter(internal var sp: SharedPreferences, data: List<ScanResult>?) : BaseQuickAdapter<ScanResult, BaseViewHolder>(R.layout.item_scan_result, data) {

    var expendIndex = -1
        internal set
    internal var last_index = -1


    internal var finishScanDevices: MutableList<String>? = null

    @SuppressLint("MissingPermission")
    override fun convert(helper: BaseViewHolder, item: ScanResult) {
        val device = item.device

        //        item.getScanRecord().getAdvertiseFlags()
        val flags = BlueToothHelper.getFlags(item.scanRecord!!.advertiseFlags)

        helper.setText(R.id.tv_flags, BlueToothHelper.getFlags(flags))

        if (sp.getBoolean(device.address, false) == true) {
            helper.setChecked(R.id.cb_favourite, true)
        } else {
            helper.setChecked(R.id.cb_favourite, false)
        }
        helper.addOnClickListener(R.id.iv_icon)

        if (SUtil.isEmpty(device.name)) {
            helper.setText(R.id.tv_name, "N/A")
        } else {
            helper.setText(R.id.tv_name, device.name)
        }
        if (device.bondState == BluetoothDevice.BOND_BONDED) {
            helper.setText(R.id.tv_bound_state, "BONDED")
        } else if (device.bondState == BluetoothDevice.BOND_BONDING) {
            helper.setText(R.id.tv_bound_state, "BONDING")
        } else {
            helper.setText(R.id.tv_bound_state, "NOT BONDED")
        }
        helper.setText(R.id.tv_address, device.address)

        helper.setText(R.id.tv_dbm, item.rssi.toString() + "dBm")
        if (finishScanDevices != null && finishScanDevices!!.size > 0 && finishScanDevices!!.contains(device.address)) {
            helper.getView<View>(R.id.tv_dbm).alpha = 0.5f
            //            helper.setTextColor(R.id.tv_dbm, mContext.getResources().getColor(R.color.gray));
        } else {
            helper.getView<View>(R.id.tv_dbm).alpha = 1f
            //            helper.setTextColor(R.id.tv_dbm, mContext.getResources().getColor(R.color.black));
        }

        helper.addOnClickListener(R.id.tv_connect)
        helper.addOnClickListener(R.id.iv_options)
        val expand = helper.getView<LinearLayout>(R.id.ll_expand)
        if (helper.position == expendIndex) {
            expand.visibility = View.VISIBLE
            helper.setText(R.id.tv_device_type, BlueToothHelper.getType(device.type))
            //            List<ParcelUuid> mServiceUuids = item.getScanRecord().getServiceUuids();
            val mServiceUuids = item.device.uuids
            if (mServiceUuids != null) {
                helper.setText(R.id.tv_uuid, mServiceUuids.toString())
            } else {
                helper.setText(R.id.tv_uuid, "null")
            }

            helper.setText(R.id.tv_local_name, item.device.name)
            helper.addOnClickListener(R.id.bt_raw)
            helper.addOnClickListener(R.id.bt_more)
        } else {
            expand.visibility = View.GONE
        }
    }


    //    static class BluetoothDeviceInfo {
    //        BluetoothDevice device;
    //        short rssi;
    //
    //        public BluetoothDeviceInfo(BluetoothDevice device) {
    //            this.device = device;
    //        }
    //
    //        public BluetoothDevice getDevices() {
    //            return device;
    //        }
    //
    //        public void setDevices(BluetoothDevice device) {
    //            this.device = device;
    //        }
    //
    //        public short getRssi() {
    //            return rssi;
    //        }
    //
    //        public void setRssi(short rssi) {
    //            this.rssi = rssi;
    //        }
    //    }

    fun setExpendItem(index: Int) {
        this.last_index = expendIndex
        this.expendIndex = index
        if (last_index > -1) {
            notifyItemChanged(last_index)
        }
        if (expendIndex > -1) {
            notifyItemChanged(expendIndex)
        }
    }

    fun scanFinish(results: List<ScanResult>) {
        finishScanDevices = ArrayList()
        for (item in results) {
            finishScanDevices!!.add(item.device.address)
        }
        notifyDataSetChanged()
    }
}
