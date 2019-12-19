package edu.tjrac.swant.filesystem

//package color.measurement.com.from_cp20.manager.file;

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View

import edu.tjrac.swant.baselib.common.interfaze.OnItemClickListener
import edu.tjrac.swant.baselib.util.IntentUtil

import java.io.File
import java.util.ArrayList

import edu.tjrac.swant.filesystem.adapter.FileDirAdapter
import edu.tjrac.swant.filesystem.adapter.FileExploreAdapter
import edu.tjrac.swant.wjzx.R

/**
 * Created by wpc on 2017/6/14.
 */
@SuppressLint("NewApi", "ValidFragment")
class FileExploreDialog(root: String) : DialogFragment() {
    internal var mContext: Context? = null

    internal var rote= ArrayList<String>()
    internal var mRvPathIndicateView: RecyclerView?=null
    internal var mRvFileView: RecyclerView?=null
    internal var dirAdapter: FileDirAdapter?=null
    internal var adapter: FileExploreAdapter?=null
    private val mOnItemClick = OnItemClickListener { view, position ->
        val path = adapter?.path
        val childs = adapter?.childs
        if (File(path, childs?.get(position)).isDirectory) {
            rote.add(childs?.get(position)!!)
            refeshFileView(rote!!)
        } else {
            startActivity(IntentUtil.openFileIntent(File(path, childs?.get(position))))
            //                OpenFileHelper.openFile(mContext, new File(path, childs[position]));
        }
    }

    init {
        rote = ArrayList()
        rote.add(root)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        mContext = activity
        val builder = AlertDialog.Builder(mContext)
        builder.setTitle("文件管理")
        val v = LayoutInflater.from(mContext).inflate(R.layout.dialog_fileexplore, null)
        mRvPathIndicateView = v.findViewById<View>(R.id.rv_path_indicate_view) as RecyclerView
        mRvFileView = v.findViewById<View>(R.id.rv_file_view) as RecyclerView

        mRvPathIndicateView?.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        mRvFileView?.layoutManager = GridLayoutManager(mContext, 4)
        refeshFileView(rote)
        refeshDirView()
        builder.setView(v)
        return builder.create()
    }

    private fun refeshDirView() {
        dirAdapter = FileDirAdapter(rote, mContext)
        dirAdapter?.setOnItemClickListener { view, position ->
            val size = rote.size
            for (i in position + 1 until size) {
                rote.removeAt(i)
            }
            refeshFileView(rote)
            refeshDirView()
        }
        mRvPathIndicateView?.adapter = dirAdapter
    }

    internal fun refeshFileView(rote: ArrayList<String>) {
        var path = ""
        for (str in rote) {
            path += "/$str"
        }
        adapter = FileExploreAdapter(mContext, path, false)
        adapter?.setOnItemClick(mOnItemClick)
        //        adapter.setOnItemLongClickListener(mOnItemLongClickListener);
        mRvFileView?.adapter = adapter
    }
    //    private OnItemLongClickListener mOnItemLongClickListener = new OnItemLongClickListener() {
    //        @Override
    //        public void onItemLongClick(int positon) {
    //            Intent share = new Intent(Intent.ACTION_SEND);
    //            ComponentName component = new ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity");
    //            share.setComponent(component);
    //            File file = new File(adapter.getPath(), adapter.getChilds()[positon]);
    //            System.out.println("file " + file.exists());
    //            share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
    //            share.setType("*/*");
    //            startActivity(Intent.createChooser(share, "发送"));
    //        }
    //    };


}
