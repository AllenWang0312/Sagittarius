package edu.tjrac.swant.bluetooth.view

import android.annotation.SuppressLint
import android.bluetooth.le.ScanResult
import android.os.Bundle
import android.os.ParcelUuid
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import edu.tjrac.swant.wjzx.R
import kotlinx.android.synthetic.main.fragment_flags_service.*

/**
 * Created by wpc on 2018/2/2 0002.
 */

//@SuppressLint("ValidFragment")
@SuppressLint("MissingPermission", "ValidFragment","NewApi")
//@SuppressLint("NewApi")
class ScanInfoFlagsAndServicesFragment(scanResult: ScanResult) : Fragment() {

    private val mUuids: Array<ParcelUuid>?
    private val mAdvertiseFlags: Int

 init {
        mUuids = scanResult.device?.uuids
        mAdvertiseFlags = scanResult.scanRecord!!.advertiseFlags
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_flags_service, container, false)

        flag_view.setFlag(mAdvertiseFlags)

        if (mUuids != null && mUuids.size > 0) {
            for (uuid in mUuids) {
                val textView = TextView(activity)
                textView.text = uuid.toString()
                ll_services.addView(textView)
            }
        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}
