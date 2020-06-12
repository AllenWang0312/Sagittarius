package edu.tjrac.swant.bluetooth.view

import android.annotation.TargetApi
import android.bluetooth.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.*
import android.widget.TextView
import edu.tjrac.swant.baselib.common.base.BaseFragment
import edu.tjrac.swant.baselib.util.T
import edu.tjrac.swant.bluetooth.BlueToothHelper
import edu.tjrac.swant.bluetooth.adapter.BLEFragmentsPagerAdapter
import edu.tjrac.swant.bluetooth.R
import kotlinx.android.synthetic.main.fragment_ble.view.*
import java.io.IOException
import java.util.*

class BLEFragment : BaseFragment() {

    //    @BindView(R.id.tab_layout) TabLayout mTabLayout;
    //    @BindView(R.id.vp) ViewPager mVp;
    internal var content_adapter: BLEFragmentsPagerAdapter? = null

    //    @BindView(R.id.rssi) RSSIView mRSSIView;
    internal var adapter: BluetoothAdapter? = null

    //    BluetoothLeScanner scanner;
    //首先获取BluetoothManager
    internal var bluetoothManager: BluetoothManager? = null
    private var mBluetoothGatt: BluetoothGatt? = null


    private val connectReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)

            //            L.i("BLEFragment:onReceive", action)
            //            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
            //                Log.i("onReceive", "ACTION_GATT_CONNECTED");
            //            }else
            if (BluetoothDevice.ACTION_ACL_CONNECTED === action) {
                //从Intent得到blueDevice对象
                connectedSockets.add(mBluetoothSocket!!)
                connectedDevices.add(device)

                //                L.i("RSSI:" + device.address, intent.extras.getShort(BluetoothDevice.EXTRA_RSSI).toString() + "")
                content_adapter?.addFragment(ConnectDeviceFragment(this@BLEFragment, found?.found_devices!![found?.found_flags?.indexOf(device.address)!!]))
                content_adapter?.notifyDataSetChanged()
                refeshTitle(device)

            } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED === action) {
                content_adapter?.remove(device.address)
            }
        }
    }

    //    BluetoothAdapter.LeScanCallback callback = new BluetoothAdapter.LeScanCallback() {
    //        @Override
    //        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
    //            L.i("onLeScan", device.getAddress());
    //            if (!scaned_devices.contains(device)) {
    //                scaned_devices.add(device);
    //                result_adapter.notifyItemInserted(scaned_devices.size() - 1);
    //            }
    //        }
    //    };

    //    ScanCallback callback = new ScanCallback() {
    //        @Override
    //        public void onScanResult(int callbackType, ScanResult result) {
    //            L.i("onScanResult", result.getDevice().getName());
    //            if (result != null && result.getDevice() != null) {
    //                if (!scaned_devices.contains(result.getDevice())) {
    //                    scaned_devices.add(result.getDevice());
    //                    result_adapter.notifyItemInserted(scaned_devices.size() - 1);
    //                }
    //            }
    //            super.onScanResult(callbackType, result);
    //        }
    //
    //        @Override
    //        public void onBatchScanResults(List<ScanResult> results) {
    //            L.i("onBatchScanResults", results.size() + "");
    //            super.onBatchScanResults(results);
    //        }
    //
    //        @Override
    //        public void onScanFailed(int errorCode) {
    //            ic_scan.setTitle("ic_scan");
    //            T.show(getActivity(), "ic_scan failed");
    //            super.onScanFailed(errorCode);
    //        }
    //    };


    //    private boolean contains(ArrayList<ScanResultRecycAdapter.BluetoothDeviceInfo> scaned_devices, BluetoothDevice device) {
    //        for (ScanResultRecycAdapter.BluetoothDeviceInfo item : scaned_devices) {
    //            if (item.getDevices().getAddress() == device.getAddress()) {
    //                return true;
    //            }
    //        }
    //        return false;
    //    }
    var handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
        }
    }

    var found: FoundDevicesFragment? = null
    var bond: BondDevicesFragment? = null
    var advertiser: AdvertiserFragment? = null

    var v: View? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        BlueToothHelper.initRes(activity!!)
        bluetoothManager = activity?.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        v = inflater.inflate(R.layout.fragment_ble, container, false)

        adapter = bluetoothManager?.adapter

        //        adapter = BluetoothAdapter.getDefaultAdapter();


        if (adapter == null) {
            T.show(activity!!, "当前设备不支持蓝牙功能")
        }

        val filter = IntentFilter()
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
        activity?.registerReceiver(connectReceiver, filter)

        //        found_head= (CardView) LayoutInflater.from(getActivity()).inflate(R.layout.found_devices_filters, null);
        //        cb_found_head = (CheckBox) found_head.findViewById(R.id.cb_filter);
        //        found_swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
        //            @Override
        //            public void onRefresh() {
        //
        //            }
        //        });
        //         = found_swiper.findViewById(R.id.recycler);


        //        View connectDeviceView = LayoutInflater.from(getActivity()).inflate(R.layout.connected_device_view, null);

        content_adapter = BLEFragmentsPagerAdapter(activity, this, childFragmentManager, v?.tab_layout)
        found = FoundDevicesFragment(this, adapter!!, "found")
        content_adapter?.addFragment(found)
        bond = BondDevicesFragment(this, adapter!!, "bond")
        content_adapter?.addFragment(bond)
        advertiser = AdvertiserFragment("advertiser")
        content_adapter?.addFragment(advertiser)
        //        content_adapter.addView(found, "Found");

        if (adapter == null) {
            T.show("BlueTooth is unable")
        } else {
            //            View advertiser = LayoutInflater.from(getActivity()).inflate(R.layout.ble_advertiser, null);
            //            content_adapter.addView(advertiser, "advertiser");
        }
        //        ImageView imageView = new ImageView(getActivity());
        //        imageView.setImageResource(R.drawable.__leak_canary_icon);
        //        content_adapter.addView(imageView, "Image");
        v?.vp?.adapter = content_adapter
        v?.vp?.offscreenPageLimit = 4
        v?.tab_layout?.setupWithViewPager(v?.vp)
        return v
    }

    private var mBluetoothSocket: BluetoothSocket? = null

    //2.x
    fun connect(btDev: BluetoothDevice, handler: Handler?) {
        try {
            //通过和服务器协商的uuid来进行连接
            mBluetoothSocket = btDev.createRfcommSocketToServiceRecord(BlueToothHelper.SPP_UUID)
            if (mBluetoothSocket != null)
            //全局只有一个bluetooth，所以我们可以将这个socket对象保存在appliaction中
            //通过反射得到bltSocket对象，与uuid进行连接得到的结果一样，但这里不提倡用反射的方法
            //mBluetoothSocket = (BluetoothSocket) btDev.getClass().getMethod("createRfcommSocket", new Class[]{int.class}).invoke(btDev, 1);
            //在建立之前调用
                if (adapter?.isDiscovering!!)
                //停止搜索
                    adapter?.cancelDiscovery()
            //如果当前socket处于非连接状态则调用连接
            if (!mBluetoothSocket?.isConnected!!) {
                //你应当确保在调用connect()时设备没有执行搜索设备的操作。
                // 如果搜索设备也在同时进行，那么将会显著地降低连接速率，并很大程度上会连接失败。
                mBluetoothSocket?.connect()
            }
            Log.d("blueTooth", "已经链接")
            if (handler == null) return
            //结果回调
            val message = Message()
            message.what = 4
            message.obj = btDev
            handler.sendMessage(message)
        } catch (e: Exception) {
            Log.e("blueTooth", "...链接失败")
            try {
                mBluetoothSocket?.close()
            } catch (e1: IOException) {
                e1.printStackTrace()
            }
            e.printStackTrace()
        }

    }

    fun connect(address: String): Boolean {
        val device:BluetoothDevice?= adapter?.getRemoteDevice(address)
        if (device == null) {
            connectDevice(device)
        }
        return false
    }

    fun connectDevice(device: BluetoothDevice?): Boolean {
        mBluetoothGatt = device?.connectGatt(activity, false, mGattCallback)
        return true
    }

    private val mGattCallback = @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2) object : BluetoothGattCallback() {
        override fun onPhyUpdate(gatt: BluetoothGatt, txPhy: Int, rxPhy: Int, status: Int) {
            super.onPhyUpdate(gatt, txPhy, rxPhy, status)
        }

        override fun onPhyRead(gatt: BluetoothGatt, txPhy: Int, rxPhy: Int, status: Int) {
            super.onPhyRead(gatt, txPhy, rxPhy, status)
        }

        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            super.onServicesDiscovered(gatt, status)
        }

        override fun onCharacteristicRead(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {
            super.onCharacteristicRead(gatt, characteristic, status)
        }

        override fun onCharacteristicWrite(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {
            super.onCharacteristicWrite(gatt, characteristic, status)
        }

        override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
            super.onCharacteristicChanged(gatt, characteristic)
        }

        override fun onDescriptorRead(gatt: BluetoothGatt, descriptor: BluetoothGattDescriptor, status: Int) {
            super.onDescriptorRead(gatt, descriptor, status)
        }

        override fun onDescriptorWrite(gatt: BluetoothGatt, descriptor: BluetoothGattDescriptor, status: Int) {
            super.onDescriptorWrite(gatt, descriptor, status)
        }

        override fun onReliableWriteCompleted(gatt: BluetoothGatt, status: Int) {
            super.onReliableWriteCompleted(gatt, status)
        }

        override fun onReadRemoteRssi(gatt: BluetoothGatt, rssi: Int, status: Int) {
            super.onReadRemoteRssi(gatt, rssi, status)
        }

        override fun onMtuChanged(gatt: BluetoothGatt, mtu: Int, status: Int) {
            super.onMtuChanged(gatt, mtu, status)
        }
    }

    fun disconnect(device: BluetoothDevice) {

        val socket = connectedSockets[connectedDevices.indexOf(device)]

        if (socket.isConnected) {
            Log.e("mBluetoothSocket", "...正在断开")
            try {
                socket.close()
            } catch (e: IOException) {
                Log.e("mBluetoothSocket", "...断开出错")
                e.printStackTrace()
            }

        }
    }

    internal var connectedSockets = ArrayList<BluetoothSocket>()
    internal var connectedDevices = ArrayList<BluetoothDevice>()

    fun refeshTitle(device: BluetoothDevice) {
        val position = connectedDevices.indexOf(device)
        val tab = v?.tab_layout?.getTabAt(position + 3)
        val tab_view = LayoutInflater.from(activity).inflate(R.layout.title_connected_device, null)
        (tab_view.findViewById<View>(R.id.name) as TextView).text = device.name
        (tab_view.findViewById<View>(R.id.address) as TextView).text = device.address
        tab_view.findViewById<View>(R.id.iv_close).setOnClickListener { disconnect(device) }
        tab?.setCustomView(tab_view)
    }

    override fun onResume() {
        if (adapter?.isEnabled!!) {

        } else {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            enableBtIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            activity?.startActivity(enableBtIntent)
            //            T.show(getActivity(), "请打开蓝牙");
            //            adapter.enable();
        }
        super.onResume()
    }

    internal var scan: MenuItem? = null


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.bluetooth, menu)
        scan = menu?.findItem(R.id.ble_scan)
        super.onCreateOptionsMenu(menu, inflater)
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP) override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.ble_scan -> when (scan?.title.toString()) {
                "ic_scan" -> found?.scanDevices()
                "stop scanning" -> found?.stopScanning()
                "disconnect" -> {
                }
            } //                        adapter.cancelDiscovery();
            R.id.ble_refresh -> {
            }
            R.id.ble_rssi -> {
            }
        } //                mRSSIView.setVisibility(View.VISIBLE);
        return super.onOptionsItemSelected(item)
    }

    override fun onBack() {
        activity?.onBackPressed()
    }

    //    override fun backable(): Boolean {
    //        return false
    //    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 3) { //REQUEST_COARSE_LOCATION
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //                T.show(getActivity(), "现在已经获取扫描权限啦");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onDestroy() {
        activity?.unregisterReceiver(connectReceiver)
        super.onDestroy()
    }
}
