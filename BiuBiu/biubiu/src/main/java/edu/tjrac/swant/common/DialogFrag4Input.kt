package edu.tjrac.swant.common

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import edu.tjrac.swant.biubiu.R

/**
 * Created by wpc on 2020-03-20.
 */
@SuppressLint("ValidFragment")
class DialogFrag4Input(
        val title: String? = "",
        val hint: String? = "",
        val content:String?="",
        val inputType: Int? = InputType.TYPE_CLASS_NUMBER
) : DialogFragment(), View.OnClickListener {

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_close -> {
                dialog?.dismiss()
            }
        }
    }

    var pos: View.OnClickListener? = null

    var v: View? = null
    var et: EditText? = null


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var dialog = Dialog(activity!!, R.style.custom_dialog_style)
        v = LayoutInflater.from(activity!!).inflate(R.layout.dialog_input, null)
        dialog?.setContentView(v!!)
        var tv_title = v?.findViewById<TextView>(R.id.tv_title)
        et = v?.findViewById(R.id.et_content)
        if(null!=inputType){
            et?.inputType = inputType!!
        }
        if (!TextUtils.isEmpty(title)) {
            tv_title?.text = title
        }
        if (!TextUtils.isEmpty(hint)) {
            et?.hint = hint
        }
        if(!TextUtils.isEmpty(content)){
            et?.setText(content)
        }
        v?.findViewById<ImageView>(R.id.iv_close)?.setOnClickListener(this)
        if(null!=pos){
            v?.findViewById<Button>(R.id.bt_ensure)?.setOnClickListener(pos)
        }
        return dialog
    }

    fun getInput(): String? {
        return et?.text?.toString()
    }
}
