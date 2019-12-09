package edu.tjrac.swant.filesystem.adapter

import android.app.Activity
import android.content.DialogInterface
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.LinearLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import edu.tjrac.swant.baselib.util.Phone
import edu.tjrac.swant.filesystem.ChoseFileDialog
import edu.tjrac.swant.filesystem.bean.TaskGroup
import edu.tjrac.swant.wjzx.R
import kotlinx.android.synthetic.main.item_sub_repush_task.view.*

class RePushTasksAdapter(data: MutableList<TaskGroup>?) :
    BaseQuickAdapter<TaskGroup, BaseViewHolder>(R.layout.item_repush_task, data) {

    override fun convert(helper: BaseViewHolder?, item: TaskGroup?) {
        helper?.addOnClickListener(R.id.iv_add)
        helper?.setText(R.id.tv_name, item?.name)
        var ll_childs = helper?.getView<LinearLayout>(R.id.ll_childs)
        if (null != item?.tasks && item?.tasks?.size!! > 0) {
            helper?.setVisible(R.id.ll_childs, true)
            for (i in 0 until item?.tasks?.size!!) {
                var child = mLayoutInflater.inflate(R.layout.item_sub_repush_task, null)
                child.et_from.setText(item.tasks?.get(i)?.fromPath)
                child.et_to.setText(item.tasks?.get(i)?.toPath)
                child.et_from.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    }

                    override fun afterTextChanged(s: Editable?) {
                        item.tasks?.get(i)?.fromPath = s.toString()
                    }
                })

                child.et_to.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    }

                    override fun afterTextChanged(s: Editable?) {
                        item.tasks?.get(i)?.toPath = s.toString()
                    }
                })
                child.et_to.setOnFocusChangeListener { v, hasFocus ->
                    showChoseFileDialog(v as EditText)
                }
                child.et_from.setOnFocusChangeListener { v, hasFocus ->
                    showChoseFileDialog(v as EditText)
                }
                ll_childs?.addView(child)
            }
        } else {
            helper?.setVisible(R.id.ll_childs, false)
        }
    }

    var dialog: ChoseFileDialog? = null
    private fun showChoseFileDialog(edit: EditText) {
        var dialog = ChoseFileDialog(Phone.SDCardPath!!,
            DialogInterface.OnClickListener { dia, which -> edit.setText(dialog?.getPath()) })
        dialog?.show((mContext as Activity).fragmentManager, "chose_file")
    }

}