package com.example.pixsocialapplication.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AlertDialog
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
        Snackbar.make(rootView,message, duration).setAction("Confirm!"){

        }.show()
    }

    fun customDialog(view : View, context: Context, cancelable : Boolean = false): AlertDialog {
        val dialog = AlertDialog.Builder(context).setView(view).create()
        with(dialog) {
            setCancelable(cancelable)
            setCanceledOnTouchOutside(cancelable)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.setGravity(Gravity.CENTER_VERTICAL)
        }
        return dialog
    }
}