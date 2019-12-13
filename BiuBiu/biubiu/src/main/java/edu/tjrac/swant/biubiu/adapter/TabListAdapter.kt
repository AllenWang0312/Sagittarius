package edu.tjrac.swant.biubiu.adapter

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import edu.tjrac.swant.baselib.util.SUtil
import edu.tjrac.swant.biubiu.R
import edu.tjrac.swant.biubiu.bean.Tab

/**
 * Created by wpc on 2019-11-27.
 */
class TabListAdapter(layoutResId: Int, data: List<Tab>?) : BaseQuickAdapter<Tab, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder, item: Tab) {
        var et = helper.getView<EditText>(R.id.et_name)

        et.hint = item.name
        if (!SUtil.isEmpty(item.alias)) {
            et.setText(item.alias)
        }

        et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                item.alias = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })

    }
}
