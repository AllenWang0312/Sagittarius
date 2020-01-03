package edu.tjrac.swant.biubiu_launcher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * Created by wpc on 2020-01-03.
 */
class FuYiFragment : Fragment() {
    var v: View?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v=inflater.inflate(R.layout.fragment_fuyi,container,false)
        return v
    }
}
