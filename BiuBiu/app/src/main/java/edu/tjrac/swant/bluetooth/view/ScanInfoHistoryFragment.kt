package edu.tjrac.swant.bluetooth.view


import android.annotation.SuppressLint
import android.bluetooth.le.ScanResult
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.text.SpannableStringBuilder
import android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import edu.tjrac.swant.bluetooth.BlueToothHelper
import edu.tjrac.swant.bluetooth.BlueToothHelper.keySet
import edu.tjrac.swant.bluetooth.adapter.ScanInfoHistoryAdapter
import edu.tjrac.swant.bluetooth.bean.ScanInfo
import edu.tjrac.swant.unicorn.BaseChartView
import edu.tjrac.swant.wjzx.R
import kotlinx.android.synthetic.main.scan_info_history.*
import java.util.*

/**
 * Created by wpc on 2018/2/2 0002.
 */

@SuppressLint("ValidFragment")
class ScanInfoHistoryFragment internal constructor(private val result: ScanResult, internal var infos: ArrayList<ScanInfo>) : Fragment() {
    internal var chartValues: ArrayList<BaseChartView.ChartValue>?=null
    internal var adapter: ScanInfoHistoryAdapter?=null


    @SuppressLint("ResourceAsColor")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.scan_info_history, container, false)
        //        mRssiView.setTitles(32,"TIME(s)","RSSI(dBm)",getActivity().getResources().getColor(R.color.blue));
        return v
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        chartValues = getChartValues(infos)

        rssi_view.setStyle(BaseChartView.StyleBuilder()
                .line(true)
                .dashPath(true)
                .setXInfo(10f, 0f, 10, "TIME(s)", activity!!.resources.getColor(R.color.blue))
                .setYInfo(-100f, 0f, 5, "RSSI(dBm)", activity!!.resources.getColor(R.color.blue))
                .addLine(activity!!.resources.getColor(R.color.blue),
                        chartValues!!)
        )
//        L.i("chartValue", chartValues!!.size.toString() + "")
        recycler.setLayoutManager(LinearLayoutManager(activity))
        adapter = ScanInfoHistoryAdapter(infos)

        adapter!!.bindToRecyclerView(recycler)
        val headview = LayoutInflater.from(activity).inflate(R.layout.head_scan_infos, null)
        initHeadView(headview)
        adapter!!.addHeaderView(headview)
        recycler.setAdapter(adapter)
        super.onViewCreated(view, savedInstanceState)
    }
    internal var values: Array<String?>?=null

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun initHeadView(headview: View) {
        values = arrayOfNulls<String>(4)
        values!![0] = BlueToothHelper.getType(result.device!!.type)
        val flags = BlueToothHelper.getFlags(result.scanRecord!!.advertiseFlags)

        values!![1] = BlueToothHelper.getFlags(flags)
        values!![2] = BlueToothHelper.getStringUUIDs(result.scanRecord!!.serviceUuids)
        values!![3] = result.device.name
        val ll = headview.findViewById<View>(R.id.infos) as LinearLayout
        for (i in keySet?.indices!!) {
            val span = SpannableStringBuilder(keySet?.get(i) + values!![i])
            span.setSpan(ForegroundColorSpan(activity!!.resources.getColor(R.color.blue)),
                    0, keySet?.get(i)?.length!!, SPAN_INCLUSIVE_INCLUSIVE)
            val textView = TextView(activity)
            textView.text = span
            ll.addView(textView)
        }
        (headview.findViewById<View>(R.id.div_time) as TextView).text = infos[0].advTime.toString() + "ms"
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun getChartValues(infos: ArrayList<ScanInfo>): ArrayList<BaseChartView.ChartValue> {
        val base = infos[0].tagTime
        val values = ArrayList<BaseChartView.ChartValue>()
        for (info in infos) {
            values.add(BaseChartView.ChartValue(((base - info.tagTime) / 1000).toFloat(), info.dbm.toFloat()))
        }
        return values
    }
}
