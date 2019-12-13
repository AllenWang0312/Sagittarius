package edu.tjrac.swant.image

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.SharedElementCallback
import android.support.v4.view.ViewPager
import android.view.View
import edu.tjrac.swant.baselib.common.base.BaseBarActivity
import edu.tjrac.swant.biubiu.R
import kotlinx.android.synthetic.main.activity_image_preview.*


class ImagePreviewActivity : BaseBarActivity(), View.OnClickListener {

    var images: ArrayList<String>? = null
    var index: Int? = 0
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_left -> {
//                finish()
                finishAfterTransition()
            }
        }
    }

    var adapter: CheckPhotoPagerAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_preview)
        if (null != intent) {
            if (intent.hasExtra("images")) images = intent.getStringArrayListExtra("images")
            if (intent.hasExtra("index")) index = intent.getIntExtra("index", 0)
        }
        setToolbar(findViewById(R.id.toolbar))

        if (null != images && images?.size!! > 0) {
            adapter = CheckPhotoPagerAdapter(this, images)
            vp.adapter = adapter
            vp.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {

                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                }

                override fun onPageSelected(position: Int) {
                    setTitle("" + (position + 1) + "/" + images?.size!!)
                }

            })
            vp.currentItem = index!!
            //图片加载完成的回调中，启动过渡动画
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                startPostponedEnterTransition()
            };
//            getActivity().startPostponedEnterTransition()
//            scheduleStartPostponedTransition()
        }

    }

    var IMG_CURRENT_POSITION = "img_current_position"
    override fun finishAfterTransition() {
        val intent = Intent()
        if (index === vp.currentItem) {
            //没有改变
            intent.putExtra(IMG_CURRENT_POSITION, -1)
        } else {
            intent.putExtra(IMG_CURRENT_POSITION, vp.currentItem)
        }
        setResult(Activity.RESULT_OK, intent)
        super.finishAfterTransition()

    }

    override fun onActivityReenter(resultCode: Int, data: Intent?) {
        if (resultCode === Activity.RESULT_OK && data != null) {
            val exitPos = data.getIntExtra(IMG_CURRENT_POSITION, -1)
            val exitView = adapter?.getView(exitPos)
            if (exitView != null) {
                ActivityCompat.setExitSharedElementCallback(this, object : SharedElementCallback() {
                    override fun onMapSharedElements(names: MutableList<String>?, sharedElements: MutableMap<String, View>?) {
                        names?.clear()
                        sharedElements?.clear()
                        var url=images?.get(exitPos)!!
                        names?.add(url)
                        sharedElements?.put(url,exitView)
                    }
                })
            }
        }
    }

    override fun setToolbar(tool: View) {
        super.setToolbar(tool)
        setLeftIcon(R.drawable.ic_arrow_back_white_24dp, this)
        setTitle("" + (index!! + 1) + "/" + images?.size!!)
    }
}
