package com.qq42labs.screenfreezedemo

import android.app.PendingIntent
import android.content.Context
import android.content.Intent

class InAppTimeReminderNotificationChannel(
    context: Context
): BaseNotificationChannel(
    channelID = ONGOING_NOTIFICATION_IN_APP_REMINDER_CHANNEL_ID,
    name = "Service name",
    descriptionText = "Description",
    context = context
) {

    companion object {

        fun getOpenInAppTimeReminderSettingsPendingIntent(context: Context) : PendingIntent {
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        }
    }
}