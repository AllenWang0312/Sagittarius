package edu.tjrac.swant.meitu_v2.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import edu.tjrac.swant.baselib.common.base.BaseFragment
import edu.tjrac.swant.meitu.R
import edu.tjrac.swant.meitu.bean.Tab
import edu.tjrac.swant.meitu.net.Net

/**
 * Created by wpc on 2019-11-28.
 */

class HomeFollowFragment : BaseFragment() {

    var v: View? = null

    var follows = ArrayList<Tab>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_home_follow, container, false)

        return v
    }

    var pageNo = 1
    var pageSize = 20

    fun onGetFollowSuccess() {
        Net.instance.getApiService().getZoneHistroy(pageNo, pageSize)

    }
}