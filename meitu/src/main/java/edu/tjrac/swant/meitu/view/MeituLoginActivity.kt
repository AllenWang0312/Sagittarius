package edu.tjrac.swant.meitu.view

import android.content.Intent
import android.os.Bundle
import edu.tjrac.swant.baselib.common.base.BaseActivity
import edu.tjrac.swant.baselib.util.StringUtils
import edu.tjrac.swant.meitu.App
import edu.tjrac.swant.meitu.R
import edu.tjrac.swant.meitu.bean.User
import edu.tjrac.swant.meitu.net.BR
import edu.tjrac.swant.meitu.net.NESubscriber
import edu.tjrac.swant.meitu.net.Net
import kotlinx.android.synthetic.main.activity_meitu_login.*

class MeituLoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meitu_login)

        bt_login.setOnClickListener {
            var account = et_account.text.toString()
            var pass = et_pass.text.toString()
            if (StringUtils.isEmpty(account) || StringUtils.isEmpty(pass)) {

            } else {
                Net.instance.getApiService().login(account, pass)
                        .compose(edu.tjrac.swant.meitu.net.RxUtil.applySchedulers())
                        .subscribe(object : NESubscriber<BR<User>>(this) {
                            override fun onSuccess(t: BR<User>?) {
                                bt_login.post {
                                    App.token = t?.data?.token
                                    App.loged = t?.data
                                    startActivity(Intent(this@MeituLoginActivity, MeituMainActivity::class.java))
                                    finish()
                                }
                            }
                        })
            }
        }
    }

}
