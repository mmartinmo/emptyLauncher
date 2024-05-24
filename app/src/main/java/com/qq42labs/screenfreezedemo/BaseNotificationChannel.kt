package com.qq42labs.screenfreezedemo

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import java.lang.Exception

abstract class BaseNotificationChannel(
    private val channelID: String,
    private val name: String,
    private val descriptionText: String,
    private val context: Context
) {

    companion object {

        const val ONGOING_NOTIFICATION_NOTIFICATION_MANAGER_CHANNEL_ID = "channel id notification manager11"

        const val ONGOING_NOTIFICATION_FOR_MIN_PHONE_MANAGER_ID = 1
        const val NOTIFICATION_MANAGER_SETUP_NOTIFICATION_ID = 2

        const val ONGOING_NOTIFICATION_MONOCHROME_CHANNEL_ID = "channel id monochrome mode 1"
        const val ONGOING_NOTIFICATION_FOR_MONOCHROME_MODE_NOTIFICATION_ID = 30

        const val ONGOING_NOTIFICATION_IN_APP_REMINDER_CHANNEL_ID = "channel id in app time reminder Android14"
        const val ONGOING_NOTIFICATION_IN_APP_REMINDER_NOTIFICATION_ID = 6
        const val IN_APP_TIMER_SETUP_NOTIFICATION_ID = 5

        const val SETUP_REMINDER_CHANNEL_ID = "channel id setup reminder"
        const val SETUP_REMINDER_NOTIFICATION_ID = 6

        suspend fun isNotificationActive(notificationID: Int, context: Context): Boolean {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                try {
                    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    val notifications = notificationManager.activeNotifications //this call can take longer time and cause ANR - call only from coroutine
                    notifications?.forEach { notification ->
                        if (notification.id == notificationID)
                            return true
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return false
        }
    }

    fun create() {

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelID, name, importance).apply {
                description = descriptionText
                vibrationPattern = longArrayOf(0L)
                enableVibration(true)
                enableLights(false)

                val audioAttributes = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_COMMUNICATION_DELAYED)
                    .build()
                setSound(null, audioAttributes)
            }
            // Register the channel with the system
            val notificationManager = NotificationManagerCompat.from(context.applicationContext)
            notificationManager.createNotificationChannel(channel)
        }
    }
}