package com.example.superheroesfromaliexpress.backgroundtask

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.superheroesfromaliexpress.R
import com.example.superheroesfromaliexpress.core.INTENT_TAG_DETAILED_HERO
import com.example.superheroesfromaliexpress.ui.activities.DetailedSuperHeroActivity


/**
 * Created by David Trpchevski (trpcevski.david@gmail.com) on 15 June 2019
 */
class NotifyUtils(private val context: Context) {

    private var notificationCompatBuilder: NotificationCompat.Builder? = null
    private var notificationManagerCompat: NotificationManagerCompat? = null


    fun createNotification(id: String, message: String, title: String, idForItem: Int) {
        notificationCompatBuilder = NotificationCompat.Builder(context, createNotificationChannel())
        notificationManagerCompat = NotificationManagerCompat.from(context)

        val mainAppIntent = Intent(context, DetailedSuperHeroActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .putExtra(INTENT_TAG_DETAILED_HERO, idForItem)

        val appPendingIntent = PendingIntent.getActivity(context, 0, mainAppIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val action = NotificationCompat.Action.Builder(R.drawable.notification, "Open my chosen hero", appPendingIntent)

        notificationCompatBuilder
                ?.setDefaults(Notification.DEFAULT_LIGHTS)
                ?.setSmallIcon(R.drawable.fly)
                ?.setContentTitle(title)?.setContentText(message)
                ?.setPriority(NotificationCompat.PRIORITY_HIGH)
                ?.setAutoCancel(true)
                ?.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                ?.setColorized(true)
                ?.setContentIntent(appPendingIntent)
                ?.addAction(action.build())
                ?.setOnlyAlertOnce(true)

        notificationCompatBuilder?.build()?.let { notificationManagerCompat?.notify(id.toInt(), it) }


    }


    private fun createNotificationChannel(): String {

        val channel = "12"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val name = "Notifications channel"
            val description = "Blaze it"

            val notificationChannel = NotificationChannel(channel, name, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.description = description
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.MAGENTA
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        return channel

    }

}