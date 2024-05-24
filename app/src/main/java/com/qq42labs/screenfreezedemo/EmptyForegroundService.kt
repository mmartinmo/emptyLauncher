package com.qq42labs.screenfreezedemo

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

class EmptyForegroundService: Service() {

    companion object {

        fun start(context: Context) {
            try {
                val serviceIntent = Intent(context, EmptyForegroundService::class.java)
                ContextCompat.startForegroundService(context, serviceIntent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun stop(context: Context) {
            try {
                val serviceIntent = Intent(context, EmptyForegroundService::class.java)
                context.stopService(serviceIntent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private val TAG = "EmptyForegroundService"

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG,"onStartCommand() - start")
        startOnGoingNotification()
        Log.i(TAG,"onStartCommand() - end")
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.i(TAG,"onBind()")
        return null
    }

    private fun startOnGoingNotification() {
        Log.i(TAG,"startOnGoingNotification() - start")

        //val notificationManager = NotificationManagerCompat.from(this)

        InAppTimeReminderNotificationChannel(this).create()
        val builder = NotificationCompat.Builder(this, BaseNotificationChannel.ONGOING_NOTIFICATION_IN_APP_REMINDER_CHANNEL_ID)
        with (builder) {
            setContentTitle("Service title")
            setContentText("Service subtitle")
            setSmallIcon(R.drawable.ic_launcher_foreground)
            setContentIntent(InAppTimeReminderNotificationChannel.getOpenInAppTimeReminderSettingsPendingIntent(this@EmptyForegroundService))
            // setVibrate(longArrayOf(0L))
            //  setSound(null)
            priority = NotificationCompat.PRIORITY_DEFAULT

            // scope.launch {
            val notification = builder.build()
            //notificationManager.notify(BaseNotificationChannel.ONGOING_NOTIFICATION_IN_APP_REMINDER_NOTIFICATION_ID, notification)

            try { //was throwing null pointer exception on some Vivo devices in rare cases
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    startForeground(BaseNotificationChannel.ONGOING_NOTIFICATION_IN_APP_REMINDER_NOTIFICATION_ID,notification,
                        ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC)//THIS DID NOT HELP
                }
                else {
                    startForeground(BaseNotificationChannel.ONGOING_NOTIFICATION_IN_APP_REMINDER_NOTIFICATION_ID,notification)
                }
            } catch (e: Exception) {
                Log.e(TAG,"startOnGoingNotification() - exception")
                e.printStackTrace()
            }
            // }
        }
        Log.i(TAG,"startOnGoingNotification() - end")

    }

    override fun onDestroy() {
        Log.i(TAG,"onDestroy()")
    }
}