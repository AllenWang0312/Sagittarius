package edu.tjrac.swant

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.masterwok.demosimpletorrentandroid.activities.TorrentActivity
import com.tbruyelle.rxpermissions2.RxPermissions
import edu.tjrac.swant.baselib.common.base.SingleFragmentActivity
import edu.tjrac.swant.baselib.util.IntentUtil
import edu.tjrac.swant.bluetooth.view.BLEFragment
import edu.tjrac.swant.recorder.Camera2VideoActivity
import edu.tjrac.swant.webview.ChromeActivity
import edu.tjrac.swant.wjzx.R
import kotlinx.android.synthetic.main.activity_demo_entrances.*

@Route(path = Router.app_demo)
class DemoEntrancesActivity : AppCompatActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.bt_main -> {
                ARouter.getInstance().build(Router.app_main).navigation()
            }

            R.id.bt_file_system -> {
                val rxPermission = RxPermissions(this!!)
                rxPermission.requestEach(Manifest.permission.READ_EXTERNAL_STORAGE).subscribe { t ->
                        if (t?.granted!!) {
                            ARouter.getInstance().build(Router.file_main).navigation()
                        } else if (t.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                            // Log.d(TAG, permission.name + " is denied. More info should be provided.")
                        } else {
                            // 用户拒绝了该权限，并且选中『不再询问』
                            // Log.d(TAG, permission.name + " is denied.")
                        }
                    }

            }
            R.id.bt_torrent_download -> {
                startActivity(Intent(this, TorrentActivity::class.java))
            }
            R.id.bt_tf_camera -> {
                //                startActivity(Intent(this, TensorFlowActivity::class.java))
                ARouter.getInstance().build(Router.tensorflow).navigation()
            }
            R.id.bt_camera2 -> {
                startActivity(Intent(this, Camera2VideoActivity::class.java))
            }
            R.id.bt_vr -> {
                //                startActivity(Intent(this,VRVideoActivity::class.java))
                ARouter.getInstance().build(Router.vr_player).navigation()
            }
            R.id.bt_ar_solar -> {
                //                startActivity(Intent(this, SolarActivity::class.java))
                ARouter.getInstance().build(Router.ar_solar).navigation()
            }
            R.id.bt_bluetooth -> {
                SingleFragmentActivity.start(this, BLEFragment())
                //                ARouter.getInstance().build("/ble/main")
                //                        .withInt("fragment", R.string.bluetooth)
                //                        .navigation()
            }
            R.id.bt_fingerprint -> {
                //                startActivity(Intent(this, FingerPrintBaseActivity::class.java))
                ARouter.getInstance().build(Router.finger_main)
                    //                    .withInt("fragment", R.string.bluetooth)
                    .navigation()
            }

            R.id.bt_smb -> {
                ARouter.getInstance().build(Router.samba_main).navigation()
            }

            R.id.bt_web_home -> {
                startActivity(Intent(this, ChromeActivity::class.java)
                    //                        .putExtra("url", "https://m.fengniao.com/")
                )
            }
            R.id.bt_web_scheme -> {
                //                startActivity(IntentUtil.openUrlWithSystemChrome("xianzhilms://test.report.com/index.html?lms=3"))
                startActivity(IntentUtil.openUrlWithSystemChrome("xianzhilms://wj.qq.com/s2/5112378/27bc?id=26&type=questionnaire"))
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo_entrances)

        bt_bluetooth?.setOnClickListener(this)
        bt_fingerprint?.setOnClickListener(this)
        bt_tf_camera?.setOnClickListener(this)
        bt_vr?.setOnClickListener(this)
        bt_ar_solar?.setOnClickListener(this)
        bt_file_system?.setOnClickListener(this)
        bt_smb?.setOnClickListener(this)
        bt_camera2?.setOnClickListener(this)
        bt_web_home?.setOnClickListener(this)
        bt_web_scheme?.setOnClickListener(this)
        bt_torrent_download?.setOnClickListener(this)

    }

}
