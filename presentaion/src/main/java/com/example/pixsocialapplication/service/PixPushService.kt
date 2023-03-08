package com.example.pixsocialapplication.service

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PixelFormat
import android.os.*
import android.util.TypedValue
import android.view.*
import android.widget.ImageView
import androidx.core.app.NotificationCompat
import com.example.pixsocialapplication.R
import com.example.pixsocialapplication.utils.DLog
import com.example.pixsocialapplication.utils.ImageLoader


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

    private var imageUrl = ""

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        if (intent?.action != null && intent.action!!.equals(
//                ACTION_STOP_FOREGROUND, ignoreCase = true)) {
//            stopSelf()
//        }
        generateForegroundNotification()
        if (intent != null) {
            startPic(intent)
        }
//        imageUrl = intent?.getStringExtra("imageUri").toString()

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

    var startX = 0f
    var startY = 0f

    @SuppressLint("ClickableViewAccessibility")
    fun startPic(intent : Intent){
        imageUrl = intent.getStringExtra("imageUri").toString()

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

        val imagePic = mView!!.findViewById<ImageView>(R.id.image_pic)
        mView!!.setOnTouchListener { v, motionEvent ->
            when (motionEvent.action){
                MotionEvent.ACTION_DOWN -> {
                    startX = motionEvent.rawX
                    startY = motionEvent.rawY
                }
                MotionEvent.ACTION_MOVE -> {
                    val movedX: Float = motionEvent.rawX - startX
                    val movedY: Float = motionEvent.rawY - startY

                    setCoordinateUpdate(movedX, movedY)
                    startX = motionEvent.rawX
                    startY = motionEvent.rawY
//                    v.x = v.x + movedX
//                    v.y = v.y + movedY
                }
            }
            true
        }
        DLog().d("test : $imageUrl")
        ImageLoader(applicationContext).imageLoadWithURL(imageUrl, imagePic)
        windowManager?.addView(mView, params)

    }

    private fun setCoordinateUpdate(x: Float, y: Float) {
        if (windowManager != null) {
            params?.x = params?.x?.plus(x.toInt())
            params?.y = params?.y?.plus(y.toInt())

            windowManager!!.updateViewLayout(mView, params)
        }
    }


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

}