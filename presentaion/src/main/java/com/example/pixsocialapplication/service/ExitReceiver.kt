package com.example.pixsocialapplication.service

import android.app.Activity
import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.core.app.ActivityCompat.getSystemService
import com.example.pixsocialapplication.utils.CommonUtils

class ExitReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val message = intent?.getStringExtra("MESSAGE")

        if (message != null) {
//            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }

//        isMyServiceRunning(PixIntentService::class.java))

       CommonUtils.appFinish()

    }
//
//    private fun isMyServiceRunning(clazz: Class<PixIntentService>) {
//        try {
//            val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
//            for (service in manager.getRunningServices(Int.MAX_VALUE)) {
//                if (clazz.name == service.service.className) {
//                    return true
//                }
//            }
//        } catch (e: Exception) {
//            return false
//        }
//        return false
//    }
}