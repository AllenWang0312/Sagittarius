package edu.tjrac.swant.meitu.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import edu.tjrac.swant.meitu.R
import edu.tjrac.swant.meitu.adapter.TabListAdapter
import edu.tjrac.swant.meitu.bean.Tab

class MeituSelectTabActivity : AppCompatActivity() {

    var adapter: TabListAdapter?=null
    var data:ArrayList<Tab>?=ArrayList()
    var datas:ArrayList<ArrayList<Tab>>?=ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meitu_select_tab)
        adapter=TabListAdapter(R.layout.item_tab_select,data)
    }
}
