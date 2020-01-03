package edu.tjrac.swant.wjzx.aidl

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import edu.tjrac.swant.wjzx.R
import edu.tjrac.swant.wjzx.aidl.bean.IRemoteService
import edu.tjrac.swant.wjzx.aidl.bean.IRemoteServiceCallback
import edu.tjrac.swant.wjzx.aidl.bean.ISecondary
import kotlinx.android.synthetic.main.activity_aidltest.*
import java.lang.ref.WeakReference

val BUMP_MSG = 1


class AIDLTestActivity : AppCompatActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.kill_button -> {
            }
        }
    }

    var mService: IRemoteService? = null
    var secondaryService: ISecondary? = null
    private var handler: InternalHandler? = null

    var mCallback: IRemoteServiceCallback = object : IRemoteServiceCallback.Stub() {

        override fun valueChanged(value: Int) {
            handler?.sendMessage(handler?.obtainMessage(BUMP_MSG, value, 0))
        }

    }
    var mConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mService = IRemoteService.Stub.asInterface(service)
            kill_button.isEnabled = true
            tv_callback.text = "attached"
            try {
                mService?.registerCallback(mCallback)
            } catch (e: Exception) {

            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mService = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aidltest)
        kill_button.isEnabled = false
        kill_button?.setOnClickListener(this)
        tv_callback.text = "Not attached."
        handler = InternalHandler(tv_callback)

    }

    private class InternalHandler(
            textView: TextView,
            private val weakTextView: WeakReference<TextView> = WeakReference(textView)
    ) : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                BUMP_MSG -> weakTextView.get()?.text = "Received from service: ${msg.arg1}"
                else -> super.handleMessage(msg)
            }
        }
    }
}
