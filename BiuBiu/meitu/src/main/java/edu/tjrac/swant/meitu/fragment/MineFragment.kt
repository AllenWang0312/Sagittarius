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
import edu.tjrac.swant.meitu.bean.stuct.UserCenter
import edu.tjrac.swant.meitu.net.BR
import edu.tjrac.swant.meitu.net.NESubscriber
import edu.tjrac.swant.meitu.net.Net
import edu.tjrac.swant.meitu.net.RxUtil
import edu.tjrac.swant.meitu.view.mine.FeedbackListActivity
import edu.tjrac.swant.meitu.view.mine.MeituCollectionsActivity
import edu.tjrac.swant.meitu.view.mine.MeituVisitHistroyActivity
import edu.tjrac.swant.meitu.view.mine.SettingActivity
import kotlinx.android.synthetic.main.fragment_meitu_mine.*
import kotlinx.android.synthetic.main.fragment_meitu_mine.view.*

class MineFragment : BaseFragment(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fl_visit -> {
                startActivity(Intent(activity!!, MeituVisitHistroyActivity::class.java))
            }
            R.id.tv_focus -> {
                startActivity(Intent(activity!!, MeituCollectionsActivity::class.java)
                        .putExtra("title", "我的关注"))
            }
            R.id.fl_likes -> {
                startActivity(Intent(activity!!, MeituCollectionsActivity::class.java))
            }
            R.id.fl_feedback -> {
                startActivity(Intent(activity!!, FeedbackListActivity::class.java))
            }
            R.id.iv_setting -> {
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
        Net.instance.getApiService().getUserCenter()
                .compose(RxUtil.applySchedulers())
                .subscribe(object : NESubscriber<BR<UserCenter>>(this) {
                    override fun onSuccess(t: BR<UserCenter>?) {
                        var user = t?.data?.user
                        Glide.with(activity!!).load(user?.portarit)
                                .apply(RequestOptions().circleCrop()).into(iv_cover)
                        tv_name.text = user?.name
                        tv_account.text = user?.account
                    }
                })
    }

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