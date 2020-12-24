package com.example.applauncherlist

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

open class AppChangeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("installation", "install receiver")
    }
}