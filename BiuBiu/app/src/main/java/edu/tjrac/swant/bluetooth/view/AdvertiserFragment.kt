/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.tjrac.swant.bluetooth.view

import android.annotation.SuppressLint
import android.bluetooth.le.AdvertiseCallback
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.Toast

import edu.tjrac.swant.baselib.common.base.BaseFragment
import edu.tjrac.swant.bluetooth.AdvertiserService
import edu.tjrac.swant.wjzx.R

@SuppressLint("ValidFragment")
class AdvertiserFragment(title: String)//        this.title = title;
    : BaseFragment(), View.OnClickListener {

    private var mSwitch: Switch? = null

    private val advertisingFailureReceiver = object : BroadcastReceiver() {

        /**
         * Receives Advertising error codes from `AdvertiserService` and displays error messages
         * to the user. Sets the advertising toggle to 'false.'
         */
        override fun onReceive(context: Context, intent: Intent) {

            val errorCode = intent.getIntExtra(AdvertiserService.ADVERTISING_FAILED_EXTRA_CODE, -1)

            mSwitch!!.isChecked = false

            var errorMessage = getString(R.string.start_error_prefix)
            when (errorCode) {
                AdvertiseCallback.ADVERTISE_FAILED_ALREADY_STARTED -> errorMessage += " " + getString(R.string.start_error_already_started)
                AdvertiseCallback.ADVERTISE_FAILED_DATA_TOO_LARGE -> errorMessage += " " + getString(R.string.start_error_too_large)
                AdvertiseCallback.ADVERTISE_FAILED_FEATURE_UNSUPPORTED -> errorMessage += " " + getString(R.string.start_error_unsupported)
                AdvertiseCallback.ADVERTISE_FAILED_INTERNAL_ERROR -> errorMessage += " " + getString(R.string.start_error_internal)
                AdvertiseCallback.ADVERTISE_FAILED_TOO_MANY_ADVERTISERS -> errorMessage += " " + getString(R.string.start_error_too_many)
                AdvertiserService.ADVERTISING_TIMED_OUT -> errorMessage = " " + getString(R.string.advertising_timedout)
                else -> errorMessage += " " + getString(R.string.start_error_unknown)
            }

            Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_advertiser, container, false)
        mSwitch = view.findViewById<View>(R.id.advertise_switch) as Switch
        mSwitch!!.setOnClickListener(this)
        return view
    }

    override fun onResume() {
        super.onResume()
        if (AdvertiserService.running) {
            mSwitch!!.isChecked = true
        } else {
            mSwitch!!.isChecked = false
        }
        val failureFilter = IntentFilter(AdvertiserService.ADVERTISING_FAILED)
        activity!!.registerReceiver(advertisingFailureReceiver, failureFilter)
    }

    override fun onPause() {
        super.onPause()
        activity!!.unregisterReceiver(advertisingFailureReceiver)
    }

    private fun getServiceIntent(c: Context?): Intent {
        return Intent(c, AdvertiserService::class.java)
    }

    override fun onClick(v: View) {
        // Is the toggle on?
        val on = (v as Switch).isChecked
        if (on) {
            activity!!.startService(getServiceIntent(activity))
        } else {
            activity!!.stopService(getServiceIntent(activity))
            mSwitch!!.isChecked = false
        }
    }

    override fun onBack() {

    }
}