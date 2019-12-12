package edu.tjrac.swant.meitu.view.trend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import edu.tjrac.swant.baselib.common.adapter.V4FragmentsPagerAdapter
import edu.tjrac.swant.baselib.common.base.BaseFragment
import edu.tjrac.swant.common.view.TabItemState
import edu.tjrac.swant.meitu.R
import kotlinx.android.synthetic.main.fragments_trend.view.*

/**
 * Created by wpc on 2019-11-21.
 */
class TrendFragments : BaseFragment() {

    var v: View? = null
    var adapter: V4FragmentsPagerAdapter? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragments_trend, container, false)
        adapter = V4FragmentsPagerAdapter(childFragmentManager)

        adapter?.addFragment(TrendFragment(), "全部")
//        adapter?.addFragment(StyleFragment(),"test")
        adapter?.addFragment(TrendFragment("follow"), "关注")

        v?.vp?.adapter = adapter

//        v?.tab?.addView()
        v?.tab?.normal = TabItemState(resources.getColor(R.color.text_color_primary),16f
//                UiUtil.sp2px(activity!!, 16)
        )
        v?.tab?.selected = TabItemState(resources.getColor(R.color.colorAccent), 20f
//                UiUtil.sp2px(activity!!, 20)
        )

        v?.tab?.setupWithViewPager(v?.vp!!)
        return v
    }
}