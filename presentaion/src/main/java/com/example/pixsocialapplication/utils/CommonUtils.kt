package com.example.pixsocialapplication.utils

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.example.pixsocialapplication.utils.pixel.Pixelate.fromBitmap
import com.example.pixsocialapplication.utils.pixel.PixelateLayer
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import java.net.URL

object CommonUtils {

    fun appFinish() {
        ActivityCompat.finishAffinity(Activity())
        System.runFinalization()
        System.exit(0)
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

    fun convertPixelArt(
        resources: Resources,
        resId: Int,
        width: Int = 100,
        height: Int = 100
    ): Bitmap {
        val bitmap = decodeBitmapFromResource(resources, resId, width, height)

        val pixelate = PixelateLayer.Builder(PixelateLayer.Shape.Square)
            .setResolution(20f)
            .setSize(15f)
            .setOffset(10f)
            .build()

        return fromBitmap(bitmap, pixelate, pixelate)
    }

    suspend fun convertPixelArtUrl( resources: Resources, url: String, width: Int = 100,
                                    height: Int = 100): Bitmap? {
        val job = CoroutineScope(Dispatchers.IO).async {
            try {
                var bitmap: Bitmap? = null
// 화면 크기에 가장 근접하는 이미지의 리스케일 사이즈를 구한다.
                withContext(Dispatchers.IO) {
                    bitmap = decodeBitmapFromResource(resources, url, width, height)
//                    bitmap = BitmapFactory.decodeStream(URL(url).openStream())
//                    bitmap = Bitmap.createScaledBitmap(bitmap!!,200,200,true);
                }

                val pixelate = PixelateLayer.Builder(PixelateLayer.Shape.Square)
                    .setResolution(15f)
                    .setSize(10f)
                    .setOffset(10f)
                    .build()

                Log.d("TEST_bitmap", bitmap.toString())
                return@async fromBitmap(bitmap!!, pixelate, pixelate)
            } catch (e: Exception) {
                DLog().d(e.toString())
                return@async null
            }
        }

        return job.await()
    }

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