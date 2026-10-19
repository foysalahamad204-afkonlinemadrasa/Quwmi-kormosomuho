package com.example.data.notice

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * MyFirebaseMessagingService:
 * Handles incoming FCM push notifications for boosted notices.
 */
class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Notification data: title, body, and noticeId for navigation
        val title = remoteMessage.notification?.title ?: remoteMessage.data["title"] ?: "নতুন খেদমতের বিজ্ঞপ্তি"
        val body = remoteMessage.notification?.body ?: remoteMessage.data["body"] ?: "একটি নতুন গুরুত্বপূর্ণ খেদমত বিজ্ঞপ্তি প্রকাশিত হয়েছে।"
        val noticeId = remoteMessage.data["noticeId"]

        sendNotification(title, body, noticeId)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // In a real app, send this token to your server to target this device
        // NoticeRepository could have a method for this.
    }

    private fun sendNotification(title: String, body: String, noticeId: String?) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra("noticeId", noticeId)
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = "kaomi_notices"
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Placeholder
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "খেদমত বিজ্ঞপ্তি",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }
}
