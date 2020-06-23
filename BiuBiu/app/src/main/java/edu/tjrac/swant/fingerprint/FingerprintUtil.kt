package edu.tjrac.swant.fingerprint

import android.content.Context
import android.content.Intent

/**
 * Created by popfisher on 2016/11/7.
 */
object FingerprintUtil {
    private const val ACTION_SETTING = "android.settings.SETTINGS"
    fun openFingerPrintSettingPage(context: Context) {
        val intent = Intent(ACTION_SETTING)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
        }
    }
}