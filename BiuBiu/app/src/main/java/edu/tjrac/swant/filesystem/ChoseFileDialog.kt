package edu.tjrac.swant.filesystem

import android.annotation.SuppressLint
import android.app.AlertDialog.Builder
import android.app.Dialog
import android.app.DialogFragment
import android.content.DialogInterface
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import edu.tjrac.swant.baselib.util.FileUtils

import edu.tjrac.swant.baselib.util.SUtil
import edu.tjrac.swant.baselib.util.T
import edu.tjrac.swant.filesystem.adapter.DiaplayFileListAdapter
import edu.tjrac.swant.filesystem.bean.RePushTaskInfo
import edu.tjrac.swant.filesystem.bean.TaskGroup
import edu.tjrac.swant.wjzx.R
import kotlinx.android.synthetic.main.chosefiledialog_layout.view.*
import java.io.File
import java.util.*


class ChoseFileDialog() : DialogFragment(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> {
                if (dir?.path.equals(FileUtils.getSDcardPath())) {
                    T.show("已经是根目录了")
                } else {
                    enter(dir?.parentFile!!)
                }
            }
        }
    }

    var query: String? = null
        set(value) {
            field = value
            if (!SUtil.isEmpty(value)) {
                var filter = mData?.filter {
                    it.name.toString().contains(value!!)
                }
                mData?.clear()
                mData?.addAll(filter!!)
                adapter?.notifyDataSetChanged()
            }
        }

    var dirpath: String? = null
        set(value) {
            var dir = File(value)
            if (dir.exists() && dir.isDirectory) {
                field = value
                this.dir = dir
            } else {
                field = value?.substring(0, value?.lastIndexOf("/")!!)
                this.dir = File(field)
                query = value?.substring(value?.lastIndexOf("/") + 1, value.length)

            }
        }
    var dir: File? = null
        set(value) {
            field = value
            mData?.addAll(field?.listFiles()!!)
            adapter?.notifyDataSetChanged()
        }

    var mData: ArrayList<File>? = ArrayList()


    var pos: DialogInterface.OnClickListener? = null
//    constructor(mData: ArrayList<File>) {
//        this.mData = mData
//        dir = mData.get(0).parentFile
//    }

    @SuppressLint("ValidFragment")
    constructor(dirpath: String, pos: DialogInterface.OnClickListener) : this() {
        this.dirpath = dirpath
        this.pos = pos
    }
    //	FileUtils.showChoseFileToPlayDialog(FileUtils.FILE_PATH, ".ppt",
    //	SoftUpdateActivity.this);

    internal var rv: RecyclerView? = null
    var adapter: DiaplayFileListAdapter? = null
    lateinit var v: View

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        //3 在此处设置 无标题 对话框背景色
        //getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        // //对话框背景色
        //getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.RED));
        //getDialog().getWindow().setDimAmount(0.5f);//背景黑暗度

        //不能在此处设置style
        // setStyle(DialogFragment.STYLE_NORMAL,R.style.Mdialog);//在此处设置主题样式不起作用

        val builder = Builder(activity)
        builder.setTitle("选择文件")

        v = LayoutInflater.from(activity).inflate(R.layout.chosefiledialog_layout, null)
        rv = v.findViewById(R.id.rv_chosefiledialog) as RecyclerView
        v.iv_back?.setOnClickListener(this)
        v.et_path?.setText(dir?.path)
        v.et_path?.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                dirpath=s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })

        rv?.layoutManager = LinearLayoutManager(activity!!)
        adapter = DiaplayFileListAdapter(mData)

        adapter?.onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { adapter, view, position ->
            var item = mData?.get(position) as TaskGroup
            item.tasks?.add(RePushTaskInfo())
            adapter?.notifyItemChanged(position)
        }
        adapter?.onItemClickListener =
            BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
                var item = adapter?.getItem(position)!! as File
                if(item.isDirectory){
                    enter(item)
                }

            }
        rv?.adapter = adapter

        builder.setView(v)
        builder.setCancelable(false)
        builder.setPositiveButton("确定", pos)
        builder.setNegativeButton("取消", null)
        return builder.create()
    }

    fun enter(file: File) {
        dir = file
        mData?.clear()
        mData?.addAll(file.listFiles())
        adapter?.notifyDataSetChanged()
        v?.et_path?.setText(dir?.path)
    }

    public fun getPath(): String {
        return dir?.absolutePath!!
    }
    //    interface onRefeshListener{
    //        void onRefesh();
    //    }

}
