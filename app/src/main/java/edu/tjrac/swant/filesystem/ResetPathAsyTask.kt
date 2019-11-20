package edu.tjrac.swant.filesystem

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.AsyncTask
import android.view.LayoutInflater
import android.widget.TextView
import edu.tjrac.swant.baselib.util.FileUtils
import edu.tjrac.swant.baselib.util.Phone
import edu.tjrac.swant.baselib.util.StringUtils
import edu.tjrac.swant.wjzx.R
import java.io.File

/**
 * Created by wpc on 2019-07-19.
 */
class ResetPathAsyTask : AsyncTask<String, String, Int> {
    var mContext: Context? = null
    var map: HashMap<String, String>? = null
    var dialog: Dialog? = null

    constructor(context: Context, configs: List<String>) {
        mContext = context
        map = HashMap()
        for (i in configs) {
            if(!StringUtils.isEmpty(i.trim())){
                var items = i.split("->")
                map?.put(items[0].trim(), items[1].trim())
            }
        }
    }

    var message: TextView? = null

    override fun onPreExecute() {
        super.onPreExecute()

        dialog = Dialog(mContext!!)
        var view = LayoutInflater.from(mContext).inflate(R.layout.progress_reset_path_task_dialog, null)
        message=view.findViewById(R.id.tv_message)
        dialog?.setContentView(view)
        dialog?.setCancelable(false)
        dialog?.show()
    }

    @SuppressLint("WrongThread")
    override fun doInBackground(vararg params: String?): Int {
        for (i in map?.keys!!) {
            if(i.contains("#")) continue

            var from = File(Phone.SDCardPath + i)
            if(from.exists()&&from.isDirectory){
                var to = File(Phone.SDCardPath + map?.get(i))
                if (!to.exists()) {
                    to.mkdirs()
                }
                var childs=from.listFiles()
                if(null!=childs&&childs.isNotEmpty()){
                    for (i in childs) {
                        var end = FileUtils.extensionName(i.name)
                        if (params.contains(end)) {
                            FileUtils.moveFile(i.absolutePath, to.absolutePath)
                            onProgressUpdate(i.absolutePath, to.absolutePath)
                        }
                    }
                }
            }

        }
        return 0
    }

    override fun onPostExecute(result: Int?) {
        super.onPostExecute(result)
        dialog?.dismiss()
    }

    override fun onProgressUpdate(vararg values: String?) {
        super.onProgressUpdate(*values)
        message?.post {
            message?.text = values[0]+"->"+values[1]
        }
    }

}