package edu.tjrac.swant.filesystem.adapter

import android.annotation.TargetApi
import android.os.Build
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import edu.tjrac.swant.baselib.util.SUtil
import edu.tjrac.swant.filesystem.FileSystemHelper
import edu.tjrac.swant.filesystem.MediaUtil
import edu.tjrac.swant.wjzx.R
import java.io.File
import java.text.Collator
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList

/**
 * Created by wpc on 2018/1/4.
 */

class GalleryContentAdapter(
        internal var cut_paths: HashMap<String, Long>,
        internal var copy_paths: HashMap<String, Long>
) : BaseQuickAdapter<File, BaseViewHolder>(R.layout.item_gallery) {

    private val instance: GalleryContentAdapter? = null
    var showCheckBox = false
    //    Set<Integer> checkedItemIndex = new HashSet<>();
    internal var dir: File? = null

    internal var checkedItemIndex = HashMap<String, Long>()

    internal var pathRecyc: RecyclerView? = null


    //    @Override
    //    public void bindToRecyclerView(RecyclerView recyclerView) {
    //        super.bindToRecyclerView(recyclerView);
    //    }

    fun sortBy(refresh:Boolean) {
        val cmp = Collator.getInstance(Locale.CHINA)
        mData?.sortWith(object : Comparator<File> {
            override fun compare(f1: File?, f2: File?): Int {
                if (cmp.compare(f1?.name, f2?.name) > 0) {
                    return 1
                } else if (cmp.compare(f1?.name, f2?.name) < 0) {
                    return -1
                }
                return 0
            }
        })
        if(refresh){
            notifyDataSetChanged()
        }
    }

    internal var paths: ArrayList<String>? = ArrayList()
    internal var failterMode = 1//1模糊匹配 0精准
    internal var mFailter: String? = ""

    internal var pathRecycAdapter = PathRecycAdapter(paths)

    var currentDir: File? = null
        get() = if (dir != null) {
            dir!!
        } else mData[0].parentFile

    val currentDirPath: String
        get() = if (dir != null) {
            dir!!.absolutePath
        } else mData[0].parent

    internal var media_type: MediaUtil.MediaType? = null

    //    public ArrayList<File> getSelectedFiles() {
    //        ArrayList<File> list = new ArrayList<>();
    //        for (Integer i : checkedItemIndex) {
    //            list.add(mData.get(i));
    //        }
    //        return list;
    //    }

    val selectedFilesPath: HashMap<String, Long>
        get() {
            Log.i("getSelectedFilesPath", checkedItemIndex.size.toString() + "")
            return checkedItemIndex
        }


    var path_histroy: LinkedList<Any>? = LinkedList()

    private var selected: HasItemSelectedCallback? = null

    internal var type = 2

    //
    internal var sortType: Int = 0
    internal var sortOrdition: Int = 0

    fun bindToPathRecyc(Recyc: RecyclerView) {
        pathRecyc = Recyc
        pathRecyc?.layoutManager = LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false)
        pathRecyc?.adapter = pathRecycAdapter
    }


    fun remove(path: String): Int {
        for (i in paths?.indices!!) {
            if (path == paths!![i]) {
                paths?.remove(path)
                return i
            }
        }
        return -1
    }

    fun setDatas(rootName: String?, file: Any?) {
        if (file is File) {
            this.setDatas(rootName, file)
        } else if (file is String) {
            this.setDatas(rootName, file)
        }
    }

    //rootName 根节点描述//p 路径 多个用;间隔
    fun setDatas(rootName: String?, p: String?) {
        dir = null
        mData = getFiles(p!!)
        sortBy(false)
        notifyDataSetChanged()
        if (rootName != null) {
            paths?.clear()
            paths?.add(rootName)
            pathRecycAdapter.notifyDataSetChanged()
        }
    }


    fun setDatas(rootName: String?, dirfile: File?) {
        dir = dirfile
        this.setDatas(rootName, dirfile?.listFiles())
    }

    fun setDatas(rootName: String?, data: Array<File>) {
        val files = ArrayList<File>()
        for (file in data) {
            files.add(file)
        }
        this.setDatas(rootName, files)
    }

    fun setDatas(rootName: String?, data: ArrayList<File>) {
        //        this.rootName = rootName;

        mData = withFilter(data)
        if (!SUtil.isEmpty(rootName)) {
            if (paths != null && paths?.size!! > 0) {
                if (rootName != paths!![0]) {
                    dir = null
                }
            }

            paths?.clear()
            paths?.add(rootName!!)
            pathRecycAdapter.notifyDataSetChanged()

        }
        sortBy(false)
        notifyDataSetChanged()
        if (rootName != null) {
            pathRecycAdapter.notifyDataSetChanged()
            //            if (paths != null && paths.size() > 0) {
            //                if (!rootName.equals(paths.get(0))) {
            //                    dir = null;
            //                }
            //            }
        }
    }

    private fun withFilter(data: List<File>?): List<File>? {
        if (media_type == MediaUtil.MediaType.file) {
            return data
        } else {
            val result = ArrayList<File>()
            for (item in data!!) {
                if (MediaUtil.TypeEqual(media_type!!, item)) {
                    result.add(item)
                }
            }
            return result
        }

    }


    fun getPaths(data: List<File>): String {
        val sb = StringBuffer()
        for (i in data.indices) {
            sb.append(data[i].absoluteFile)
            if (i != data.size) {
                sb.append(";")
            }
        }
        return sb.toString()
    }

    fun cd_dir(file: File) {
        currentPath = file.absolutePath
        checkedItemIndex.clear()
        mData.clear()
        val files = file.listFiles()
        for (file1 in files) {
            if (MediaUtil.TypeEqual(media_type!!, file1)) {
                mData.add(file1)
            }
        }
        paths?.add(file.name)
        pathRecycAdapter.notifyDataSetChanged()
        sortBy(false)
        notifyDataSetChanged()
    }

    fun switchCheckState(b: Boolean?) {
        if (b == null) {
            showCheckBox = !showCheckBox

        } else {
            showCheckBox = b
        }
        notifyDataSetChanged()
    }

    override fun convert(helper: BaseViewHolder, item: File) {
        //        if(CarryPathDialogFragment.setting.toContain(item.getAbsolutePath())){
        //
        //        }else if(CarryPathDialogFragment.setting.fromContain(item.getAbsolutePath())){
        //
        //        }
        FileSystemHelper.loadFileIconToImageView(item, helper.getView<View>(R.id.iv) as ImageView, mContext)
        //        if(type==1){
        //            UiUtils.loadFileIconToImageView(item,(ImageView) helper.getView(R.id.iv),mContext);
        //        }else {
        //            if (item.isDirectory()) {
        //                File[] childs = item.listFiles();
        //                Glide.with(mContext).load(childs[0].getAbsolutePath()).into((ImageView) helper.getView(R.id.iv));

        //            } else {
        //                Glide.with(mContext).load(item.getAbsolutePath()).into((ImageView) helper.getView(R.id.iv));

        //            }
        //        }
        if (item.isDirectory) {
            helper.setText(R.id.tv_title, item.name +if(null==item.list()){""}else{ "(" + item.list().size + ")"})
        } else {
            helper.setText(R.id.tv_title, item.name)
        }
        helper.setVisible(R.id.cb_select, showCheckBox)
        if (showCheckBox) {
            helper.setChecked(R.id.cb_select, checkedItemIndex.keys.contains(item.absolutePath))
        }
        if (cut_paths.keys.contains(item.absolutePath)) {
            helper.setAlpha(R.id.fl_root, 0.5f)
        } else {
            helper.setAlpha(R.id.fl_root, 1f)
        }
        if (!SUtil.isEmpty(mFailter)) {
            if (failterMode == 1) {
                val count = 0
                val p = Pattern.compile(item.name)
                val m = p.matcher(mFailter)

                if (m.find()) {
                    helper.setAlpha(R.id.fl_root, 1f)
                } else {
                    helper.setAlpha(R.id.fl_root, 0.3f)
                }
            } else if (failterMode == 0) {
                if (item.name.toLowerCase().contains(mFailter!!)) {
                    helper.setAlpha(R.id.fl_root, 1f)
                } else {
                    helper.setAlpha(R.id.fl_root, 0.3f)
                }
            }
        }
        //        helper.addOnClickListener(R.id.iv);

    }

    fun select(position: Int) {
        checkedItemIndex[getItem(position)?.absolutePath!!] = System.currentTimeMillis()
        if (showCheckBox) {
            notifyItemChanged(position)
        } else {
            showCheckBox = !showCheckBox
            notifyDataSetChanged()
        }
    }

    fun switchItem(position: Int) {
        val path = getItem(position)?.absolutePath
        if (checkedItemIndex.keys.contains(path)) {
            checkedItemIndex.remove(path)
        } else {
            checkedItemIndex.put(path!!, System.currentTimeMillis())
        }

        notifyItemChanged(position)

        if (selected != null) {
            if (checkedItemIndex.size > 0) {
                if (checkedItemIndex.size == 1) selected?.onItemSelected()
            } else {
                selected?.onNothingSelected()
            }
        }

    }

    fun back(): Boolean {
        showCheckBox = false
        checkedItemIndex.clear()

        if (path_histroy != null && path_histroy?.size!! > 1) {
            path_histroy?.pop()
            setDatas(null, path_histroy?.first!!)
            paths?.removeAt(paths?.size!! - 1)
            pathRecycAdapter.notifyDataSetChanged()
        }
        return path_histroy?.size!! > 1
    }

    fun setSelectedCallback(selected: HasItemSelectedCallback) {
        this.selected = selected
    }

    fun unSelectAll() {
        checkedItemIndex.clear()
        notifyDataSetChanged()
    }

    fun setLayoutType(type: Int) {
        this.type = type
        when (type) {
            0 -> {
                mLayoutResId = R.layout.item_gallery_headline
                recyclerView.layoutManager = LinearLayoutManager(mContext)
            }
            1 -> {
                mLayoutResId = R.layout.item_gallery_list
                recyclerView.layoutManager = LinearLayoutManager(mContext)
            }
            2 -> {
                mLayoutResId = R.layout.item_gallery
                recyclerView.layoutManager = GridLayoutManager(mContext, 3)
            }
        }//                GalleryContentAdapter.this=new GalleryContentAdapter(cut_paths,copy_paths);
        notifyDataSetChanged()
        //        notifyAll();
        //        notify();
    }

    fun getCurrentPath(): String {
        return currentPath
    }


    fun setFailter(failter: String, failterMode: Int) {
        mFailter = failter
        this.failterMode = failterMode
        notifyDataSetChanged()

    }

    @TargetApi(Build.VERSION_CODES.N)
    fun sort(sortType: Int, sortOrdition: Int) {
        this.sortType = sortType
        this.sortOrdition = sortOrdition
        data.sortWith(Comparator { o1, o2 ->
            if (sortType == 0) {
                //                    return o1.getName()-o2.getName();
                1
            } else if (sortType == 1) {

                -1
            } else {
                if (sortOrdition == 0) {
                    if (o1.lastModified() > o2.lastModified()) -1 else 1
                } else {
                    if (o1.lastModified() > o2.lastModified()) 1 else -1
                }

            }
        })
    }

    fun hasDirPath(): Boolean {
        return false

    }

    fun setMediaType(mediaType: MediaUtil.MediaType) {
        media_type = mediaType
    }


    interface HasItemSelectedCallback {
        fun onItemSelected()

        fun onNothingSelected()
    }

    companion object {

        internal var currentPath: String = ""


        private fun getFiles(paths: String): List<File> {
            val files = ArrayList<File>()
            if (paths.contains(";")) {
                currentPath = ""
                val path = paths.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                for (item in path) {
                    val f = File(item)
                    if (f.exists()) {
                        files.add(f)
                    }

                }
            } else {
                files.add(File(paths))
            }
            return files
        }
    }
}
