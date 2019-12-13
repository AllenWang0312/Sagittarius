package edu.tjrac.swant.biubiu.view.account

import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.Vibrator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.FrameLayout
import com.google.gson.JsonObject
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import edu.tjrac.swant.baselib.common.base.BaseActivity
import edu.tjrac.swant.baselib.util.UiUtil
import edu.tjrac.swant.biubiu.MeituMainActivity
import edu.tjrac.swant.biubiu.R
import edu.tjrac.swant.biubiu.adapter.TabListAdapter
import edu.tjrac.swant.biubiu.bean.Tab
import edu.tjrac.swant.biubiu.net.BR
import edu.tjrac.swant.biubiu.net.NESubscriber
import edu.tjrac.swant.biubiu.net.Net
import edu.tjrac.swant.biubiu.net.RxUtil
import kotlinx.android.synthetic.main.activity_meitu_select_tab.*
import java.util.*
import kotlin.collections.ArrayList

class MeituSelectTabActivity : BaseActivity(), View.OnClickListener {

    internal var helper = ItemTouchHelper(object : ItemTouchHelper.Callback() {

        override fun isLongPressDragEnabled(): Boolean {
            return true
        }

        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
            var dragFrlg = 0
            if (recyclerView?.layoutManager is GridLayoutManager) {
                dragFrlg = ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            } else if (recyclerView?.layoutManager is LinearLayoutManager) {
                dragFrlg = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            }
            return makeMovementFlags(dragFrlg, 0)
        }

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            //滑动事件  下面注释的代码，滑动后数据和条目错乱，被舍弃
            //            Collections.swap(datas,viewHolder.getAdapterPosition(),target.getAdapterPosition());
            //            ap.notifyItemMoved(viewHolder.getAdapterPosition(),target.getAdapterPosition());

            //得到当拖拽的viewHolder的Position
            val fromPosition = viewHolder?.adapterPosition
            //拿到当前拖拽到的item的viewHolder
            val toPosition = target?.adapterPosition
            if (toPosition!! < selected?.size!!) {
                if (fromPosition!! < toPosition!!) {
                    for (i in fromPosition until toPosition) {
                        Collections.swap(selected, i, i + 1)
                    }
                } else {
                    for (i in fromPosition downTo toPosition + 1) {
                        Collections.swap(selected, i, i - 1)
                    }
                }
                adapter?.notifyItemMoved(fromPosition, toPosition)
                return true
            }
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

        }

        override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                viewHolder?.itemView?.setBackgroundColor(resources.getColor(R.color.colorAccent))
                //获取系统震动服务//震动70毫秒
                val vib = getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
                vib.vibrate(30)
            }
            super.onSelectedChanged(viewHolder, actionState)
        }

        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            super.clearView(recyclerView, viewHolder)
            viewHolder?.itemView?.setBackgroundColor(0)
            adapter?.notifyDataSetChanged()  //完成拖动后刷新适配器，这样拖动后删除就不会错乱
        }
    })

    var adapter: TabListAdapter? = null
    var data: ArrayList<Tab>? = ArrayList()
    var pageNo = 1
    var datas: ArrayList<ArrayList<Tab>>? = ArrayList()

    var selected: ArrayList<Tab>? = ArrayList()
    var tab_adapter: TagAdapter<Tab>? = null
    var maxSize = 10
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meitu_select_tab)

        recycler_selected.layoutManager = GridLayoutManager(this, 3)
        adapter = TabListAdapter(R.layout.item_tab_select, selected)
        recycler_selected.adapter = adapter
        helper.attachToRecyclerView(recycler_selected)

        tab_adapter = object : TagAdapter<Tab>(data) {
            override fun getView(parent: FlowLayout?, position: Int, i: Tab?): View {
                var dp8 = UiUtil.dp2px(this@MeituSelectTabActivity, 8).toInt()
                var item = layoutInflater.inflate(R.layout.item_tag_tab, null) as CheckBox
                item.isChecked = i?.checked!!
                var layoutparams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, UiUtil.dp2px(this@MeituSelectTabActivity, 30).toInt())
                layoutparams.leftMargin = dp8
                layoutparams.setMargins(dp8, dp8, dp8, dp8)
                item.layoutParams = layoutparams
                item.text = i?.name
                return item
            }
        }
        tag.setOnSelectListener {
            Log.i("selectList", "" + it.size!!)
            for (i in 0 until data?.size!!) {
                var item = data?.get(i)
                if (it.contains(i)) {
                    if (!selected?.contains(item)!!) {
                        selected?.add(item!!)
                        adapter?.notifyItemInserted(selected?.size!! - 1)
                    }
                } else {
                    if (selected?.contains(item)!!) {
                        adapter?.notifyItemRemoved(data?.indexOf(item)!!)
                        selected?.remove(item!!)
                    }
                }
            }
            bt_submit?.text = "完成(" + selected?.size!! + "/" + maxSize + ")"
        }
        tag.adapter = tab_adapter
        initData()
        tv_pre.setOnClickListener(this)
        tv_next.setOnClickListener(this)
        bt_submit.setOnClickListener(this)
        bt_skip.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.bt_submit -> {
                Net.instance.getApiService().followTabs(selected)
                        .compose(RxUtil.applySchedulers())
                        .subscribe(object : NESubscriber<BR<JsonObject>>(this) {
                            override fun onSuccess(t: BR<JsonObject>?) {
                                finish()
                                startActivity(Intent(this@MeituSelectTabActivity, MeituMainActivity::class.java))
                            }
                        })
            }
            R.id.bt_skip -> {
                startActivity(Intent(this, MeituMainActivity::class.java))
            }
            R.id.tv_pre -> {
                if (pageNo == 1) {

                } else {
                    pageNo--
                    data?.clear()
                    data?.addAll(datas?.get(pageNo - 1)!!)
                    tab_adapter?.notifyDataChanged()
                }
                initVisiable()
            }
            R.id.tv_next -> {
                if (pageNo == datas?.size!!) {
                    pageNo++
                    initData()
                } else {
                    pageNo++
                    data?.clear()
                    data?.addAll(datas?.get(pageNo - 1)!!)
                    tab_adapter?.notifyDataChanged()
                }
                initVisiable()
            }
        }
    }

    private fun initVisiable() {
        if (pageNo == 1) {
            tv_pre.visibility = View.INVISIBLE
        } else {
            tv_pre.visibility = View.VISIBLE
        }
    }

    private fun initData() {
        Net.instance.getApiService().getTabs(null, pageNo)
                .compose(RxUtil.applySchedulers())
                .subscribe(object : NESubscriber<BR<ArrayList<Tab>>>(this) {
                    override fun onSuccess(t: BR<ArrayList<Tab>>?) {
                        if (t?.data?.size!! > 0) {
                            data?.clear()
                            data?.addAll(t?.data!!)
                            tag.setMaxSelectCount(maxSize - selected?.size!!)
                            tab_adapter?.notifyDataChanged()
                            datas?.add(t?.data!!)
                        }
                    }
                })

    }
}
