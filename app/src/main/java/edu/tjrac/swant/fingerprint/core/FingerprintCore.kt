package edu.tjrac.swant.fingerprint.core

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.CancellationSignal
import android.os.Handler
import android.os.Looper
import android.util.Log

import java.lang.ref.WeakReference


/**
 * Created by popfisher on 2016/11/7.
 */
@TargetApi(Build.VERSION_CODES.M)
@SuppressLint("MissingPermission")

class FingerprintCore(context: Context) {
    private var mState = NONE

    private var mFingerprintManager: FingerprintManager? = null
    private var mFpResultListener: WeakReference<IFingerprintResultListener>? = null
    private var mCancellationSignal: CancellationSignal? = null
    private var mCryptoObjectCreator: CryptoObjectCreator? = null
    private var mAuthCallback: FingerprintManager.AuthenticationCallback? = null

    private var mFailedTimes = 0
    var isSupport = false
    private var mHandler: Handler? = Handler(Looper.getMainLooper())

    val isAuthenticating: Boolean
        get() = mState == AUTHENTICATING

    private val mFailedRetryRunnable = Runnable { startAuthenticate(mCryptoObjectCreator!!.cryptoObject) }

    /**
     * 时候有指纹识别硬件支持
     * @return
     */
    val isHardwareDetected: Boolean
        get() {
            try {
                return mFingerprintManager!!.isHardwareDetected
            } catch (e: SecurityException) {
            } catch (e: Throwable) {
            }

            return false
        }

    /**
     * 是否录入指纹，有些设备上即使录入了指纹，但是没有开启锁屏密码的话此方法还是返回false
     * @return
     */
    // 有些厂商api23之前的版本可能没有做好兼容，这个方法内部会崩溃（redmi note2, redmi note3等）
    val isHasEnrolledFingerprints: Boolean
        get() {
            try {
                return mFingerprintManager!!.hasEnrolledFingerprints()
            } catch (e: SecurityException) {
            } catch (e: Throwable) {
            }

            return false
        }

    /**
     * 指纹识别回调接口
     */
    interface IFingerprintResultListener {
        /** 指纹识别成功  */
        fun onAuthenticateSuccess()

        /** 指纹识别失败  */
        fun onAuthenticateFailed(helpId: Int)

        /** 指纹识别发生错误-不可短暂恢复  */
        fun onAuthenticateError(errMsgId: Int)

        /** 开始指纹识别监听成功  */
        fun onStartAuthenticateResult(isSuccess: Boolean)
    }

    init {
        mFingerprintManager = getFingerprintManager(context)
        isSupport = mFingerprintManager != null && isHardwareDetected
        //        FPLog.log("fingerprint isSupport: " + isSupport);
        initCryptoObject()
    }

    private fun initCryptoObject() {
        try {
            mCryptoObjectCreator = CryptoObjectCreator(CryptoObjectCreator.ICryptoObjectCreateListener {
                // startAuthenticate(cryptoObject);
                // 如果需要一开始就进行指纹识别，可以在秘钥数据创建之后就启动指纹认证
            })
        } catch (throwable: Throwable) {
            //            FPLog.log("create cryptoObject failed!");
        }

    }

    fun setFingerprintManager(fingerprintResultListener: IFingerprintResultListener) {
        mFpResultListener = WeakReference(fingerprintResultListener)
    }

    fun startAuthenticate() {
        startAuthenticate(mCryptoObjectCreator!!.cryptoObject)
    }

    @SuppressLint("MissingPermission")
    private fun startAuthenticate(cryptoObject: FingerprintManager.CryptoObject) {
        prepareData()
        mState = AUTHENTICATING
        try {
            mFingerprintManager!!.authenticate(cryptoObject, mCancellationSignal, 0, mAuthCallback!!, null)
            notifyStartAuthenticateResult(true, "")
        } catch (e: SecurityException) {
            try {
                mFingerprintManager!!.authenticate(null, mCancellationSignal, 0, mAuthCallback!!, null)
                notifyStartAuthenticateResult(true, "")
            } catch (e2: SecurityException) {
                notifyStartAuthenticateResult(false, Log.getStackTraceString(e2))
            } catch (throwable: Throwable) {

            }

        } catch (throwable: Throwable) {

        }

    }

