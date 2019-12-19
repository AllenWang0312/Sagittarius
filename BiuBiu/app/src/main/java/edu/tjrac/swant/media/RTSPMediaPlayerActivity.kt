package edu.tjrac.swant.media

//import kotlinx.android.synthetic.main.activity_simple_media_player.*
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import edu.tjrac.swant.baselib.util.FileUtils
import edu.tjrac.swant.baselib.util.SUtil
import edu.tjrac.swant.baselib.util.T
import edu.tjrac.swant.media.rtsp.TU
import edu.tjrac.swant.biubiu.net.BR
import edu.tjrac.swant.Config
import edu.tjrac.swant.wjzx.R

class RTSPMediaPlayerActivity : AppCompatActivity() {

    var playlist: ArrayList<TU>? = null

    var title = ""
    //    String path="https:// video-xz.veimg.cn/7/286/pc_hd/7_286OpMFL.mp4";
    var path = ""
        set(value) {
            if (!SUtil.isEmpty(value)) {
                field = value
                sp?.edit()?.putString(Config.SP.CACHE_URL, value)?.commit()
            }
        }
        get() {
            if (SUtil.isEmpty(field)) {
                field = sp?.getString(Config.SP.CACHE_URL, "http://ivi.bupt.edu.cn/hls/cctv1.m3u8")!!
            }
            return field
        }
    var index = 0
        get() {
            if (null != playlist) {
                for (i in 0 until playlist?.size!!) {
                    if (playlist?.get(i)?.url.equals(path)) {
                        return i
                    }
                }
            }
            return 0
        }
    var sp: SharedPreferences? = null

    fun onGetPlayListSuccess(data: ArrayList<TU>) {
        playlist = data
//        if (SUtil.isEmpty(path)) {
//            path = data.get(0).url!!
//        }
    }

    override fun onResume() {
        super.onResume()
        try {
            var testData = FileUtils.getFromAssets(this, "playlist.json")
            var bean = Gson().fromJson<BR<ArrayList<TU>>>(testData, object : TypeToken<BR<ArrayList<TU>>>() {}.type)
            onGetPlayListSuccess(bean.data!!)
        } catch (e: Exception) {
            T.show(e.toString())
        }
//        simple_player.setVideoPath(path)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
//            IjkMediaPlayer.loadLibrariesOnce(null)
//            IjkMediaPlayer.native_profileBegin("libijkplayer.so")
        } catch (e: Exception) {
            this.finish()
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_media_player)
        sp = getSharedPreferences(Config.SP.CACHE, Context.MODE_PRIVATE)

//        var audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
//        var name = ComponentName(this, MediaKeyReceiver::class.java)
//        audioManager.registerMediaButtonEventReceiver(name)

//        simple_player.setListener(object : VideoPlayerListener() {
//            override fun onBufferingUpdate(iMediaPlayer: IMediaPlayer, i: Int) {}
//
//            override fun onCompletion(iMediaPlayer: IMediaPlayer) {}
//
//            override fun onError(iMediaPlayer: IMediaPlayer, i: Int, i1: Int): Boolean {
//                return false
//            }
//
//            override fun onInfo(iMediaPlayer: IMediaPlayer, i: Int, i1: Int): Boolean {
//                return false
//            }
//
//            override fun onPrepared(iMediaPlayer: IMediaPlayer) {
//                iMediaPlayer.start()
//            }
//
//            override fun onSeekComplete(iMediaPlayer: IMediaPlayer) {}
//
//            override fun onVideoSizeChanged(iMediaPlayer: IMediaPlayer, i: Int, i1: Int, i2: Int, i3: Int) {}
//        })
    }


    //    @Override
    //    protected void onStop() {
    //        super.onStop();
    //        //点击返回或不允许后台播放时 释放资源
    //        if (mBackPressed || !player.isBackgroundPlayEnabled()) {
    //            player.stopPlayback();
    //            player.release(true);
    //            player.stopBackgroundPlay();
    //        } else {
    //            mVideoView.enterBackground();
    //        }
    //        IjkMediaPlayer.native_profileEnd();
    //    }
}
