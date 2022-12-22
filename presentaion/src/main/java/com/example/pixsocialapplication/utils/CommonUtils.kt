package com.example.pixsocialapplication.utils

import android.app.Activity
import androidx.core.app.ActivityCompat

object CommonUtils {

    fun appFinish(){
        ActivityCompat.finishAffinity(Activity());
        System.runFinalization()
        System.exit(0)
    }
}