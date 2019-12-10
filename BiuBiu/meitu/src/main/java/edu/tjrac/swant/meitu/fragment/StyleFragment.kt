package edu.tjrac.swant.meitu.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import edu.tjrac.swant.baselib.common.base.BaseFragment
import edu.tjrac.swant.meitu.R

/**
 * Created by wpc on 2019-12-10.
 */
class StyleFragment : BaseFragment() {
    var v: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_style, container, false)
        return v
    }
}