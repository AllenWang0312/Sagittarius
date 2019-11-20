package edu.tjrac.swant.meitu.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import edu.tjrac.swant.baselib.common.base.BaseFragment
import edu.tjrac.swant.meitu.R
import edu.tjrac.swant.meitu.view.FeedbackListActivity
import edu.tjrac.swant.meitu.view.MeituCollectionsActivity
import kotlinx.android.synthetic.main.fragment_meitu_mine.view.*

/**
 * Created by wpc on 2019-11-13.
 */
class MineFragment : BaseFragment(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fl_likes -> {
                startActivity(Intent(activity!!, MeituCollectionsActivity::class.java))
            }
            R.id.fl_feedback -> {
                startActivity(Intent(activity!!, FeedbackListActivity::class.java))
            }
        }
    }

    lateinit var v: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_meitu_mine, container, false)
        v?.fl_likes.setOnClickListener(this)
        v?.fl_feedback.setOnClickListener(this)
        return v
    }
}