package edu.tjrac.swant

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import edu.tjrac.swant.baselib.common.base.SingleFragmentActivity
import edu.tjrac.swant.bluetooth.view.BLEFragment
import edu.tjrac.swant.fingerprint.FingerPrintBaseActivity
import edu.tjrac.swant.wjzx.R
import kotlinx.android.synthetic.main.activity_demo_entrances.*

@Route(path = "/demo", group = "app")
class DemoEntrancesActivity : AppCompatActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.bt_bluetooth -> {
                SingleFragmentActivity.start(this, BLEFragment())
//                startActivity(Intent(this,SingleFragmentActivity<BLEFragment>::class.java))
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
    }

}
