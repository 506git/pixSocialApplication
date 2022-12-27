package com.example.pixsocialapplication.utils

import android.app.Activity
import android.view.View
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.Snackbar

object CommonUtils {

    fun appFinish(){
        ActivityCompat.finishAffinity(Activity())
        System.runFinalization()
        System.exit(0)
    }

    fun snackBar(activity: Activity, message : String, duration : Int){
        val rootView: View = activity.window.decorView.findViewById(android.R.id.content)
        Snackbar.make(rootView,message, duration).show()
    }
}