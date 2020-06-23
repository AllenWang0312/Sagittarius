package edu.tjrac.swant.fingerprint

import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import edu.tjrac.swant.fingerprint.core.FingerprintCore
import edu.tjrac.swant.fingerprint.core.FingerprintCore.IFingerprintResultListener
import edu.tjrac.swant.wjzx.R

@TargetApi(Build.VERSION_CODES.M)
class FingerPrintBaseActivity : AppCompatActivity(), View.OnClickListener {
    private var mFingerprintCore: FingerprintCore? = null
    private var mKeyguardLockScreenManager: KeyguardLockScreenManager? = null
    private var mToast: Toast? = null
    private val mHandler = Handler(Looper.getMainLooper())
    private var mFingerGuideImg: ImageView? = null
    private var mFingerGuideTxt: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fingerprint_main)
        initViews()
        initViewListeners()
        initFingerprintCore()
    }

    private fun initFingerprintCore() {
        mFingerprintCore = FingerprintCore(this)
        mFingerprintCore!!.setFingerprintManager(mResultListener!!)
        mKeyguardLockScreenManager = KeyguardLockScreenManager(this)
    }

    private fun initViews() {
        mFingerGuideImg = findViewById<View>(R.id.fingerprint_guide) as ImageView
        mFingerGuideTxt = findViewById<View>(R.id.fingerprint_guide_tip) as TextView
    }

    private fun initViewListeners() {
        findViewById<View>(R.id.fingerprint_recognition_start).setOnClickListener(this)
        findViewById<View>(R.id.fingerprint_recognition_cancel).setOnClickListener(this)
        findViewById<View>(R.id.fingerprint_recognition_sys_unlock).setOnClickListener(this)
        findViewById<View>(R.id.fingerprint_recognition_sys_setting).setOnClickListener(this)
    }

    override fun onClick(v: View) {
        val viewId = v.id
        when (viewId) {
            R.id.fingerprint_recognition_start -> startFingerprintRecognition()
            R.id.fingerprint_recognition_cancel -> cancelFingerprintRecognition()
            R.id.fingerprint_recognition_sys_unlock -> startFingerprintRecognitionUnlockScreen()
            R.id.fingerprint_recognition_sys_setting -> enterSysFingerprintSettingPage()
        }
    }

    private fun enterSysFingerprintSettingPage() {
        FingerprintUtil.openFingerPrintSettingPage(this)
    }

    private fun cancelFingerprintRecognition() {
        if (mFingerprintCore!!.isAuthenticating) {
            mFingerprintCore!!.cancelAuthenticate()
            resetGuideViewState()
        }
    }

    private fun startFingerprintRecognitionUnlockScreen() {
        if (mKeyguardLockScreenManager == null) {
            return
        }
        if (!mKeyguardLockScreenManager!!.isOpenLockScreenPwd) {
            toastTipMsg(R.string.fingerprint_not_set_unlock_screen_pws)
            FingerprintUtil.openFingerPrintSettingPage(this)
            return
        }
        mKeyguardLockScreenManager!!.showAuthenticationScreen(this)
    }

    /**
     * 开始指纹识别
     */
    private fun startFingerprintRecognition() {
        if (mFingerprintCore!!.isSupport) {
            if (!mFingerprintCore!!.isHasEnrolledFingerprints) {
                toastTipMsg(R.string.fingerprint_recognition_not_enrolled)
                FingerprintUtil.openFingerPrintSettingPage(this)
                return
            }
            toastTipMsg(R.string.fingerprint_recognition_tip)
            mFingerGuideTxt!!.setText(R.string.fingerprint_recognition_tip)
            mFingerGuideImg!!.setBackgroundResource(R.drawable.fingerprint_guide)
            if (mFingerprintCore!!.isAuthenticating) {
                toastTipMsg(R.string.fingerprint_recognition_authenticating)
            } else {
                mFingerprintCore!!.startAuthenticate()
            }
        } else {
            toastTipMsg(R.string.fingerprint_recognition_not_support)
            mFingerGuideTxt!!.setText(R.string.fingerprint_recognition_tip)
        }
    }

    private fun resetGuideViewState() {
        mFingerGuideTxt!!.setText(R.string.fingerprint_recognition_guide_tip)
        mFingerGuideImg!!.setBackgroundResource(R.drawable.fingerprint_normal)
    }

    private var mResultListener: IFingerprintResultListener? = object : IFingerprintResultListener {
        override fun onAuthenticateSuccess() {
            toastTipMsg(R.string.fingerprint_recognition_success)
            resetGuideViewState()
        }

        override fun onAuthenticateFailed(helpId: Int) {
            toastTipMsg(R.string.fingerprint_recognition_failed)
            mFingerGuideTxt!!.setText(R.string.fingerprint_recognition_failed)
        }

        override fun onAuthenticateError(errMsgId: Int) {
            resetGuideViewState()
            toastTipMsg(R.string.fingerprint_recognition_error)
        }

        override fun onStartAuthenticateResult(isSuccess: Boolean) {}
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == KeyguardLockScreenManager.REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS) {
            // Challenge completed, proceed with using cipher
            if (resultCode == Activity.RESULT_OK) {
                toastTipMsg(R.string.sys_pwd_recognition_success)
            } else {
                toastTipMsg(R.string.sys_pwd_recognition_failed)
            }
        }
    }

    private fun toastTipMsg(messageId: Int) {
        if (mToast == null) {
            mToast = Toast.makeText(this, messageId, Toast.LENGTH_SHORT)
        }
        mToast!!.setText(messageId)
        mToast!!.cancel()
        mHandler.removeCallbacks(mShowToastRunnable)
        mHandler.postDelayed(mShowToastRunnable, 0)
    }

    private fun toastTipMsg(message: String) {
        if (mToast == null) {
            mToast = Toast.makeText(this, message, Toast.LENGTH_LONG)
        }
        mToast!!.setText(message)
        mToast!!.cancel()
        mHandler.removeCallbacks(mShowToastRunnable)
        mHandler.postDelayed(mShowToastRunnable, 200)
    }

    private var mShowToastRunnable: Runnable? = Runnable { mToast!!.show() }

    override fun onDestroy() {
        if (mFingerprintCore != null) {
            mFingerprintCore!!.onDestroy()
            mFingerprintCore = null
        }
        if (mKeyguardLockScreenManager != null) {
            mKeyguardLockScreenManager!!.onDestroy()
            mKeyguardLockScreenManager = null
        }
        mResultListener = null
        mShowToastRunnable = null
        mToast = null
        super.onDestroy()
    }
}