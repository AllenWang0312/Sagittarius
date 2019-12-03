package edu.tjrac.swant.meitu.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import edu.tjrac.swant.baselib.common.base.BaseFragment
import edu.tjrac.swant.meitu.R
import edu.tjrac.swant.meitu.bean.User
import edu.tjrac.swant.meitu.net.BR
import edu.tjrac.swant.meitu.net.NESubscriber
import edu.tjrac.swant.meitu.net.Net
import edu.tjrac.swant.meitu.net.RxUtil
import edu.tjrac.swant.meitu.view.FeedbackListActivity
import edu.tjrac.swant.meitu.view.MeituCollectionsActivity
import edu.tjrac.swant.meitu.view.SettingActivity
import kotlinx.android.synthetic.main.fragment_meitu_mine.*
import kotlinx.android.synthetic.main.fragment_meitu_mine.view.*

/**
 * Created by wpc on 2019-11-13.
 */
class MineFragment : BaseFragment(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fl_down->{
                startActivity(Intent(activity!!, MeituCollectionsActivity::class.java))
            }
            R.id.fl_likes -> {
                startActivity(Intent(activity!!, MeituCollectionsActivity::class.java))
            }
            R.id.fl_feedback -> {
                startActivity(Intent(activity!!, FeedbackListActivity::class.java))
            }
            R.id.fl_setting -> {
                startActivity(Intent(activity!!, SettingActivity::class.java))
            }
//            R.id.iv_cover -> {
//                startActivity(Intent(activity!!, UserInfoActivity::class.java))
//            }
        }
    }

    lateinit var v: View
    override fun onResume() {
        super.onResume()
        Net.instance.getApiService().getUser()
                .compose(RxUtil.applySchedulers())
                .subscribe(object : NESubscriber<BR<User>>(this) {
                    override fun onSuccess(t: BR<User>?) {
                        Glide.with(activity!!).load(t?.data?.portarit)
                                .apply(RequestOptions().circleCrop()).into(iv_cover)
                        tv_name.text = t?.data?.name
                        tv_account.text = t?.data?.account
                    }
                })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_meitu_mine, container, false)
        v?.fl_down.setOnClickListener(this)
        v?.fl_likes.setOnClickListener(this)
        v?.fl_feedback.setOnClickListener(this)
        v?.fl_setting.setOnClickListener(this)
        v?.iv_cover.setOnClickListener(this)
        return v
    }
}