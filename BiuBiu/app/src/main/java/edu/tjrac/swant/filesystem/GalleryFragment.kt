package edu.tjrac.swant.filesystem

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.*
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.IBinder
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.SearchView
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import edu.tjrac.swant.Config
import edu.tjrac.swant.baselib.common.base.BaseFragment
import edu.tjrac.swant.baselib.uncom.CameraUtil
import edu.tjrac.swant.baselib.util.*
import edu.tjrac.swant.filesystem.Config.RESET_PATH_SETTING_PATH
import edu.tjrac.swant.filesystem.adapter.ClipBoardRecycAdapter
import edu.tjrac.swant.filesystem.adapter.GalleryContentAdapter
import edu.tjrac.swant.filesystem.bean.RePushTaskInfo
import edu.tjrac.swant.filesystem.bean.TaskGroup
import edu.tjrac.swant.wjzx.R
import kotlinx.android.synthetic.main.fragment_gallery.*
import kotlinx.android.synthetic.main.gallery_bottom_sheet.*
import kotlinx.android.synthetic.main.gallery_bottom_sheet.view.*
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by wpc on 2018/1/12 0012.
 */

class GalleryFragment : BaseFragment(), View.OnClickListener, View.OnLongClickListener {


    //    @BindView(R.id.fab_sort) FloatingActionButton fab_sort;
    //    @BindView(R.id.fab_content) FloatingActionButton fab_content;
    //    @BindView(R.id.fab_left) FloatingActionButton fab_left;
    //    @BindView(R.id.fab) FloatingActionButton fab;
    //    @BindView(R.id.fab_add) FloatingActionButton fab_add;
    //    @BindView(R.id.fab_more) FloatingActionButton fab_more;

    //    @BindView(R.id.et_search) EditText et_search;
    //    @BindView(R.id.iv_search) ImageView iv_search;
    //    @BindView(R.id.rv_paths) RecyclerView rv_paths;
    //    @BindView(R.id.rv_gallery_content) RecyclerView rv_gallery_content;
    //    @BindView(R.id.tv_clipfile_size) TextView tv_clipfile_size;
    //    @BindView(R.id.swiper) SwipeRefreshLayout swiper;

    //    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    //    @BindView(R.id.nav_view) NavigationView navigationView;
    //    @BindView(R.id.ll_bottom) View ll_bottom;
    private var mBottomSheetBehavior: BottomSheetBehavior<LinearLayout>? = null

    private val types = intArrayOf(
        R.drawable.ic_view_headline_white_24dp,
        R.drawable.ic_view_list_white_24dp,
        R.drawable.ic_view_module_white_24dp
    )

    internal var filter: RecycItemFilter<File>? = null
    internal var adapter: GalleryContentAdapter? = null
    //    ArrayList<GalleryContentAdapter.GalleryInfo> infos = new ArrayList<>();
    internal var gallerys = ArrayList<File>()
    internal var quick_res: String? = null
    internal var paths: ArrayList<String>? = null

    internal var cut_paths: HashMap<String, Long>? = HashMap()
    internal var copy_paths: HashMap<String, Long>? = HashMap()
    //    PopupWindow mPopupWindow;

    internal var clipAdapter: ClipBoardRecycAdapter? = null
    //    @BindView(R.id.adView)
    //     AdView mAdView;

    internal var sp: SharedPreferences? = null

    private val mItemSelectedCallback = object : GalleryContentAdapter.HasItemSelectedCallback {
        override fun onItemSelected() {
            activity?.runOnUiThread {
                cut?.isVisible = true
                copy?.isVisible = true
                delete?.isVisible = true
                paste?.isVisible = true
                //                    rename.setVisible(true);
                search?.isVisible = false
            }
        }

        override fun onNothingSelected() {
            activity?.runOnUiThread {
                cut?.isVisible = false
                copy?.isVisible = false
                delete?.isVisible = false
                paste?.isVisible = false
                //                    rename.setVisible(false);
                search?.isVisible = true
            }

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        sp = activity?.getSharedPreferences(Config.SP.GallerySetting, Context.MODE_PRIVATE)

        val view = inflater.inflate(R.layout.fragment_gallery, container, false)

        mBottomSheetBehavior = BottomSheetBehavior.from<LinearLayout>(view.ll_bottom)

        mBottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED

        mBottomSheetBehavior?.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {

            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                Log.i("slideOffset", slideOffset.toString())

                view.ll_bottom.setBackgroundColor(((0xff * slideOffset).toInt() shl 24) + 0xffffff)

            }
        })
        //        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        //                getActivity(), drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //        drawer.addDrawerListener(toggle);
        //        navigationView.setNavigationItemSelectedListener(this);
        //
        //        toggle.syncState();

