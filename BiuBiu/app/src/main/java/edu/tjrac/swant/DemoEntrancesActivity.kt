package edu.tjrac.swant

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import edu.tjrac.swant.fingerprint.FingerPrintBaseActivity
import edu.tjrac.swant.tensorflow.TensorFlowActivity
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
            R.id.bt_ar_solar -> {

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
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo_entrances)
        bt_bluetooth.setOnClickListener(this)
        bt_fingerprint.setOnClickListener(this)
        bt_tf_camera?.setOnClickListener(this)
        bt_ar_solar?.setOnClickListener(this)
        bt_file_system?.setOnClickListener(this)
    }

}
