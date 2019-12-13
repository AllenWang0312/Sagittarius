package edu.tjrac.swant.biubiu.view.phone

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import edu.tjrac.swant.baselib.common.base.BaseFragment
import edu.tjrac.swant.biubiu.R

/**
 * Created by wpc on 2019-12-12.
 */
class PhoneFragment() : BaseFragment() {
    var type: String? = null

    @SuppressLint("ValidFragment")
    constructor(tag: String) : this() {
        this.type = tag
    }

    var v: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_main, container, false)

        return v

    }
}