        //        AdRequest adRequest = new AdRequest.Builder().build();
        //        mAdView.loadAd(adRequest);
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        swiper.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            if (adapter != null) {
                adapter?.notifyDataSetChanged()
                swiper.setRefreshing(false)
            }
        })
        adapter = GalleryContentAdapter(cut_paths!!, copy_paths!!)

        filter = object : RecycItemFilter<File>(rv_gallery_content, adapter?.data) {
            override fun equal(data: File, filter: String): Boolean {
                return data.name.contains(filter)
            }
        }

        //        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
        //            rv_gallery_content.setLayoutManager(new GridLayoutManager(getActivity(), 3, RecyclerView.HORIZONTAL, false));
        //            Log.i("info", "landscape");
        //        } else if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
        rv_gallery_content.setLayoutManager(GridLayoutManager(activity, 3))
        //            Log.i("info", "portrait");
        //        }
        adapter?.bindToRecyclerView(rv_gallery_content)
        adapter?.bindToPathRecyc(rv_paths)
        adapter?.onItemLongClickListener = BaseQuickAdapter.OnItemLongClickListener { ad, view, position ->
            adapter?.switchCheckState(null)
            false
        }
        adapter?.setOnItemClickListener { ad, view, position ->
            if (adapter?.showCheckBox!!) {
                adapter?.switchItem(position)
            } else {
                val file = adapter?.getItem(position)
                if (file?.isDirectory!!) {
                    backable = true
                    adapter?.path_histroy?.push(file)
                    adapter?.cd_dir(file)
                } else {
                    if (".h264".contains(SUtil.getEndString(file.name))) {
                        //                            startActivity(new Intent(getActivity(), H264Activity.class)
                        //                            .putExtra("path",file.getAbsoluteFile()));

                    }
//                    else if (OpenGLActivity.res_type.contains(SUtil.getEndString(file.name))) {
//                        OpenGLActivity.start(activity?, file.absolutePath)
//                    } else if (GalleryAlbumActivity.res_type.contains(SUtil.getEndString(file.name))) {
//                        GalleryAlbumActivity.start(activity?,
//                                adapter?.getItem(position)?.getParent(), position
//                        )
//                    } else if (LottieViewerActivity.res_type.contains(SUtil.getEndString(file.name))) {
//                        LottieViewerActivity.start(activity?, file.absolutePath)
//                    } else if (MusicPlayerActivity.res_type.contains(SUtil.getEndString(file.name))) {
//                        MusicPlayerActivity.start(activity?,
//                                file.absolutePath?
//                        )
//                    }
                    else {
                        val file1 = adapter?.getItem(position)
                        Log.i("open file", file1?.absolutePath)
                        FileUtils.openFile(activity, file1)
                    }//                        else if(MediaPlayerActivity.){
                    //
                    //                        }
                }
            }
        }
        adapter?.setSelectedCallback(mItemSelectedCallback)
        //       rv_gallery_content.setLayoutManager(new );
        rv_gallery_content.setAdapter(adapter)


        //剪切adapter
        clipAdapter = ClipBoardRecycAdapter(clipboardData)
        recycler?.layoutManager = GridLayoutManager(activity, 5)

        clipAdapter?.bindToRecyclerView(recycler)
        clipAdapter?.setEmptyView(R.layout.empty)
        //            recycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        clipAdapter?.setOnItemClickListener { ad, view, position ->
            //                    clipboardData.remove(position);
            clipAdapter?.remove(position)
            clipAdapter?.notifyItemRemoved(position)
            adapter?.notifyDataSetChanged()
            notifyClipBoardCountChanged()
        }
        clipAdapter?.bindToPathsType(cut_paths, copy_paths)
        recycler?.adapter = clipAdapter

