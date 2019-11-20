package edu.tjrac.swant.filesystem

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.Log
import android.view.LayoutInflater
import com.google.gson.Gson
import edu.tjrac.swant.baselib.common.base.BaseApplication
import edu.tjrac.swant.baselib.util.StringUtils
import edu.tjrac.swant.filesystem.Config.SP.CARRAY_JSON
import edu.tjrac.swant.wjzx.R
import kotlinx.android.synthetic.main.dialog_carry_path.*
import java.io.File
import java.util.*

/**
 * Created by wpc on 2018-08-24.
 */

@SuppressLint("ValidFragment")
class CarryPathDialogFragment(
        //    @BindView(R.id.et_from) TextInputEditText et_from;
        //    @BindView(R.id.et_to) TextInputEditText et_to;

        internal var from: String, internal var to: String) : DialogFragment() {

    init {
        initCarrySetting()
    }

    inner class CarrySetting {
        internal var maps: ArrayList<CarryInfo>? = null

        fun fromContain(absolutePath: String): Boolean {
            if (maps != null && maps!!.size > 0) {
                for (item in maps!!) {
                    if (item.from == absolutePath) {
                        return true
                    }
                }
            }
            return false
        }

        fun toContain(absolutePath: String): Boolean {
            if (maps != null && maps!!.size > 0) {
                for (item in maps!!) {
                    if (item.to == absolutePath) {
                        return true
                    }
                }
            }
            return false
        }

    }

    internal inner class CarryInfo(var from: String, var to: String) {
        var options: Int = 0//0 剪切
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("设置整理路径")
        val view = LayoutInflater.from(activity).inflate(R.layout.dialog_carry_path, null)
        et_from.setText(from)
        et_to.setText(to)


        builder.setView(view)
        builder.setPositiveButton("确定") { dialog, which ->
            val from = et_from.getText().toString().trim()
            val to = et_to.getText().toString().trim()
            if (StringUtils.isEmpty(from) || StringUtils.isEmpty(to)) {

            } else {
                if (from == to) {

                } else {
                    val f_from = File(from)
                    val f_to = File(to)
                    if (f_from.exists() && f_from.isDirectory() && f_to.exists() && f_to.isDirectory()) {


                        if (setting!!.maps == null) {
                            setting!!.maps = ArrayList<CarryInfo>()
                        } else {
                            //                                if (setting.fromContain(f_from.getAbsolutePath())) {
                            //                                    T.show
                            //                                }
                            setting!!.toContain(from)
                        }
                        setting!!.maps!!.add(CarryInfo(f_from.getAbsolutePath(), f_to.getAbsolutePath()))
                        sp!!.edit().putString(CARRAY_JSON, Gson().toJson(setting)).commit()

                    }
                }
            }
        }
        builder.setNegativeButton("取消", null)
        return builder.create()
    }


    override fun onDestroyView() {
        super.onDestroyView()
    }

    companion object {
        var setting: CarrySetting?=null

        var sp: SharedPreferences? = null

        fun initCarrySetting() {
            if (sp == null) {
                sp = BaseApplication.instance?.getSharedPreferences(Config.SP.setting, Context.MODE_PRIVATE)
            }
            val carry = sp!!.getString(CARRAY_JSON, "")
            Log.i(CARRAY_JSON, carry)
            setting = Gson().fromJson(carry, CarrySetting::class.java)
        }
    }


}
