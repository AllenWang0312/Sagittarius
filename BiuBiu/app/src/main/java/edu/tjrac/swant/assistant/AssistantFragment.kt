package edu.tjrac.swant.assistant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import edu.tjrac.swant.baselib.common.base.BaseFragment
import edu.tjrac.swant.wjzx.R

/**
 * Created by wpc on 2019-08-21.
 */
class AssistantFragment : BaseFragment() {
    var v: View? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_assistant, container, false)
        return v
    }
}