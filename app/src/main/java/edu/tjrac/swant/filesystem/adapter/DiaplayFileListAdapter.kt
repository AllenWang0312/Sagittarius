package edu.tjrac.swant.filesystem.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import edu.tjrac.swant.baselib.util.FileUtils
import edu.tjrac.swant.wjzx.R
import java.io.File

/**
 * Created by wpc on 2016/11/17.
 */

public class DiaplayFileListAdapter(data: ArrayList<File>?) :

    BaseQuickAdapter<File, BaseViewHolder>(R.layout.chose_file_item, data) {

    override fun convert(helper: BaseViewHolder, item: File) {

        helper.setText(R.id.filename_chosefiledialogitem, item.name)
        if (item.isDirectory) {
            helper.setText(R.id.dirpath_chosefiledialogitem, "" + item.listFiles().size + "个文件")
        } else {
            helper.setText(R.id.dirpath_chosefiledialogitem, FileUtils.getAutoFileOrFilesSize(item.absolutePath))
        }

    }
}
