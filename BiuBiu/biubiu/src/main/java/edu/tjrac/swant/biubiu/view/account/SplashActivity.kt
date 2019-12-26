package edu.tjrac.swant.biubiu.view.account

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.tbruyelle.rxpermissions2.RxPermissions
import edu.tjrac.swant.baselib.common.base.BaseActivity
import edu.tjrac.swant.baselib.util.SUtil
import edu.tjrac.swant.biubiu.BiuBiuApp
import edu.tjrac.swant.biubiu.Config
import edu.tjrac.swant.biubiu.MeituMainActivity
import edu.tjrac.swant.biubiu.R
import edu.tjrac.swant.biubiu.bean.Banner
import edu.tjrac.swant.biubiu.bean.stuct.LoginRespon
import edu.tjrac.swant.biubiu.net.BR
import edu.tjrac.swant.biubiu.net.NESubscriber
import edu.tjrac.swant.biubiu.net.Net
import edu.tjrac.swant.biubiu.net.RxUtil
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.*

class SplashActivity : BaseActivity() {
    var sp: SharedPreferences? = null
    var waitTime =3;

    var timer = Timer()
    override fun onResume() {
        super.onResume()
        if (sp?.getBoolean(Config.SP.FIRST_TIME, true)!!) {
            sp?.edit()?.putBoolean(Config.SP.FIRST_TIME, false)?.commit()
            val rxPermission = RxPermissions(this@SplashActivity)
            rxPermission
                    .requestEach(
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.READ_PHONE_STATE
//                    Manifest.permission.CAMERA,
//                    Manifest.permission.CALL_PHONE
                    )
                    .subscribe { t ->
                        if (t!!.granted) {
                            Log.i("granted", t.name)
                            //                                ARouter.getSInstance().build(Config_v4.PATH.ACTIVITY_LOGIN).navigation()
                        } else if (t.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                            //                                    Log.d(TAG, permission.name + " is denied. More info should be provided.")
                        } else {
                            // 用户拒绝了该权限，并且选中『不再询问』
                            //                                    Log.d(TAG, permission.name + " is denied.")
                        }
                    }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        if (BuildConfig.DEBUG) {
//            finish()
//            startActivity(Intent(this, RTSPMediaPlayerActivity::class.java))
//            return
//        }

        sp = getSharedPreferences(Config.SP.CACHE, Context.MODE_PRIVATE)
        setContentView(R.layout.activity_splash)

        tv_skip.setOnClickListener {
            jump()
        }
//        bt.setOnClickListener {
//            jump()
//        }
        if (BiuBiuApp.loged != null) {
            iv_cover.visibility = View.VISIBLE
            tv_name.visibility = View.VISIBLE
            Glide.with(this).load(BiuBiuApp.loged?.portarit)
                    .apply(RequestOptions().circleCrop()).into(iv_cover)
            tv_name.text = BiuBiuApp.loged?.name
        }
        if (!SUtil.isEmpty(BiuBiuApp.token)) {
            Net.instance.getApiService().getSplashInfo()
                    .compose(RxUtil.applySchedulers())
                    .subscribe(object : NESubscriber<BR<ArrayList<Banner>>>(this) {
                        override fun onSuccess(t: BR<ArrayList<Banner>>?) {
                            for (i in t?.data!!) {
                                var now = System.currentTimeMillis()
                                if (now > i.getStartTime() && now < i.getEndTime()) {
//                                    Log.i("splash",i.src)
                                    Glide.with(this@SplashActivity).load(i.src).into(iv)
                                    break
                                }
                            }
                        }
                    })
            Net.instance.getApiService().tokenLogin(BiuBiuApp.device!!)
                    .compose(RxUtil.applySchedulers())
                    .subscribe(object : NESubscriber<BR<LoginRespon>>(this) {
                        override fun onSuccess(t: BR<LoginRespon>?) {
//                            BiuBiuApp.loged = t?.data
//                            next = Intent(this@SplashActivity, MeituMainActivity::class.java)
//                            MeituLoginActivity.onLoginSuccess(this@SplashActivity, t?.data)
                            if (!SUtil.isEmpty(t?.data?.user?.token)) {
                                BiuBiuApp.token = t?.data?.user?.token
                            }
                            BiuBiuApp.loged = t?.data?.user

                            if (t?.data?.has_tab != true) {
                                next = Intent(this@SplashActivity, MeituSelectTabActivity::class.java)
                            } else {
                                next = Intent(this@SplashActivity, MeituMainActivity::class.java)
                                        .putExtra("bind", t?.data?.has_bind)
                            }
                        }

                        override fun onError(e: Throwable?) {
                            super.onError(e)
                            next = Intent(this@SplashActivity, MeituMainActivity::class.java)
                        }
                    })
        }


        timer?.schedule(object : TimerTask() {
            override fun run() {
                waitTime--
                tv_skip.post {
                    tv_skip.text = "跳过($waitTime)"
                    if (waitTime == 0) {
                        jump()
                        timer.cancel()
                    }
                }
            }
        }, 1000, 1000)
    }

    var next: Intent? = null
        get() {
            if (field == null) {
                field = Intent(this@SplashActivity, MeituLoginActivity::class.java)
            }
            return field
        }

    private fun jump() {
        finish()
        startActivity(next)
    }
}
