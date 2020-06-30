package edu.tjrac.swant.vr

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.telecom.VideoProfile.isPaused
import androidx.appcompat.app.AppCompatActivity
import com.google.vr.sdk.widgets.video.VrVideoEventListener
import com.google.vr.sdk.widgets.video.VrVideoView
import kotlinx.android.synthetic.main.activity_v_r_video.*
import java.io.IOException


class VRVideoActivity : AppCompatActivity() {
    internal class ActivityEventListener : VrVideoEventListener() {
        override fun onLoadSuccess() { //加载成功
//            Log.i(TAG, "Sucessfully loaded video " + videoWidgetView.getDuration())
        }

        override fun onLoadError(errorMessage: String) { //加载失败
//            Log.e(TAG, "Error loading video: $errorMessage")
        }

        override fun onClick() { //当我们点击了VrVideoView时候触发
//            togglePause()
        }

        override fun onNewFrame() { //一个新的帧被绘制到屏幕上。
        }

        override fun onCompletion() { //视频播放完毕。
//            videoWidgetView.seekTo(0) //移动到视频开始
        }
    }

//    internal class VideoLoaderTask : AsyncTask<String?, Void?, Boolean?>() {
//        protected fun doInBackground(vararg uri: String?): Boolean {
//            try {
//                videoWidgetView.loadVideoFromAsset(uri[0]) //加载视频文件
//            } catch (e: IOException) { //视频文件打开失败
//                Log.e(FragmentActivity.TAG, "Could not open video: $e")
//            }
//            return true
//        }
//    }
//
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_v_r_video)
        var url = intent.data
        if(null!=url){
           playUri(url)
        }
        video?.setEventListener(ActivityEventListener())
        bt?.setOnClickListener{
            choiceVideo()
        }
    }

    override fun onPause() {
        super.onPause()
        video.pauseRendering() //暂停3D渲染和跟踪
//        isPaused = true
    }

    override fun onResume() {
        super.onResume()
        video.resumeRendering() //恢复3D渲染和跟踪，但官方文档上面没有写
    }

    override fun onDestroy() {
        video.shutdown() //关闭渲染并释放相关的内存
        super.onDestroy()
    }
    private fun playUri(uri: Uri) {
        var options = VrVideoView.Options()
        try {
            video.loadVideo(uri, options)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun choiceVideo() {
        val i = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
//        val i = Intent(Intent.ACTION_PICK)
//        i.type="video/*"
        startActivityForResult(i, 66)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 66 && resultCode == Activity.RESULT_OK && null != data) {
            val selectedVideo: Uri? = data.data
            if(null!=selectedVideo){
                playUri(selectedVideo)
            }
//            val filePathColumn = arrayOf(MediaStore.Video.Media.DATA)
//            val cursor: Cursor? = contentResolver.query(selectedVideo, filePathColumn, null, null, null)
//            cursor.moveToFirst()
//            val columnIndex: Int = cursor.getColumnIndex(filePathColumn[0])
//            VIDEOPATH = cursor.getString(columnIndex)
//            cursor.close()
//            submit_vd_ad.setText(VIDEOPATH)
        }
        if (resultCode != Activity.RESULT_OK) {
            return
        }
    }
}