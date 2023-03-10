package com.example.pixsocialapplication.utils

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.example.pixsocialapplication.ui.common.CommonActivity
import com.example.pixsocialapplication.ui.common.LoadingDialog
import com.google.android.material.snackbar.Snackbar
import java.net.URL
import kotlin.system.exitProcess

object CommonUtils {

    fun appFinish(activity: Activity = Activity()) {
        ActivityCompat.finishAffinity(activity)
        System.runFinalization()
        exitProcess(0)
    }

    fun showDialog(activity: Activity){
        (activity as CommonActivity).show()
    }

    fun dismissDialog(activity: Activity){
        (activity as CommonActivity).hide()
    }

    fun snackBar(activity: Activity, message: String, duration: Int) {
        val rootView: View = activity.window.decorView.findViewById(android.R.id.content)
        Snackbar.make(rootView, message, duration).setAction("Confirm!") {

        }.show()
    }

    fun customDialog(view: View, context: Context, cancelable: Boolean = false): AlertDialog {
        val dialog = AlertDialog.Builder(context).setView(view).create()
        with(dialog) {
            setCancelable(cancelable)
            setCanceledOnTouchOutside(cancelable)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.setGravity(Gravity.CENTER_VERTICAL)
        }
        return dialog
    }

    fun alertDialog(context: Context, message: String, btnText: String, cancelable: Boolean = false, onClick: DialogInterface.OnClickListener) {
        val dialog = AlertDialog.Builder(context).setTitle("알림").setCancelable(cancelable)
            .setMessage(message).setPositiveButton(btnText, onClick)
            .create()
        dialog.show()
    }


    fun getScreenHeight(context: Context): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = wm.currentWindowMetrics
            val insets = windowMetrics.windowInsets
                .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            windowMetrics.bounds.height() - insets.bottom - insets.top
        } else {
            val displayMetrics = DisplayMetrics()
            wm.defaultDisplay.getMetrics(displayMetrics)
            displayMetrics.heightPixels
        }
    }

    fun getScreenWidth(context: Context): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = wm.currentWindowMetrics
            val insets = windowMetrics.windowInsets
                .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            windowMetrics.bounds.width() - insets.left - insets.right
        } else {
            val displayMetrics = DisplayMetrics()
            wm.defaultDisplay.getMetrics(displayMetrics)
            displayMetrics.widthPixels
        }
    }

    var networkState = true

    fun decodeBitmapFromResource(
        res: Resources,
        url: String,
        reqWidth: Int,
        reqHeight: Int
    ): Bitmap {
        // First decode with inJustDecodeBounds=true to check dimensions
        return BitmapFactory.Options().run {
            inJustDecodeBounds = true
            BitmapFactory.decodeStream(URL(url).openStream())
//            BitmapFactory.decodeResource(res, resId, this)

            // Calculate inSampleSize
            inSampleSize = calculateInSampleSize(this, reqWidth, reqHeight)

            // Decode bitmap with inSampleSize set
            inJustDecodeBounds = false

            BitmapFactory.decodeStream(URL(url).openStream())
        }
    }

    fun decodeBitmapFromResource(
        res: Resources,
        resId: Int,
        reqWidth: Int,
        reqHeight: Int
    ): Bitmap {
        // First decode with inJustDecodeBounds=true to check dimensions
        return BitmapFactory.Options().run {
            inJustDecodeBounds = true
            BitmapFactory.decodeResource(res, resId, this)

            // Calculate inSampleSize
            inSampleSize = calculateInSampleSize(this, reqWidth, reqHeight)

            // Decode bitmap with inSampleSize set
            inJustDecodeBounds = false

            BitmapFactory.decodeResource(res, resId, this)
        }
    }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        // Raw height and width of image
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }
}