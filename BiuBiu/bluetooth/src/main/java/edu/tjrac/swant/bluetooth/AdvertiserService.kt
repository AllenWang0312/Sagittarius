package edu.tjrac.swant.bluetooth

import android.annotation.SuppressLint
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.bluetooth.BluetoothManager
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.bluetooth.le.BluetoothLeAdvertiser
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import edu.tjrac.swant.baselib.util.L
import java.util.concurrent.TimeUnit


/**
 * Manages BLE Advertising independent of the main app.
 * If the app goes off screen (or gets killed completely) advertising can continue because this
 * Service is maintaining the necessary Callback in memory.
 */
@SuppressLint("NewApi")
class AdvertiserService : Service() {

    private var mBluetoothLeAdvertiser: BluetoothLeAdvertiser? = null

    private var mAdvertiseCallback: AdvertiseCallback? = null

    private var mHandler: Handler? = null

    private var timeoutRunnable: Runnable? = null

    /**
     * Length of time to allow advertising before automatically shutting off. (10 minutes)
     */
    private val TIMEOUT = TimeUnit.MILLISECONDS.convert(10, TimeUnit.MINUTES)

    override fun onCreate() {
        L.i(TAG, "onCreate()")
        running = true
        initialize()
        startAdvertising()
        setTimeout()
        super.onCreate()
    }

    override fun onDestroy() {
        running = false
        stopAdvertising()
        mHandler!!.removeCallbacks(timeoutRunnable)
        stopForeground(true)
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    /**
     * Get references to system Bluetooth objects if we don't have them already.
     */

    private fun initialize() {
        if (mBluetoothLeAdvertiser == null) {
            val mBluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
            if (mBluetoothManager != null) {
                val mBluetoothAdapter = mBluetoothManager.adapter
                if (mBluetoothAdapter != null) {
                    mBluetoothLeAdvertiser = mBluetoothAdapter.bluetoothLeAdvertiser
                } else {
                    Toast.makeText(this, getString(R.string.bt_null), Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, getString(R.string.bt_null), Toast.LENGTH_LONG).show()
            }
        }

    }

    /**
     * Starts a delayed Runnable that will cause the BLE Advertising to timeout and stop after a
     * set amount of time.
     */
    private fun setTimeout() {
        mHandler = Handler()
        timeoutRunnable = Runnable {
            Log.d(TAG, "AdvertiserService has reached timeout of $TIMEOUT milliseconds, stopping advertising.")
            sendFailureIntent(ADVERTISING_TIMED_OUT)
            stopSelf()
        }
        mHandler!!.postDelayed(timeoutRunnable, TIMEOUT)
    }

    /**
     * Starts BLE Advertising.
     */
    private fun startAdvertising() {
        goForeground()
        Log.d(TAG, "Service: Starting Advertising")

        if (mAdvertiseCallback == null) {
            val settings = buildAdvertiseSettings()
            val data = buildAdvertiseData()
            mAdvertiseCallback = SampleAdvertiseCallback()

            if (mBluetoothLeAdvertiser != null) {
                mBluetoothLeAdvertiser!!.startAdvertising(settings, data,
                        mAdvertiseCallback)
            }
        }
    }

    private fun goForeground() {
        val notificationIntent = Intent("edu.tjrac.swant.wjzx.MainActivity")
        val pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0)
        val n = Notification.Builder(this)
                .setContentTitle("Advertising device via Bluetooth")
                .setContentText("This device is discoverable to others nearby.")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build()
        startForeground(FOREGROUND_NOTIFICATION_ID, n)
    }

    /**
     * Stops BLE Advertising.
     */
    private fun stopAdvertising() {
        Log.d(TAG, "Service: Stopping Advertising")
        if (mBluetoothLeAdvertiser != null) {
            mBluetoothLeAdvertiser!!.stopAdvertising(mAdvertiseCallback)
            mAdvertiseCallback = null
        }
    }

    /**
     * Returns an AdvertiseData object which includes the Service UUID and Device Name.
     */
    private fun buildAdvertiseData(): AdvertiseData {

        val dataBuilder = AdvertiseData.Builder()
        dataBuilder.addServiceUuid(BlueToothHelper.Service_UUID)
        dataBuilder.setIncludeDeviceName(true)

        /* For example - this will cause advertising to fail (exceeds size limit) */
        //String failureData = "asdghkajsghalkxcjhfa;sghtalksjcfhalskfjhasldkjfhdskf";
        //dataBuilder.addServiceData(Constants.Service_UUID, failureData.getBytes());
        return dataBuilder.build()
    }

    private fun buildAdvertiseSettings(): AdvertiseSettings {
        val settingsBuilder = AdvertiseSettings.Builder()
        settingsBuilder.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_POWER)
        settingsBuilder.setTimeout(0)
        return settingsBuilder.build()
    }

    private inner class SampleAdvertiseCallback : AdvertiseCallback() {

        override fun onStartFailure(errorCode: Int) {
            super.onStartFailure(errorCode)

            Log.d(TAG, "Advertising failed")
            sendFailureIntent(errorCode)
            stopSelf()

        }

        override fun onStartSuccess(settingsInEffect: AdvertiseSettings) {
            super.onStartSuccess(settingsInEffect)
            Log.d(TAG, "Advertising successfully started")
        }
    }

    /**
     * Builds and sends a broadcast intent indicating Advertising has failed. Includes the error
     * code as an extra. This is intended to be picked up by the `AdvertiserFragment`.
     */
    private fun sendFailureIntent(errorCode: Int) {
        val failureIntent = Intent()
        failureIntent.action = ADVERTISING_FAILED
        failureIntent.putExtra(ADVERTISING_FAILED_EXTRA_CODE, errorCode)
        sendBroadcast(failureIntent)
    }

    companion object {

        private val TAG = AdvertiserService::class.java.simpleName

        private val FOREGROUND_NOTIFICATION_ID = 1

        /**
         * A global variable to let AdvertiserFragment check if the Service is running without needing
         * to start or bind to it.
         * This is the best practice method as defined here:
         * https://groups.google.com/forum/#!topic/android-developers/jEvXMWgbgzE
         */
        var running = false

        val ADVERTISING_FAILED = "edu.tjrac.swant.unicorn.module.ble.advertising_failed"

        val ADVERTISING_FAILED_EXTRA_CODE = "failureCode"

        val ADVERTISING_TIMED_OUT = 6
    }

}