//        fab_tasks.setOnClickListener(this)
        fab_reset.setOnClickListener(this)
        fab_reset.setOnLongClickListener(this)
        fab_left.setImageResource(types[2])

        fab_sort.setOnClickListener(this)
        fab_content.setOnClickListener(this)
        fab_left.setOnClickListener(this)
        fab_add.setOnClickListener(this)
        fab.setOnClickListener(this)
        fab_more.setOnClickListener(this)
        //        iv_search.setImeOptions(EditorInfo.IME_ACTION_SEND);
        //        et_search.setOnKeyListener(new View.OnKeyListener() {
        //            @Override
        //            public boolean onKey(View v, int keyCode, KeyEvent event) {
        //                if (KeyEvent.KEYCODE_SEARCH == keyCode && KeyEvent.ACTION_DOWN == event.getAction()) {
        //                    //处理事件
        //                    adapter.setFailter(et_search.getText().toString());
        //                    return true;
        //                }
        //                return false;
        //            }
        //        });
        initGalleryInfos(MediaUtil.MediaType.image)
        fab_content.setImageResource(R.drawable.ic_collections_white_24dp)
        super.onViewCreated(view, savedInstanceState)

    }


    internal fun showAddFilePopMenu(type: MediaUtil.MediaType, dirPath: String) {
        if (SUtil.isEmpty(dirPath)) {
            T.show(activity!!, "当前路径不可用")
        } else {
            val builder = AlertDialog.Builder(activity)
            when (type) {
                MediaUtil.MediaType.file -> {
                    builder.setTitle("新建")
                    builder.setItems(arrayOf("文件夹", "文件")) { dialog, which ->
                        when (which) {
                            0 -> showCreateFileDialog(dirPath, true)
                            1 -> showCreateFileDialog(dirPath, false)
                        }
                    }
                }

                MediaUtil.MediaType.image -> {
                    builder.setTitle("新建")
                    builder.setItems(arrayOf("文件夹", "图片:系统相机", "图片:网络资源")) { dialog, which ->
                        when (which) {
                            0 -> showCreateFileDialog(dirPath, true)
                            1 -> {

                                val log = EditTextDialog(activity, "文件名", "请输入文件名")
                                log.positive = DialogInterface.OnClickListener { dialog, which ->
                                    var name = log.editText.text.toString()
                                    if (SUtil.isEmpty(name)) {
                                        name = System.currentTimeMillis().toString() + ".jpg"

                                    }
                                    if (!name.endsWith(".jpg")) {
                                        name = name + ".jpg"
                                    }
                                    val file = File(dirPath, name)
                                    if (!file.exists()) {
                                        try {
                                            file.createNewFile()
                                            CameraUtil.takePhotoWithSystemApp(activity, Uri.fromFile(file))
                                        } catch (e: IOException) {
                                            e.printStackTrace()
                                        }

                                    }
                                }
                                log.show(fragmentManager!!, "create file")
                            }
                        }
                    }
                }
            }
            builder.create().show()
        }

    }


    private fun showCreateFileDialog(dirPath: String, isDir: Boolean) {
        val dir = EditTextDialog(activity, "新建文件夹", "请输入文件名")
        dir.positive = DialogInterface.OnClickListener { dialog, which ->
            val name = dir.editText.text.toString()
            if (SUtil.isEmpty(name)) {
                T.show(activity, "请输入文件名")
            } else {
                val file = File(dirPath, name)
                if (file.exists()) {
                    T.show(activity, if (isDir) "文件夹已存在" else "文件已存在")
                } else {
                    if (isDir) {
                        file.mkdir()
                    } else {
                        try {
                            file.createNewFile()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }

                    }
                    adapter?.notifyDataSetChanged()
                }

            }
        }
        dir.show(fragmentManager!!, "create file")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    internal fun showPopView() {
        if (clipAdapter == null) {
            //            recycler = (RecyclerView) LayoutInflater.from(getActivity()).inflate(R.layout.recyclerview, null);
            getClipboardData()
            //            View empty = LayoutInflater.from(getActivity()).inflate(R.layout.empty, null);


            //            mPopupWindow = new PopupWindow(recycler, 600, 400);
            ////            mPopupWindow.setAnimationStyle(R.style.popup_window_anim);
            //            mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F8F8F8")));
            //            mPopupWindow.setFocusable(true);
            //            mPopupWindow.setOutsideTouchable(true);
            //            mPopupWindow.update();

        } else {
            getClipboardData()
            clipAdapter?.notifyDataSetChanged()
        }
        mBottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
        //        mPopupWindow.showAsDropDown(fab);
    }

    internal var clipboardData = ArrayList<String>()

    @SuppressLint("NewApi")
    private fun getClipboardData() {
        clipboardData.clear()
        if (cut_paths == null || copy_paths == null) {

        } else {
            //            ArrayList<Long> keys = new ArrayList<>();
            //            keys.addAll(cut_paths.values());
            //            keys.addAll(copy_paths.values());
            //
            //            keys.sort(new Comparator<Long>() {
            //                @Override
            //                public int compare(Long o1, Long o2) {
            //                    return o1 > o2 ? 1 : -1;
            //                }
            //            });
            //
            //            for (String value : keys) {
            //                if (value != null) {
            //                    clipboardData.add(value);
            //                }
            //            }
            clipboardData.addAll(cut_paths?.keys!!)
            clipboardData.addAll(copy_paths?.keys!!)

        }
    }

    internal var type: MediaUtil.MediaType? = null

    private fun initGalleryInfos(type: MediaUtil.MediaType) {
        this.type = type
        adapter?.setMediaType(type)

        //        infos.clear();
        var typeString = ""
        gallerys.clear()
        var uri: Uri? = null
        when (type) {
            MediaUtil.MediaType.file -> {
                val file = File(Phone.SDCardPath)
                adapter?.path_histroy?.clear()
                adapter?.path_histroy?.push(file)
                adapter?.setDatas("本地文件", file)
                adapter?.notifyDataSetChanged()
                return
            }
            MediaUtil.MediaType.image -> {
                uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                typeString = "图片"
            }
            MediaUtil.MediaType.video -> {
                uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                typeString = "视屏"
            }
            MediaUtil.MediaType.music -> {
                uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                typeString = "音乐"
            }
        }
        val contentResolver = activity?.contentResolver
        val cursor = contentResolver?.query(uri!!, null, null, null, null)
        if (cursor == null || cursor.count <= 0) {
            return  // 没有图片
        }
        while (cursor.moveToNext()) {
            val index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            val path = cursor.getString(index) // 文件地址
            val file = File(path)
            if (file.exists()) {
                //                    infos.add(new GalleryContentAdapter.GalleryInfo(file.getName(), file.getAbsolutePath()));
                if (gallerys.contains(file.parentFile)) {
                    continue
                } else {
                    gallerys.add(file.parentFile)
                }
                adapter?.setDatas(typeString, gallerys)
                adapter?.path_histroy?.push(adapter?.getPaths(gallerys))
                adapter?.notifyDataSetChanged()
            }
        }
        //        getActivity().bindService(
        //                new Intent(getActivity(), FileWifiShareServer.class)
        //                ,connection,BIND_AUTO_CREATE);
    }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            L.i(TAG, "onServiceConnected")
        }

        override fun onServiceDisconnected(name: ComponentName) {
            L.i(TAG, "onServiceDisconnected")
        }
    }
    internal var menu: Menu? = null
    internal var copy: MenuItem? = null
    internal var cut: MenuItem? = null
    internal var paste: MenuItem? = null
    internal var delete: MenuItem? = null
    //            rename,
    internal var search: MenuItem? = null

    internal var searchview: SearchView? = null

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.gallery, menu)
        this.menu = menu
        copy = menu?.findItem(R.id.copy)
        cut = menu?.findItem(R.id.cut)
        paste = menu?.findItem(R.id.paste)
        delete = menu?.findItem(R.id.delete)
        //        rename = menu.findItem(R.id.rename_files);
        search = menu?.findItem(R.id.search)
        searchview = search?.actionView as SearchView
        searchview?.imeOptions = EditorInfo.IME_ACTION_NEXT
        searchview?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter?.setFailter(newText, 0)
                filter?.find(newText)

                return false
            }
        })
        searchview?.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                filter?.skipToNext()
            }
            true
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val paths = adapter?.selectedFilesPath

        when (item?.itemId) {
            //            case R.id.sort:
            //                String[] sortType = getActivity().getResources().getStringArray(R.array.sorttype);
            //                String[] oradition = getActivity().getResources().getStringArray(R.array.sortoradition);
            //
            //
            //                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            //                View view = LayoutInflater.from(getActivity()).inflate(R.layout.sort_mode, null);
            //                builder.setTital("选择排序方式");
            //
            //                RadioGroup left = view.findViewById(R.id.rb_left),
            //                        right = view.findViewById(R.id.rb_right);
            //                inflateItem(Config.SP.sortType, left, sortType);
            //                inflateItem(Config.SP.sortOrdition, right, oradition);
            //
            //
            //                builder.setView(view);
            //                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            //                    @Override
            //                    public void onClick(DialogInterface dialog, int which) {
            //                        SharedPreferences sp = getActivity().getSharedPreferences(Config.SP.GallerySetting, Context.MODE_PRIVATE);
            //
            //                        adapter.sort(sp.getInt(Config.SP.sortType, 0),
            //                                sp.getInt(Config.SP.sortType, 0));
            //
            //                    }
            //                });
            //                builder.create().show();
            //                break;
            R.id.cut -> {
                cut_paths?.putAll(paths!!)
                adapter?.unSelectAll()
                notifyClipBoardCountChanged()
                if (mBottomSheetBehavior?.state == BottomSheetBehavior.STATE_EXPANDED) {
                    if (clipAdapter != null) {
                        getClipboardData()
                        clipAdapter?.notifyDataSetChanged()
                    }
                }
            }
            R.id.copy -> {
                copy_paths?.putAll(paths!!)
                adapter?.unSelectAll()
                notifyClipBoardCountChanged()
            }
            R.id.paste -> {
//                AsyPasteThread().execute()
            }
            R.id.delete -> {
                val selectFiles = adapter?.selectedFilesPath?.keys!!
                for (file in selectFiles) {
                    val f = File(file)
                    if (f.exists()) {
                        f.delete()
                        val position = adapter?.remove(file)
                        adapter?.notifyItemRemoved(position!!)
//                        Log.i("file is delete", f.absolutePath)
                    }
                }
            }
        }//                copy_paths.put()
        //            case R.id.rename_files:
        //                break;
        return super.onOptionsItemSelected(item)
    }

    private fun inflateItem(key: String, left: RadioGroup, sortType: Array<String>) {
        for (i in sortType.indices) {
            val button = RadioButton(activity)
            button.layoutParams =
                RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            button.setPadding(20, 10, 20, 10)
            button.text = sortType[i]
            val finalI = i
            button.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    activity?.getSharedPreferences(Config.SP.GallerySetting, Context.MODE_PRIVATE)?.edit()?.putInt(key, finalI)?.commit()
                }
            }
            left.addView(button)
        }
    }

    private fun notifyClipBoardCountChanged() {
        val i = cut_paths?.size!! + copy_paths?.size!!
        if (i == 0) {
            tv_clipfile_size.setVisibility(View.GONE)
        } else {
            tv_clipfile_size.setVisibility(View.VISIBLE)
            tv_clipfile_size.setText(i.toString() + "")
        }

    }

    override fun onDestroy() {
        super.onDestroy()
    }

    var backable = false

    override fun onBack() {
        if (mBottomSheetBehavior?.state == BottomSheetBehavior.STATE_EXPANDED) {
            mBottomSheetBehavior?.setState(BottomSheetBehavior.STATE_COLLAPSED)
        } else {
            backable = adapter?.back()!!
        }
    }
    var config = File(Phone.SDCardPath + RESET_PATH_SETTING_PATH)

    override fun onLongClick(v: View?): Boolean {
        when (v?.id) {
            R.id.fab_reset -> {
                FileUtils.openFile(activity!!,config)
            }
        }
        return true
    }

    override fun onClick(v: View) {
        when (v.id) {
//            R.id.fab_tasks -> {
//                initRePushTaks()
//                var pop = layoutInflater.inflate(R.layout.repush_task, null) as RecyclerView
//                var foot = layoutInflater.inflate(R.layout.foot_repush_task, null)
//                var adapter = RePushTasksAdapter(rePushTasks)
//                adapter.setOnItemChildClickListener { adapter, view, position ->
//                    rePushTasks?.get(position)?.tasks?.add(RePushTaskInfo())
//                    adapter.notifyItemChanged(position)
//                }
//                foot.tv_new_group.setOnClickListener {
//                    if (rePushTasks == null) {
//                        rePushTasks = ArrayList()
//                    }
//                    var edit = EditTextDialog(activity?, "新建分组", "请输入分组名")
//                    edit.setPositive { dialog, which ->
//                        rePushTasks?.add(TaskGroup(edit.editText.text.toString()))
//                        adapter.notifyDataSetChanged()
//                    }
//                    edit.show(childFragmentManager, "edittext")
//                }
//                foot.bt_save.setOnClickListener {
//                    savePushTask()
//                }
//                foot.bt_run.setOnClickListener {
//                    var asyTask = RePushTask(activity?, map(rePushTasks?))
//                    asyTask.execute()
//                }
//                pop.layoutManager = LinearLayoutManager(activity)
//                adapter.addFooterView(foot)
//
//                pop.adapter = adapter
//                var popWin = PopupWindow(pop, UiUtil.getScreenWidth(activity?), 400);
//                //            mPopupWindow.setAnimationStyle(R.style.popup_window_anim);
//                popWin.setBackgroundDrawable(ColorDrawable(Color.parseColor("#F8F8F8")))
//                popWin.isFocusable = true
//                popWin.isOutsideTouchable = true
////                var popWin = PopupWindow()
////                popWin.contentView = pop
//                popWin.update()
//
//                popWin.showAsDropDown(fab_tasks)
//
//            }
            R.id.fab_reset -> {

                if(config.exists()){
                    var configs = FileUtils.readFile(config)
                    var configss=configs.split("\n")
                    var task=ResetPathAsyTask(activity!!,configss)
                    task.execute(".png",".jpeg",".jpg",".gif")
//                    T.show(configss[0])
                }else{
                    T.show("没有配置文件 $RESET_PATH_SETTING_PATH")
                }

            }
            R.id.fab_sort -> UiUtil.showPopmenu(activity!!, fab_sort, true,
                R.menu.gallery_content_sort_type, PopupMenu.OnMenuItemClickListener { item ->
                    when (item.itemId) {

                    }
                    true
                })

            R.id.fab_content -> UiUtil.showPopmenu(activity!!, fab_content, true,
                R.menu.gallery_content_type, PopupMenu.OnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.filesystem -> {
                            initGalleryInfos(MediaUtil.MediaType.file)
                            fab_content.setImageResource(R.drawable.ic_folder_shared_white_24dp)
                        }
                        R.id.image -> {
                            initGalleryInfos(MediaUtil.MediaType.image)
                            fab_content.setImageResource(R.drawable.ic_collections_white_24dp)
                        }
                        R.id.video -> {
                            initGalleryInfos(MediaUtil.MediaType.video)
                            fab_content.setImageResource(R.drawable.ic_video_library_white_24dp)
                        }
                        R.id.music -> {
                            initGalleryInfos(MediaUtil.MediaType.music)
                            fab_content.setImageResource(R.drawable.ic_library_music_white_24dp)
                        }
                    }
                    true
                })
            R.id.fab_more -> {
                UiUtil.showPopmenu(activity!!, fab_more, false,
                    R.menu.path_option, PopupMenu.OnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.clip -> {
                                if (SUtil.isEmpty(adapter?.getCurrentPath())) {
                                    T.show(activity!!, "当前路径不可用")
                                } else {
                                    val cm = activity!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    cm.text = adapter!!.currentDirPath
                                    T.show(activity, "已复制当前路径: \n " + adapter?.currentDirPath)
                                }
                            }
                            R.id.set_carry_path -> {
                                if (SUtil.isEmpty(adapter?.getCurrentPath())) {
//                                    T.show(activity, "当前路径不可用")
                                } else {
                                    CarryPathDialogFragment(adapter?.getCurrentPath()!!, "")
                                        .show(childFragmentManager, "carry_path")

                                    //                                            ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                                    //                                            cm.setText(adapter.getCurrentDirPath());
                                    //                                            T.show(getActivity(), "已复制当前路径: \n " + adapter.getCurrentDirPath());
                                }
                            }
                            R.id.reanme -> {
                            }
                        }
                        false
                    })
            }

            R.id.fab ->
                //显示剪切板
                if (mBottomSheetBehavior?.state == BottomSheetBehavior.STATE_COLLAPSED) {
                    showPopView()
                } else {
                    mBottomSheetBehavior?.setState(BottomSheetBehavior.STATE_COLLAPSED)
                }


            R.id.fab_left -> UiUtil.showPopmenu(activity!!, fab_left, true,
                R.menu.layouttype, PopupMenu.OnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.action_headline -> {
                            adapter?.setLayoutType(0)
                            fab_left.setImageResource(types[0])
                        }
                        R.id.action_list -> {
                            adapter?.setLayoutType(1)
                            fab_left.setImageResource(types[1])
                        }
                        R.id.action_module -> {
                            adapter?.setLayoutType(2)
                            fab_left.setImageResource(types[2])
                        }
                    }//                                        init
                    false
                })
            R.id.fab_add -> when (type) {
                MediaUtil.MediaType.file -> showAddFilePopMenu(MediaUtil.MediaType.file, adapter?.getCurrentPath()!!)
                MediaUtil.MediaType.image -> showAddFilePopMenu(MediaUtil.MediaType.image, adapter?.getCurrentPath()!!)
                MediaUtil.MediaType.music -> {
                }
                MediaUtil.MediaType.video -> {
                }
            }
        }
    }

    private fun map(rePushTasks: ArrayList<TaskGroup>): ArrayList<RePushTaskInfo> {
        var array = ArrayList<RePushTaskInfo>()
        for (i in rePushTasks) {
            if (i.checked) {
                if (null != i.tasks && i.tasks?.size!! > 0) {
                    for (j in i.tasks!!) {
                        if (j.checked) {
                            array.add(j)
                        }
                    }
                }

            }
        }
        return array
    }

    var rePushTasks: ArrayList<TaskGroup>? = ArrayList()

    private fun initRePushTaks() {
        var configs = sp?.getString(Config.SP.REPUSHTASK, "")
        if (!SUtil.isEmpty(configs)) {
            try {
                var gson =
                    Gson().fromJson<ArrayList<TaskGroup>>(configs, object : TypeToken<ArrayList<TaskGroup>>() {}.type)
                rePushTasks?.addAll(gson)
            } catch (e: Exception) {
            }
        }
    }

    private fun savePushTask() {
        var gson = Gson().toJson(rePushTasks)
        sp?.edit()?.putString(Config.SP.REPUSHTASK, gson)?.commit()
    }


    private class AsyPasteThread : AsyncTask<Int, Int, String>() {


        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg integers: Int?): String? {
//            val file = File(adapter?.currentPath)
//            val cut_keys = cut_paths?.keys
//            val copy_keys = copy_paths?.keys
//            for (item in cut_keys) {
//
//                val file1 = File(item)
//                file1.renameTo(File(adapter?.currentPath, file1.name))
//            }
//            for (item in copy_keys) {
//                val file1 = File(item)
//                if (file1.isDirectory) {
//                    FileUtils.copyFolder(item, adapter?.currentPath)
//                } else {
//                    FileUtils.copyFile(item, adapter?.currentPath)
//                }
//            }
            return null
        }

        override fun onPostExecute(string: String) {

            super.onPostExecute(string)
        }
    }


}
