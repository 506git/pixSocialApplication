package com.example.pixsocialapplication.service

import android.annotation.SuppressLint
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PixelFormat
import android.os.*
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.app.NotificationCompat
import com.example.pixsocialapplication.BuildConfig
import com.example.pixsocialapplication.R
import com.example.pixsocialapplication.utils.DLog


// TODO: Rename actions, choose action names that describe tasks that this
// IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
private const val ACTION_FOO = "com.example.pixsocialapplication.service.action.FOO"
private const val ACTION_BAZ = "com.example.pixsocialapplication.service.action.BAZ"

// TODO: Rename parameters
private const val EXTRA_PARAM1 = "com.example.pixsocialapplication.service.extra.PARAM1"
private const val EXTRA_PARAM2 = "com.example.pixsocialapplication.service.extra.PARAM2"

/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.

 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.

 */
class PixPushService : Service() {
    private var serviceLooper: Looper? = null
    private var serviceHandler: ServiceHandler? = null

    private var iconNotification: Bitmap? = null
    private var notification: Notification? = null
    var mNotificationManager: NotificationManager? = null
    private val mNotificationId = 123

    private var windowManager: WindowManager? = null
    var mView: View? = null

    private var oldXValue = 0f
    private var oldYValue = 0f

    private var params: WindowManager.LayoutParams? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        if (intent?.action != null && intent.action!!.equals(
//                ACTION_STOP_FOREGROUND, ignoreCase = true)) {
//            stopSelf()
//        }
        generateForegroundNotification()

        serviceHandler?.obtainMessage()?.also { msg ->
            msg.arg1 = startId
            serviceHandler?.sendMessage(msg)
        }

        return START_NOT_STICKY
    }

    @SuppressLint("LaunchActivityFromNotification")
    private fun generateForegroundNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val intentMainLanding = Intent(this, MainActivity::class.java)
//            val pendingIntent = PendingIntent.getActivity(this, 0, intentMainLanding, PendingIntent.FLAG_IMMUTABLE)
            val intent = Intent(this, ExitReceiver::class.java).apply {
                putExtra("MESSAGE", "CLICKED")
            }
            val pendingIntent = PendingIntent.getBroadcast(
                this,
                System.currentTimeMillis().toInt(), intent, PendingIntent.FLAG_IMMUTABLE
            )
            iconNotification = BitmapFactory.decodeResource(resources, R.mipmap.pic_launcher)
            if (mNotificationManager == null) {
                mNotificationManager =
                    this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                assert(mNotificationManager != null)
                mNotificationManager?.createNotificationChannelGroup(
                    NotificationChannelGroup("chat_group", "Chats")
                )
                val notificationChannel =
                    NotificationChannel(
                        "service_channel", "Service Notifications",
                        NotificationManager.IMPORTANCE_MIN
                    )
                notificationChannel.enableLights(false)
                notificationChannel.lockscreenVisibility = Notification.VISIBILITY_SECRET
                mNotificationManager?.createNotificationChannel(notificationChannel)
            }
            val builder = NotificationCompat.Builder(this, "service_channel")

            builder.setContentTitle(
                StringBuilder(resources.getString(R.string.app_name)).append(" 가 실행중입니다.")
                    .toString()
            )
                .setTicker(
                    StringBuilder(resources.getString(R.string.app_name)).append(" 가 실행중입니다.")
                        .toString()
                )
                .setContentText("클릭시 앱이 종료됩니다.") //                    , swipe down for more options.
                .setSmallIcon(R.drawable.pic_icon)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setWhen(0)
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
            if (iconNotification != null) {
                builder.setLargeIcon(Bitmap.createScaledBitmap(iconNotification!!, 128, 128, false))
            }
            builder.color = resources.getColor(R.color.purple_200)
            notification = builder.build()
            startForeground(mNotificationId, notification)
        }
    }

    private inner class ServiceHandler(looper: Looper) : Handler(looper) {
        override fun handleMessage(msg: Message) {
            try {
                Thread.sleep(5000)
            } catch (e: InterruptedException) {
                // Restore interrupt status.
                Thread.currentThread().interrupt()
            }

            stopSelf(msg.arg1)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onCreate() {
        super.onCreate()
//        HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND).apply {
//            start()
//            // Get the HandlerThread's Looper and use it for our Handler
//            serviceLooper = looper
//            serviceHandler = ServiceHandler(looper)
//        }

        val inflate = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else WindowManager.LayoutParams.TYPE_SYSTEM_ALERT

        params = WindowManager.LayoutParams(
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150F, resources.displayMetrics)
                .toInt(),
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150F, resources.displayMetrics)
                .toInt(),
            oldXValue.toInt(), oldYValue.toInt(), flag,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
            PixelFormat.TRANSLUCENT
        )
        mView = inflate.inflate(R.layout.pix_service, null)

        windowManager?.addView(mView, params)


    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

}