    private fun notifyStartAuthenticateResult(isSuccess: Boolean, exceptionMsg: String) {
        if (isSuccess) {
//            FPLog.log("start authenticate...")
            if (mFpResultListener!!.get() != null) {
                mFpResultListener!!.get()?.onStartAuthenticateResult(true)
            }
        } else {
//            FPLog.log("startListening, Exception$exceptionMsg")
            if (mFpResultListener!!.get() != null) {
                mFpResultListener!!.get()?.onStartAuthenticateResult(false)
            }
        }
    }

    private fun notifyAuthenticationSucceeded() {
//        FPLog.log("onAuthenticationSucceeded")
        mFailedTimes = 0
        if (null != mFpResultListener && null != mFpResultListener!!.get()) {
            mFpResultListener!!.get()?.onAuthenticateSuccess()
        }
    }

    private fun notifyAuthenticationError(errMsgId: Int, errString: CharSequence) {
//        FPLog.log("onAuthenticationError, errId:$errMsgId, err:$errString, retry after 30 seconds")
        if (null != mFpResultListener && null != mFpResultListener!!.get()) {
            mFpResultListener!!.get()?.onAuthenticateError(errMsgId)
        }
    }

    private fun notifyAuthenticationFailed(msgId: Int, errString: String) {
//        FPLog.log("onAuthenticationFailed, msdId: $msgId errString: $errString")
        if (null != mFpResultListener && null != mFpResultListener!!.get()) {
            mFpResultListener!!.get()?.onAuthenticateFailed(msgId)
        }
    }

    private fun prepareData() {
        if (mCancellationSignal == null) {
            mCancellationSignal = CancellationSignal()
        }
        if (mAuthCallback == null) {
            mAuthCallback = object : FingerprintManager.AuthenticationCallback() {
                override fun onAuthenticationError(errMsgId: Int, errString: CharSequence) {
                    // 多次指纹密码验证错误后，进入此方法；并且，不能短时间内调用指纹验证,一般间隔从几秒到几十秒不等
                    // 这种情况不建议重试，建议提示用户用其他的方式解锁或者认证
                    mState = NONE
                    notifyAuthenticationError(errMsgId, errString)
                }

                override fun onAuthenticationHelp(helpMsgId: Int, helpString: CharSequence) {
                    mState = NONE
                    // 建议根据参数helpString返回值，并且仅针对特定的机型做处理，并不能保证所有厂商返回的状态一致
                    notifyAuthenticationFailed(helpMsgId, helpString.toString())
                    onFailedRetry(helpMsgId, helpString.toString())
                }

                override fun onAuthenticationFailed() {
                    mState = NONE
                    notifyAuthenticationFailed(0, "onAuthenticationFailed")
                    onFailedRetry(-1, "onAuthenticationFailed")
                }

                override fun onAuthenticationSucceeded(result: FingerprintManager.AuthenticationResult) {
                    mState = NONE
                    notifyAuthenticationSucceeded()
                }
            }
        }
    }

    fun cancelAuthenticate() {
        if (mCancellationSignal != null && mState != CANCEL) {
//            FPLog.log("cancelAuthenticate...")
            mState = CANCEL
            mCancellationSignal!!.cancel()
            mCancellationSignal = null
        }
    }

    private fun onFailedRetry(msgId: Int, helpString: String) {
        mFailedTimes++
//        FPLog.log("on failed retry time $mFailedTimes")
        if (mFailedTimes > 5) { // 每个验证流程最多重试5次，这个根据使用场景而定，验证成功时清0
//            FPLog.log("on failed retry time more than 5 times")
            return
        }
//        FPLog.log("onFailedRetry: msgId $msgId helpString: $helpString")
        cancelAuthenticate()
        mHandler!!.removeCallbacks(mFailedRetryRunnable)
        mHandler!!.postDelayed(mFailedRetryRunnable, 300) // 每次重试间隔一会儿再启动
    }

    fun onDestroy() {
        cancelAuthenticate()
        mHandler = null
        mAuthCallback = null
        mFpResultListener = null
        mCancellationSignal = null
        mFingerprintManager = null
        if (mCryptoObjectCreator != null) {
            mCryptoObjectCreator!!.onDestroy()
            mCryptoObjectCreator = null
        }
    }

    companion object {

        private val NONE = 0
        private val CANCEL = 1
        private val AUTHENTICATING = 2

        fun getFingerprintManager(context: Context): FingerprintManager? {
            var fingerprintManager: FingerprintManager? = null
            try {
                fingerprintManager = context.getSystemService(Context.FINGERPRINT_SERVICE) as FingerprintManager
            } catch (e: Throwable) {
                //            FPLog.log("have not class FingerprintManager");
            }

            return fingerprintManager
        }
    }
}
