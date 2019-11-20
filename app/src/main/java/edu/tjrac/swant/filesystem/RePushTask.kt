package edu.tjrac.swant.filesystem

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.AsyncTask
import edu.tjrac.swant.baselib.common.base.BaseActivity
import edu.tjrac.swant.baselib.util.FileUtils
import edu.tjrac.swant.filesystem.bean.RePushTaskInfo
import java.io.File

class RePushTask : AsyncTask<Int, Int, Int> {
    var mContex: Context? = null
    var tasks: ArrayList<RePushTaskInfo>? = null

    constructor(context: Context, tasks: ArrayList<RePushTaskInfo>) {
        this.mContex = context
        this.tasks = tasks
    }

    var count: Int? = null
        get() {
            if (field == null)
                field = count()
            return field
        }

    fun count(): Int {
        var count = 0
        for (i in tasks!!) {
            var dir = File(i.fromPath)
            if (dir.exists() && dir.isDirectory) {
                count += dir.listFiles().size
            }
        }
        return count
    }


    override fun onPreExecute() {
        super.onPreExecute()
        (mContex as BaseActivity).showProgressDialog("任务执行中,请稍后")
    }

    @SuppressLint("WrongThread")
    override fun doInBackground(vararg params: Int?): Int {
        var downTask = 0
        for (i in tasks!!) {
            var dir = File(i.fromPath)
            var to = File(i.toPath)
            var files = dir.listFiles()
            for (j in files) {
                FileUtils.moveFile(j.absolutePath, to.absolutePath)
                downTask = downTask + 1
                onProgressUpdate(downTask, count)
            }
        }
        return 0
    }

    override fun onProgressUpdate(vararg values: Int?) {
        super.onProgressUpdate(*values)
    }

    override fun onPostExecute(integer: Int?) {

        super.onPostExecute(integer)
        (mContex as BaseActivity).dismissProgressDialog()
    }
}
