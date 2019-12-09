package edu.tjrac.swant.wjzx.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import edu.tjrac.swant.baselib.common.base.FragmentActivity
import edu.tjrac.swant.bluetooth.view.BLEFragment
import edu.tjrac.swant.fingerprint.FingerPrintBaseActivity
import edu.tjrac.swant.meitu.MeituLoginActivity
import edu.tjrac.swant.meitu.MeituMainActivity
import edu.tjrac.swant.wjzx.R
import kotlinx.android.synthetic.main.activity_demo_entrances.*

class DemoEntrancesActivity : AppCompatActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.bt_login->{
                startActivity(Intent(this, MeituLoginActivity::class.java))
            }
            R.id.bt_netimg->{
                startActivity(Intent(this,MeituMainActivity::class.java))
            }
            R.id.bt_bluetooth -> {
                FragmentActivity.start(this,BLEFragment())
//                startActivity(Intent(this,FragmentActivity<BLEFragment>::class.java))
            }
            R.id.bt_fingerprint -> {
                startActivity(Intent(this,FingerPrintBaseActivity::class.java))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo_entrances)
        bt_login.setOnClickListener(this)
        bt_bluetooth.setOnClickListener(this)
        bt_fingerprint.setOnClickListener(this)
        bt_netimg.setOnClickListener(this)
    }

}
