package edu.tjrac.swant.meitu.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import edu.tjrac.swant.baselib.common.base.BaseFragment
import edu.tjrac.swant.meitu.R
import kotlinx.android.synthetic.main.fragment_meitu_mine.view.*

class MineFragment : BaseFragment(), View.OnClickListener {
    override fun onClick(v: View?) {

    }

    lateinit var v: View


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_meitu_mine, container, false)
//        v?.tv_visit.setOnClickListener(this)
        v?.tv_focus.setOnClickListener(this)
        v?.fl_visit.setOnClickListener(this)
        v?.fl_likes.setOnClickListener(this)
        v?.fl_feedback.setOnClickListener(this)
        v?.iv_setting.setOnClickListener(this)
        v?.iv_cover.setOnClickListener(this)
        return v
    }
}