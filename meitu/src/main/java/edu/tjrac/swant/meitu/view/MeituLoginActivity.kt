package edu.tjrac.swant.meitu.view

import android.content.Intent
import android.os.Bundle
import edu.tjrac.swant.baselib.common.adapter.ViewsPagerAdapter
import edu.tjrac.swant.baselib.common.base.BaseActivity
import edu.tjrac.swant.baselib.util.StringUtils
import edu.tjrac.swant.meitu.App
import edu.tjrac.swant.meitu.R
import edu.tjrac.swant.meitu.bean.User
import edu.tjrac.swant.meitu.net.BR
import edu.tjrac.swant.meitu.net.NESubscriber
import edu.tjrac.swant.meitu.net.Net
import edu.tjrac.swant.meitu.net.RxUtil
import kotlinx.android.synthetic.main.activity_meitu_login.*
import kotlinx.android.synthetic.main.page_login_login.view.*
import kotlinx.android.synthetic.main.page_login_login.view.et_account
import kotlinx.android.synthetic.main.page_login_login.view.et_pass
import kotlinx.android.synthetic.main.page_login_regist.view.*

class MeituLoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meitu_login)

        var login = layoutInflater.inflate(R.layout.page_login_login, null)
        login.bt_login.setOnClickListener {
            var account = login.et_account.text.toString()
            var pass = login.et_pass.text.toString()
            if (StringUtils.isEmpty(account) || StringUtils.isEmpty(pass)) {

            } else {
                Net.instance.getApiService().login(account, pass,App.device)
                        .compose(RxUtil.applySchedulers())
                        .subscribe(object : NESubscriber<BR<User>>(this) {
                            override fun onSuccess(t: BR<User>?) {
                                login.bt_login.post {
                                    App.token = t?.data?.token
                                    App.loged = t?.data
                                    startActivity(Intent(this@MeituLoginActivity, MeituMainActivity::class.java))
                                    finish()
                                }
                            }
                        })
            }
        }
        var regist = layoutInflater.inflate(R.layout.page_login_regist, null)
        regist.bt_register.setOnClickListener {
            var account = regist.et_account.text.toString()
            var pass = regist.et_pass.text.toString()
            var repass = regist.et_repass.text.toString()

            if (StringUtils.isEmpty(account) || StringUtils.isEmpty(pass) || StringUtils.isEmpty(repass)) {

            } else {
                if (pass.equals(repass)) {
                    Net.instance.getApiService().register(account, pass)
                            .compose(RxUtil.applySchedulers())
                            .subscribe(object : NESubscriber<BR<Any>>(this) {
                                override fun onSuccess(t: BR<Any>?) {

                                }
                            })
                }
            }
        }
        var adapter = ViewsPagerAdapter()
        adapter.addView(login, "login")
        adapter.addView(regist, "regist")

        vp_login.adapter = adapter

    }

}
