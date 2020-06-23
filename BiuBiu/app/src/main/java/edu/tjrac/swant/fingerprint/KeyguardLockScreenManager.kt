package edu.tjrac.swant.fingerprint

import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.os.Build

/**
 * Created by popfisher on 2016/11/8.
 */
class KeyguardLockScreenManager(context: Context) {
    private var mKeyManager: KeyguardManager?

    /**
     * 是否开启锁屏密码，注意：有Api版本限制
     * @return
     */
    val isOpenLockScreenPwd: Boolean
        get() = try {
            if (Build.VERSION.SDK_INT < 16) {
                false
            } else mKeyManager != null && mKeyManager!!.isKeyguardSecure
        } catch (e: Exception) {
            false
        }

    /**
     * 锁屏密码，注意：有Api版本限制
     */
    fun showAuthenticationScreen(activity: Activity) {
        if (Build.VERSION.SDK_INT < 21) {
            return
        }
        val intent = mKeyManager!!.createConfirmDeviceCredentialIntent("锁屏密码", "测试锁屏密码")
        if (intent != null) {
            activity.startActivityForResult(intent, REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS)
        }
    }

    fun onDestroy() {
        mKeyManager = null
    }

    companion object {
        const val REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS = 0
        fun getKeyguardManager(context: Context): KeyguardManager? {
            var keyguardManager: KeyguardManager? = null
            try {
                keyguardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            } catch (throwable: Throwable) {
                //            FPLog.log("getKeyguardManager getKeyguardManagerexception");
            }
            return keyguardManager
        }
    }

    init {
        mKeyManager = getKeyguardManager(context)
    }
}