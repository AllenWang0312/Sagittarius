package edu.tjrac.swant.bluetooth.view

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import edu.tjrac.swant.baselib.common.adapter.V4FragmentsPagerAdapter
import edu.tjrac.swant.baselib.util.StringUtils
import edu.tjrac.swant.bluetooth.bean.ScanInfo
import edu.tjrac.swant.wjzx.R
import kotlinx.android.synthetic.main.activity_devices_more_info.*

import java.util.*

class DevicesMoreInfoActivity : AppCompatActivity() {

    internal var scanResult: ScanResult?=null
    internal var device: BluetoothDevice?=null
    internal var infos: ArrayList<ScanInfo>?=null
    internal var adapter: V4FragmentsPagerAdapter?=null

    private var history: ScanInfoHistoryFragment? = null
    private var flags: ScanInfoFlagsAndServicesFragment? = null


    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_devices_more_info)
        setSupportActionBar(toolbar)
        val intent = intent
        scanResult = intent.getParcelableExtra<ScanResult>("scanResult")
        device = scanResult!!.device!!
        infos = intent.getParcelableArrayListExtra<ScanInfo>("infos")

        toolbar.setNavigationIcon(R.drawable.ic_close_grey_600_24dp)
        titleColor = resources.getColor(R.color.black)
        if (StringUtils.isEmpty(device!!.name!!)) {
            title = device!!.name
        } else {
            title = "N/A"
        }

        adapter = V4FragmentsPagerAdapter(supportFragmentManager)
        history = ScanInfoHistoryFragment(scanResult!!, infos!!)
        flags = ScanInfoFlagsAndServicesFragment(scanResult!!)
        adapter!!.addFragment(history!!, "history")
        adapter!!.addFragment(flags!!, "flags")
        vp.setAdapter(adapter)
        tablayout.setupWithViewPager(vp)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        MenuInflater(this@DevicesMoreInfoActivity)
                .inflate(R.menu.devices_more_info, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.info) {

        }
        return super.onOptionsItemSelected(item)
    }

    companion object {

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        fun start(context: Context, scanResult: ScanResult, scanInfos: ArrayList<ScanInfo>) {
            val intent = Intent(context, DevicesMoreInfoActivity::class.java)
            intent.putExtra("scanResult", scanResult!!)
            intent.putParcelableArrayListExtra("infos", scanInfos)
            context.startActivity(intent)
        }
    }
}
