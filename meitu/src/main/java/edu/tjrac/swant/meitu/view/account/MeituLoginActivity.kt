package edu.tjrac.swant.meitu.view.account

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import edu.tjrac.swant.baselib.common.adapter.ViewsPagerAdapter
import edu.tjrac.swant.baselib.common.base.BaseActivity
import edu.tjrac.swant.baselib.util.StringUtils
import edu.tjrac.swant.meitu.App
import edu.tjrac.swant.meitu.Config
import edu.tjrac.swant.meitu.MeituMainActivity
import edu.tjrac.swant.meitu.R
import edu.tjrac.swant.meitu.bean.stuct.LoginRespon
import edu.tjrac.swant.meitu.net.BR
import edu.tjrac.swant.meitu.net.NESubscriber
import edu.tjrac.swant.meitu.net.Net
import edu.tjrac.swant.meitu.net.RxUtil
import kotlinx.android.synthetic.main.activity_meitu_login.*
import kotlinx.android.synthetic.main.page_login_login.view.*
import kotlinx.android.synthetic.main.page_login_login.view.et_account
import kotlinx.android.synthetic.main.page_login_login.view.et_pass
import kotlinx.android.synthetic.main.page_login_regist.view.*

class MeituLoginActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_visit -> {
                startActivity(Intent(this, MeituMainActivity::class.java))
            }
            R.id.tv_regist -> {
                vp_login.currentItem = 1
            }
            R.id.bt_login -> {
                var account = login?.et_account?.text.toString()
                var pass = login?.et_pass?.text.toString()
                if (StringUtils.isEmpty(account) || StringUtils.isEmpty(pass)) {

                } else {
                    App.sp?.edit()?.putString(Config.SP.LOGIN_ACCOUNT, account)
                            ?.putBoolean(Config.SP.LOGIN_REMENBER_PASS, login?.cb_remember?.isChecked!!)
                            ?.putString(Config.SP.LOGIN_PASS, pass)?.commit()

                    Net.instance.getApiService().login(account, pass, App.device!!)
                            .compose(RxUtil.applySchedulers())
                            .subscribe(object : NESubscriber<BR<LoginRespon>>(this) {
                                override fun onSuccess(t: BR<LoginRespon>?) {
                                    onLoginSuccess(this@MeituLoginActivity, t?.data)
                                }
                            })
                }
            }
            R.id.bt_register -> {
                var account = regist?.et_account?.text.toString()
                var pass = regist?.et_pass?.text.toString()
                var repass = regist?.et_repass?.text.toString()

                if (StringUtils.isEmpty(account) || StringUtils.isEmpty(pass) || StringUtils.isEmpty(repass)) {

                } else {
                    if (pass.equals(repass)) {
                        Net.instance.getApiService().register(account, pass)
                                .compose(RxUtil.applySchedulers())
                                .subscribe(object : NESubscriber<BR<Any>>(this) {
                                    override fun onSuccess(t: BR<Any>?) {
                                        vp_login.currentItem = 0
                                        login?.et_account?.setText("")
                                        login?.et_pass?.setText("")
                                    }
                                })
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        var account = App.sp?.getString(Config.SP.LOGIN_ACCOUNT, "")
        login?.et_account?.setText(account!!)
        var remember = App.sp?.getBoolean(Config.SP.LOGIN_REMENBER_PASS, false)
        if (remember!!) {
            var pass = App.sp?.getString(Config.SP.LOGIN_PASS, "")
            login?.et_pass?.setText(pass)
        }
    }

    var login: View? = null
    var regist: View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meitu_login)
        tv_visit.setOnClickListener(this)
        login = layoutInflater.inflate(R.layout.page_login_login, null)
        login?.tv_regist?.setOnClickListener(this)
        login?.bt_login?.setOnClickListener(this)

        regist = layoutInflater.inflate(R.layout.page_login_regist, null)
        regist?.bt_register?.setOnClickListener(this)
        var adapter = ViewsPagerAdapter()
        adapter.addView(login!!, "login")
        adapter.addView(regist!!, "regist")

        vp_login.adapter = adapter

    }

    companion object {
        fun onLoginSuccess(context: Activity, data: LoginRespon?) {
            if (!StringUtils.isEmpty(data?.user?.token)) {
                App.token = data?.user?.token
            }
            App.loged = data?.user

            if (data?.has_tab != true) {
                context.startActivity(Intent(context, MeituSelectTabActivity::class.java))
            } else {
                context.startActivity(Intent(context, MeituMainActivity::class.java)
                        .putExtra("bind", data?.has_bind))
            }
            context.finish()
        }
    }


}
