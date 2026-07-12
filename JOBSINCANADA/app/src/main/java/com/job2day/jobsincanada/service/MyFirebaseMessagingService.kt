package com.job2day.jobsincanada.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.job2day.jobsincanada.MainActivity
import com.job2day.jobsincanada.R

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: ${remoteMessage.from}")

        // Check if message contains a notification payload
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
            sendNotification(
                messageBody = it.body ?: "New Notification", 
                messageTitle = it.title ?: "Jobs in Canada", 
                imageUrl = it.imageUrl?.toString(), 
                data = remoteMessage.data
            )
        } ?: run {
            // Check if message contains a data-only payload
            val title = remoteMessage.data["title"] ?: "Jobs in Canada"
            val body = remoteMessage.data["body"] ?: remoteMessage.data["message"] ?: "New Notification"
            val imageUrl = remoteMessage.data["image"] ?: remoteMessage.data["image_url"]
            Log.d(TAG, "Handling data-only message: title=$title, body=$body")
            sendNotification(body, title, imageUrl, remoteMessage.data)
        }
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
    }

    private fun sendNotification(messageBody: String, messageTitle: String, imageUrl: String? = null, data: Map<String, String> = emptyMap()) {
        try {
            val intent = Intent(this, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            
            // Build target routing route
            val route = data["screen"] ?: "home"
            intent.putExtra("route", route)
            
            // Pass all notification payload data to the activity intent extras
            data.forEach { (key, value) ->
                intent.putExtra(key, value)
            }

            val pendingIntent = PendingIntent.getActivity(
                this, 0, intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            // Setup notification builder channel config
            val channelId = getString(R.string.default_notification_channel_id)
            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val largeIcon = BitmapFactory.decodeResource(resources, R.drawable.icon)
            
            val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.icon)
                .setLargeIcon(largeIcon)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)

            // Dynamically download notification image and set BigPictureStyle
            if (!imageUrl.isNullOrEmpty()) {
                try {
                    val url = java.net.URL(imageUrl)
                    val connection = url.openConnection() as java.net.HttpURLConnection
                    connection.doInput = true
                    connection.connect()
                    val input = connection.inputStream
                    val bitmap = BitmapFactory.decodeStream(input)
                    notificationBuilder.setStyle(
                        NotificationCompat.BigPictureStyle()
                            .bigPicture(bitmap)
                            .bigLargeIcon(null as android.graphics.Bitmap?)
                    )
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to load image: ${e.message}")
                }
            }

            // Trigger Notification
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    channelId,
                    getString(R.string.default_notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Default channel for app notifications"
                    enableLights(true)
                    enableVibration(true)
                }
                notificationManager.createNotificationChannel(channel)
            }

            val notificationId = System.currentTimeMillis().toInt()
            notificationManager.notify(notificationId, notificationBuilder.build())
        } catch (e: Exception) {
            Log.e(TAG, "Error showing notification", e)
        }
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }
}
