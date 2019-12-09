package edu.tjrac.swant.bluetooth.view

//import kotlinx.android.synthetic.main.found_devices_filters.*
import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.*
import android.content.*
import android.content.Context.MODE_PRIVATE
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.PopupMenu
import android.widget.PopupWindow
import edu.tjrac.swant.baselib.common.base.BaseFragment
import edu.tjrac.swant.baselib.common.recycler.BaseDecoration
import edu.tjrac.swant.bluetooth.BlueToothHelper
import edu.tjrac.swant.bluetooth.adapter.ScanResultRecycAdapter
import edu.tjrac.swant.bluetooth.bean.ScanInfo
import edu.tjrac.swant.wjzx.R
import kotlinx.android.synthetic.main.found_swiper_recycler.*
import java.util.*

/**
 * Created by wpc on 2018/1/31 0031.
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
@SuppressLint("ValidFragment")
class FoundDevicesFragment(private val parent: BLEFragment,
        //    CardView found_head;
                           internal var adapter: BluetoothAdapter, title: String) : BaseFragment() {


    internal var auto_scanning = true

    internal var sp: SharedPreferences?=null
    internal var found_adapter: ScanResultRecycAdapter?=null

    internal var found_flags = ArrayList<String>()
    internal var found_devices = ArrayList<ScanResult>()
    internal var scaninfo = HashMap<String, ArrayList<ScanInfo>>()

    internal var handler = Handler()

    private val callback = object : ScanCallback() {
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            val address = result.device.address
            //            String name = result.getDevice().getName();
//            L.i("onScanResult:address",
                    //                    name + ":" +
//                    address)
//            L.i("onScanResult:detial", result.toString())
            if (found_flags.contains(address)) {
                val infos = scaninfo[address]
                infos!!.add(0, ScanInfo(System.currentTimeMillis(), result.rssi, infos.get(infos.size - 1).tagTime))
                found_devices[found_flags.indexOf(address)] = result
                found_adapter!!.notifyItemChanged(found_flags.indexOf(address))
            } else {
                scaninfo.put(address, ArrayList<ScanInfo>())
                scaninfo[address]!!.add(
                        ScanInfo(System.currentTimeMillis(), result.rssi, 0L))
                found_flags.add(result.device.address)
                found_devices.add(result)
                found_adapter!!.notifyItemInserted(found_devices.size - 1)
            }

            super.onScanResult(callbackType, result)
        }

        override fun onBatchScanResults(results: List<ScanResult>) {
//            L.i("onBatchScanResults")
            found_adapter!!.scanFinish(results)
            super.onBatchScanResults(results)
        }

        override fun onScanFailed(errorCode: Int) {
//            L.i("onScanFailed")
            super.onScanFailed(errorCode)
        }
    }
    private val BLEReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED === action) {
                swiper.setRefreshing(true)
                parent.scan!!.setTitle("stop scanning")
            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED === action) {
                found_adapter!!.notifyItemChanged(found_flags.indexOf(device.address))
            } else if (BluetoothDevice.ACTION_FOUND === action) {

                //                if (!found_devices.contains(device)) {
                //                    found_devices.add(device);
                //                    found_adapter.notifyItemInserted(found_devices.size() - 1);
                //                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED === action) {
                parent.scan!!.setTitle("ic_scan")
                swiper.setRefreshing(false)
            }//            else if (BluetoothDevice.ACTION_ACL_CONNECTED == action) {
            //                //从Intent得到blueDevice对象
            //                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            //                L.i("RSSI:"+device.getAddress(), intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI) + "");
            //                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
            //                    for (int i = 0; i < found_devices.size(); i++) {
            //                        if (found_devices.get(i).getAddress().equals(device.getAddress())) {
            //
            ////                            found_devices.get(i).setRssi(intent.getExtras().getShort(
            ////                                    BluetoothDevice.EXTRA_RSSI));
            //                            found_adapter.notifyItemChanged(i);
            //                        }
            //                    }
            //                }
            //            }
        }
    }
    internal var filterView: PopupWindow?=null

    @TargetApi(Build.VERSION_CODES.M)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.found_swiper_recycler, container, false)
        sp = activity!!.getSharedPreferences("bluetooth", MODE_PRIVATE)
        scanner = adapter.bluetoothLeScanner
//        this.title = title
        initRecycView()
        initFilterView()


        val filter = IntentFilter()
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        filter.addAction(BluetoothDevice.ACTION_FOUND)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        activity!!.registerReceiver(BLEReceiver, filter)

        return view
    }

    private fun initRecycView() {
        recycler.setLayoutManager(LinearLayoutManager(activity))
        found_adapter = ScanResultRecycAdapter(sp!!, found_devices)
        recycler.addItemDecoration(BaseDecoration(activity!!, LinearLayoutManager.VERTICAL))

        found_adapter!!.setOnItemClickListener { adapter, view, position ->
            if (found_adapter!!.expendIndex == position) {
                found_adapter!!.setExpendItem(-1)
            } else {
                found_adapter!!.setExpendItem(position)
            }
        }
        found_adapter!!.setOnItemChildClickListener { ad, view, position ->
            val device = found_devices[position].device
            val address = device.address
            if (view.id == R.id.iv_options) {
                val popupMenu = PopupMenu(activity, view)
                //将指定的菜单布局进行加载
                activity!!.menuInflater.inflate(R.menu.connect, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener { item ->
                    if (item.itemId == R.id.connect_phy) {

                    } else if (item.itemId == R.id.connect_bond) {
                        //                                found_devices.get(position).getDevice().createBond();
                        BlueToothHelper.createBond(found_devices[position].device)
                    }
                    false
                }//给菜单绑定监听
                //展示菜单
                popupMenu.show()
            } else if (view.id == R.id.bt_raw) {
                RawDataDialog(found_devices[position]).show(fragmentManager!!, "RawData")
            } else if (view.id == R.id.iv_icon) {
                //                    Map<String, ?> configs = sp.getAll();
                //                    L.i("keysets",configs.keySet().toString());

                if (sp!!.getBoolean(address, false) == true) {
                    sp!!.edit().putBoolean(address, false).commit()
                } else {
                    sp!!.edit().putBoolean(address, true).commit()
                }

                found_adapter!!.notifyItemChanged(position)
//                L.i("favourite", sp!!.getBoolean(address, false).toString())
            } else if (view.id == R.id.bt_more) {
                stopScanning()
                DevicesMoreInfoActivity.start(activity!!, found_devices[position], scaninfo!![address]!!)
            } else if (view.id == R.id.tv_connect) {
                parent.connect(device)
            }
        }
        swiper.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener { scanDevices() })
        found_adapter!!.bindToRecyclerView(recycler)
        recycler.setAdapter(found_adapter)
    }

    private fun initFilterView() {
        val filter_view = LayoutInflater.from(activity).inflate(R.layout.found_filter_view, null)
        filter_view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        filterView = PopupWindow(activity)
        filterView!!.isFocusable = true
        filterView!!.width = ViewGroup.LayoutParams.MATCH_PARENT
        //        filterView.setWindowLayoutType(ViewGroup.LayoutParams.MATCH_PARENT);
        filterView!!.isOutsideTouchable = true
        filterView!!.setBackgroundDrawable(activity!!.resources.getDrawable(R.drawable.card))

        filterView!!.elevation = 4f
        filterView!!.contentView = filter_view
        filterView!!.setOnDismissListener { cb_filter.setChecked(false) }
        cb_filter?.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                filterView!!.showAsDropDown(cb_filter)
                changeWindowAlfa(0.8f)

            } else {
                filterView!!.dismiss()
                changeWindowAlfa(1f)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        scanDevices()
        super.onViewCreated(view, savedInstanceState)
    }

    /*
                更改屏幕窗口透明度
             */
    internal fun changeWindowAlfa(alfa: Float) {
        val params = activity!!.window.attributes
        params.alpha = alfa
        activity!!.window.attributes = params
    }

    internal var scan_timeout = 12
    internal var scanner: BluetoothLeScanner?=null
    internal var filter: List<ScanFilter>? = null
    internal var setting: ScanSettings? = null

    @SuppressLint("MissingPermission")
    fun scanDevices() {
        if (adapter.isEnabled) {
            if (!swiper.isRefreshing) {
                swiper.isRefreshing = true
            }
//            L.i("start Scan")
            if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity!!,
                        arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                       3)
            } else {
                //发现设备 方法一
                //                adapter.startDiscovery();
                //方法二
                scanner = adapter.bluetoothLeScanner
                parent.scan!!.setTitle("stop scanning")
                if (filter == null) {
                    scanner!!.startScan(callback)
                } else {
                    scanner!!.startScan(filter!!, setting!!, callback!!)
                }
                //                handler.postDelayed(new Runnable() {
                //                    @Override
                //                    public void run() {
                //                        found_swiper.setRefreshing(false);
                //                        parent.ic_scan.setTitle("ic_scan");
                //                        scanner.stopScan(callback);
                //                    }
                //                }, 1000 * scan_timeout);

                //方法三
                //                        adapter.startLeScan(callback);
            }
        } else {
//            L.i("adapter is unable")
            //                    T.show(getActivity(), "请打开蓝牙");
            adapter.enable()
        }
    }

    fun stopScanning() {
        swiper.setRefreshing(false)
        scanner!!.stopScan(callback)
        parent.scan!!.setTitle("ic_scan")
    }

    override fun onDestroy() {
        activity!!.unregisterReceiver(BLEReceiver)
        super.onDestroy()
    }

    override fun onBack() {

    }

//    override fun backable(): Boolean {
//        return false
//    }
}
