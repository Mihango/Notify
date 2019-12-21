package com.techmashinani.notify

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class MyBroadCastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val pendingResult = goAsync()
        for (i in 1..50) {
            Log.e("Logging text", "i >>>>>>>> $i")
        }
        pendingResult.finish()
    }

}