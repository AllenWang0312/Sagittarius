package edu.tjrac.swant.bluetooth.view

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.bluetooth.le.ScanResult
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.DialogFragment
import android.util.SparseArray
import android.view.LayoutInflater

import edu.tjrac.swant.baselib.util.L
import edu.tjrac.swant.wjzx.R


/**
 * Created by wpc on 2018/2/1 0001.
 */

@SuppressLint("ValidFragment")
class RawDataDialog(
        //    @BindView(R.id.et_raw_data) EditText et_raw_data;
        //    @BindView(R.id.tab_row_2) TableRow tab_row_2;
        //    @BindView(R.id.tab_row_3) TableRow tab_row_3;
        //    @BindView(R.id.tab_row_4) TableRow tab_row_4;
        //    @BindView(R.id.tv_info) TextView tv_info;

        private val result: ScanResult) : DialogFragment() {
    internal var TAG = "RawDataDialog"
    private var mManufacturerSpecificData: SparseArray<ByteArray>? = null

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        L.i(TAG, result.toString())
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("RAW DATA")
        val dialog = LayoutInflater.from(activity).inflate(R.layout.fragment_dialog_rawdata, null)
        mManufacturerSpecificData = result.scanRecord!!.manufacturerSpecificData
        result.scanRecord!!.serviceData

        //        L.i("specificData",)
        //        et_raw_data.setText();

        builder.setView(dialog)
        builder.setPositiveButton("ok", null)
        return builder.create()
    }


    override fun onDestroyView() {
        super.onDestroyView()
    }
}
