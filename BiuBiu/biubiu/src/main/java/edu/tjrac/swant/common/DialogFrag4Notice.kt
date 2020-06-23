package edu.tjrac.swant.common

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import edu.tjrac.swant.biubiu.R

/**
 * Created by wpc on 2020-03-20.
 */
@SuppressLint("ValidFragment")
class DialogFrag4Notice(
        val title: String? = "",
        val message: String? = ""
) : DialogFragment(), View.OnClickListener {

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.close -> {
                dialog?.dismiss()
            }
        }
    }

    var pos: View.OnClickListener? = null

    var v: View? = null
    var content: TextView? = null


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var dialog = Dialog(activity!!, R.style.custom_dialog_style)
        v = LayoutInflater.from(activity!!).inflate(R.layout.dialog_notice, null)
        dialog?.setContentView(v!!)
        var tv_title = v?.findViewById<TextView>(R.id.tv_title)
        content = v?.findViewById(R.id.tv_content)
        if (!TextUtils.isEmpty(title)) {
            tv_title?.text = title
        }
        if (!TextUtils.isEmpty(message)) {
            content?.text = message!!
        }
        v?.findViewById<View>(R.id.close)?.setOnClickListener(this)
        if (null != pos)
            v?.findViewById<View>(R.id.ensure)?.setOnClickListener(pos)
        return dialog
    }

    fun getContent(): String? {
        return content?.text?.toString()
    }
}
