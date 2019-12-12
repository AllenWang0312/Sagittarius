package edu.tjrac.swant.meitu.view.phone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import edu.tjrac.swant.baselib.common.adapter.V4FragmentsPagerAdapter
import edu.tjrac.swant.baselib.common.base.BaseFragment
import edu.tjrac.swant.meitu.R
import kotlinx.android.synthetic.main.fragment_main.view.*

/**
 * Created by wpc on 2019-11-21.
 */
class PhoneFragments : BaseFragment() {

    var v: View? = null
    var adapter: V4FragmentsPagerAdapter? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_main, container, false)
        adapter = V4FragmentsPagerAdapter(childFragmentManager)

        adapter?.addFragment(PhoneFragment("hot"), "Hot")
        adapter?.addFragment(PhoneFragment("iphone"), "iPhone")
        adapter?.addFragment(PhoneFragment("Huawei"), "Huawei")
//        adapter?.addFragment(TrendFragment("follow"), "follow")
//        adapter?.addFragment(HomeFragment(), "home_home")

        v?.vp?.adapter = adapter
        v?.tab?.setupWithViewPager(v?.vp!!)
        return v
    }
}