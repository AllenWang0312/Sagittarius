package edu.tjrac.swant.bluetooth

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.os.ParcelUuid
import edu.tjrac.swant.baselib.util.L
import java.lang.reflect.Method
import java.util.*

/**
 * Created by LuHao on 2016/9/26.
 */
object BlueToothHelper {


    /**
     * UUID identified with this app - set as Service UUID for BLE Advertisements.
     *
     *
     * Bluetooth requires a certain format for UUIDs associated with Services.
     * The official specification can be found here:
     * [://www.bluetooth.org/en-us/specification/assigned-numbers/service-discovery][https]
     */

    var HEART_RATE_MEASUREMENT = "00002a37-0000-1000-8000-00805f9b34fb"
    var CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb"

    val Service_UUID = ParcelUuid
            .fromString("0000b81d-0000-1000-8000-00805f9b34fb")

    val REQUEST_ENABLE_BT = 1

    /**
     * 蓝牙UUID
     */
    //    public static final UUID SPP_UUID = UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb");
    var SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")


    //启用蓝牙
    val BLUE_TOOTH_OPEN = 1000
    //禁用蓝牙
    val BLUE_TOOTH_CLOSE = BLUE_TOOTH_OPEN + 1
    //搜索蓝牙
    val BLUE_TOOTH_SEARTH = BLUE_TOOTH_CLOSE + 1
    //被搜索蓝牙
    val BLUE_TOOTH_MY_SEARTH = BLUE_TOOTH_SEARTH + 1
    //关闭蓝牙连接
    val BLUE_TOOTH_CLEAR = BLUE_TOOTH_MY_SEARTH + 1

    var flagsValue: Array<String>?=null
    var keySet: Array<String>?=null

    fun getType(type: Int): String {
        when (type) {
            1 -> return "CLASSIC"
            2 -> return "BLE only"
            3 -> return "CLASSIC and BLE"
            else -> return "UNKNOWN"
        }
    }

    fun getStringUUIDs(serviceUuids: List<ParcelUuid>?): String {
        if (serviceUuids != null && serviceUuids.size > 0) {
            val sb = StringBuffer()
            for (id in serviceUuids) {
                sb.append(id.toString())
                sb.append(",")
            }
            return sb.toString().substring(0, sb.length - 1)
        } else {
            return ""
        }
    }

    fun initRes(context: Context) {
        flagsValue = context.resources.getStringArray(R.array.bluetooth_service_type_flags)
        keySet = context.resources.getStringArray(R.array.scan_info_key_list)

    }

    fun getFlags(flags: BooleanArray): String {
        val sb = StringBuffer()
        for (i in flags.indices) {
            if (flags[5 - i]) {
                sb.append(flagsValue?.get(i))
                sb.append(",")
            }
        }
        return if (sb.length > 0) {
            sb.toString().substring(0, sb.length - 1)
        } else {
            ""
        }

    }

    fun getFlags(advertiseFlags: Int): BooleanArray {
        //        int flag = advertiseFlags;
        val a = 0x000001
        var i1 = advertiseFlags
        val flags = BooleanArray(6)
        for (i in flags.indices) {
            flags[5 - i] = a and i1 == 0x01
            i1 = i1 shr 1
            L.i("getFlags:" + (5 - i), flags[5 - i].toString())
        }
        L.i("getFlags: result = $advertiseFlags",
                flags.toString())
        return flags
    }

    fun createBond(device: BluetoothDevice) {
        val createBondMethod: Method
        try {
            createBondMethod = BluetoothDevice::class.java
                    .getMethod("createBond")
            createBondMethod.invoke(device)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
