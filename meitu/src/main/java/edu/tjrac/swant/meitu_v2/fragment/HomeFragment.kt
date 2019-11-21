package edu.tjrac.swant.meitu_v2.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import edu.tjrac.swant.baselib.common.base.BaseFragment
import edu.tjrac.swant.meitu.R

/**
 * Created by wpc on 2019-11-21.
 */
class HomeFragment:BaseFragment(){

    var head:View?=null
    var v:View?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v=layoutInflater.inflate(R.layout.meitu_home_fragment,container,false)
        head=layoutInflater.inflate(R.layout.meitu_home_head,null)
        return v
    }
}