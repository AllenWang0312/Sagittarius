package edu.tjrac.swant.meitu.view.mine

import android.os.Bundle
import android.view.View
import edu.tjrac.swant.baselib.common.adapter.FragmentsPagerAdapter
import edu.tjrac.swant.baselib.common.base.BaseBarActivity
import edu.tjrac.swant.meitu.R
import edu.tjrac.swant.meitu.fragment.ColumCollectionFragment
import edu.tjrac.swant.meitu.fragment.FollowModelsFragment
import kotlinx.android.synthetic.main.activity_meitu_collections.*

class MeituCollectionsActivity : BaseBarActivity() {

    var adapter: FragmentsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meitu_collections)
        setToolbar(findViewById(R.id.toolbar))

        adapter = FragmentsPagerAdapter(supportFragmentManager)

        if (intent.hasExtra("title")) {
            var title = intent.getStringExtra("title")
            setTitle(title)
            adapter?.addFragment(FollowModelsFragment(), resources.getString(R.string.model))
        } else {
            setTitle("我的收藏")

            adapter?.addFragment(ColumCollectionFragment(), resources.getString(R.string.colum))
        }





        vp?.adapter = adapter
        tab?.setupWithViewPager(vp)
    }

    override fun setToolbar(tool: View) {
        super.setToolbar(tool)
        enableBackIcon()


    }
}
