package com.example.pixsocialapplication.fcm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.VISIBILITY_PRIVATE
import androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.pixsocialapplication.R
import com.example.pixsocialapplication.ui.MainActivity
import com.example.pixsocialapplication.utils.DLog

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService: FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        DLog().d("${remoteMessage.data}, notification : ${remoteMessage.notification}")
//        Toast.makeText(applicationContext,"message : ${remoteMessage.data.toString()}, notification : ${remoteMessage.notification}",Toast.LENGTH_LONG).show()
        if (remoteMessage.data.isNotEmpty()) {
            DLog().d(remoteMessage.data["imageUrl"])
            sendNotification(remoteMessage.data)
//            sendNotify(remoteMessage.data)
//            remoteMessage.data?.let {
//
//                sendNotification(it)
//
//            }
//            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use WorkManager.
//                scheduleJob()
        } else {
            Log.d("test", "Message data payload: error")
                // Handle message within 10 seconds
//                handleNow()
//            }
        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.d("TEST", "Message Notification : ${it.imageUrl}")
            Log.d("TEST", "Message Notification Body: ${it.body}")
            sendNotification(it)

        }

    }


    override fun onNewToken(token: String) {
        sendRegistrationToServer(token)
    }
    private fun sendRegistrationToServer(token: String?) {
        // TODO: Implement this method to send token to your app server.
        DLog().d(message = "token = $token")
    }

    private fun sendNotification(messageBody: RemoteMessage.Notification) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
            PendingIntent.FLAG_IMMUTABLE)

        val channelId = getString(R.string.default_notification_channel_id)

        val notificationCompat = NotificationCompat.Builder(this, channelId)
        val notificationManager =  NotificationManagerCompat.from(this)

        notificationCompat.apply {
            setSmallIcon(R.drawable.pic_icon)
            setContentTitle(getString(R.string.app_name))
            setContentTitle(messageBody.body)
            priority = NotificationCompat.PRIORITY_HIGH
            setAutoCancel(true)
            setContentIntent(pendingIntent)
            setVisibility(VISIBILITY_PUBLIC)
        }

//        val messageBody2 = "https://firebasestorage.googleapis.com/v0/b/pixsocial-a41c6.appspot.com/o/pvxR3U9OpMcIqiGNoSLRkOKmQ1F3%2F5aed6734-087d-418c-bbe1-1d0974b94982.jpg?alt=media&token=4d736cdd-e0f5-4dcf-b11a-799dbf27abe1"
        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        if (messageBody.imageUrl != null) {
            Glide.with(applicationContext).asBitmap().load(messageBody.imageUrl)
                .into(object : CustomTarget<Bitmap>(){
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        DLog().d("test start")
                        notificationCompat.setLargeIcon(resource)
                        notificationCompat.setStyle(NotificationCompat.BigPictureStyle().bigPicture(resource))
                        notificationManager.notify(0, notificationCompat.build())

                    }

                    override fun onLoadCleared(placeholder: Drawable?) {

                    }

                })
        } else  notificationManager.notify(0 /* ID of notification */, notificationCompat.build())
    }

    private fun sendNotification(data: Map<String, String>) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
            PendingIntent.FLAG_IMMUTABLE)

        val message = data["body"]
        val imageUrl = data["imageUrl"]
        val channelId = getString(R.string.default_notification_channel_id)

        val notificationCompat = NotificationCompat.Builder(this, channelId)
        val notificationManager =  NotificationManagerCompat.from(this)

        notificationCompat.apply {
            setSmallIcon(R.drawable.pic_icon)
            setContentTitle(getString(R.string.app_name))
            setContentTitle(message)
            priority = NotificationCompat.PRIORITY_HIGH
            setAutoCancel(true)
            setContentIntent(pendingIntent)
            setVisibility(VISIBILITY_PUBLIC)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannelCompat.Builder(channelId, NotificationManagerCompat.IMPORTANCE_HIGH)
                .setName("Channel human readable title")
                .build()
            notificationManager.createNotificationChannel(channel)
        }

        if (imageUrl != null) {
            Glide.with(applicationContext).asBitmap().load(imageUrl)
                .into(object : CustomTarget<Bitmap>(){
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        DLog().d("test start")
                        notificationCompat.setLargeIcon(resource)
                        notificationCompat.setStyle(NotificationCompat.BigPictureStyle().bigPicture(resource))
                        notificationManager.notify(0, notificationCompat.build())
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {

                    }

                })
        } else  notificationManager.notify(0 /* ID of notification */, notificationCompat.build())
    }

    companion object {

        private const val TAG = "MyFirebaseMsgService"
    }
}