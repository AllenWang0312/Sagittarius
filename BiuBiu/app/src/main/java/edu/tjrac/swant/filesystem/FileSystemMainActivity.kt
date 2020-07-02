package edu.tjrac.swant.filesystem

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import edu.tjrac.swant.baselib.common.base.BaseActivity
import edu.tjrac.swant.wjzx.R
import kotlinx.android.synthetic.main.activity_file_system_main.*

@Route(path = "/file/main")
class FileSystemMainActivity : BaseActivity() {
    //    var content:FrameLayout?=null
    var gallery: GalleryFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_system_main)
        setSupportActionBar(toolbar)

        gallery = GalleryFragment()
        supportFragmentManager.beginTransaction().add(gallery!!, "gallery")
            .replace(R.id.content, gallery!!).commit()

    }

    override fun onBackPressed() {

        if (gallery?.backable!!) {
            gallery?.onBack()
        } else {
            super.onBackPressed()
        }
    }

}
