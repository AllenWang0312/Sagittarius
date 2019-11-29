package edu.tjrac.swant.meitu_v2.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import edu.tjrac.swant.baselib.common.adapter.FragmentsPagerAdapter
import edu.tjrac.swant.baselib.common.base.BaseFragment
import edu.tjrac.swant.meitu.R
import kotlinx.android.synthetic.main.fragment_main.view.*

/**
 * Created by wpc on 2019-11-21.
 */
class HomeFragment : BaseFragment() {

    var v: View? = null
    var adapter: FragmentsPagerAdapter? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_main, container, false)
        adapter = FragmentsPagerAdapter(fragmentManager!!)

        adapter?.addFragment(HomeFollowFragment(), "cycler")
        adapter?.addFragment(HomeHomeFragment(), "home_home")

        v?.vp?.adapter = adapter
        v?.tab?.setupWithViewPager(v?.vp!!)


        return v
    }
}