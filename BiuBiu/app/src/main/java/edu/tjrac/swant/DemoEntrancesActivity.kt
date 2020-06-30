package edu.tjrac.swant

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.google.ar.sceneform.samples.solarsystem.SolarActivity
import com.masterwok.demosimpletorrentandroid.activities.TorrentActivity
import edu.tjrac.swant.baselib.util.IntentUtil
import edu.tjrac.swant.fingerprint.FingerPrintBaseActivity
import edu.tjrac.swant.recorder.Camera2VideoActivity
import com.mindorks.tensorflowexample.TensorFlowActivity
import edu.tjrac.swant.vr.VRVideoActivity
import edu.tjrac.swant.webview.ChromeActivity
import edu.tjrac.swant.wjzx.R
import kotlinx.android.synthetic.main.activity_demo_entrances.*

@Route(path = "/app/demo")
class DemoEntrancesActivity : AppCompatActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.bt_file_system -> {
                ARouter.getInstance().build("/app/main")
                        .withInt("fragment", R.string.file_system)
                        .navigation()
            }
            R.id.bt_tf_camera -> {
                startActivity(Intent(this, TensorFlowActivity::class.java))
            }
            R.id.bt_camera2->{
                startActivity(Intent(this, Camera2VideoActivity::class.java))
            }
            R.id.bt_vr->{
                startActivity(Intent(this,VRVideoActivity::class.java))
            }
            R.id.bt_ar_solar->{
                startActivity(Intent(this, SolarActivity::class.java))
            }
            R.id.bt_bluetooth -> {
//                SingleFragmentActivity.start(this, BLEFragment())
//                startActivity(Intent(this,SingleFragmentActivity<BLEFragment>::class.java))
                ARouter.getInstance().build("/app/main")
                        .withInt("fragment", R.string.bluetooth)
                        .navigation()
            }
            R.id.bt_fingerprint -> {
                startActivity(Intent(this, FingerPrintBaseActivity::class.java))
            }



            R.id.bt_web_home -> {
                startActivity(Intent(this, ChromeActivity::class.java)
//                        .putExtra("url", "https://m.fengniao.com/")
                )
            }
            R.id.bt_web_scheme->{
//                startActivity(IntentUtil.openUrlWithSystemChrome("xianzhilms://test.report.com/index.html?lms=3"))
                startActivity(IntentUtil.openUrlWithSystemChrome("xianzhilms://wj.qq.com/s2/5112378/27bc?id=26&type=questionnaire"))
            }
            R.id.bt_torrent_download->{
                startActivity(Intent(this, TorrentActivity::class.java))
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
        bt_camera2?.setOnClickListener(this)
        bt_web_home?.setOnClickListener(this)
        bt_web_scheme?.setOnClickListener(this)
        bt_torrent_download?.setOnClickListener(this)

    }

}
