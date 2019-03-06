package com.techmashinani.notify

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val PRIMARY_CHANNEL_ID = "primary_notification_channel"
        const val NOTIFICATION_ID = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setup()
    }

    private fun setup() {
        createNotificationChannel()
        setNotificationButtonState(isNotifyEnabled = true, isUpdateEnable = false, isCancelEnable = false)
        btn_notify.setOnClickListener { sendNotification() }
        btn_cancel.setOnClickListener { cancelNotification() }
        btn_update.setOnClickListener { updateNotification() }
    }

    private fun sendNotification() {
        with(NotificationManagerCompat.from(this)) {
            notify(NOTIFICATION_ID, getNotificationBuilder().build())
        }
        setNotificationButtonState(isNotifyEnabled = false, isUpdateEnable = true, isCancelEnable = true)
    }

    private fun createNotificationChannel() {
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            // create channel
            val channel = NotificationChannel(PRIMARY_CHANNEL_ID, "Mascot Notification", NotificationManager.IMPORTANCE_HIGH)
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.description = "Notification from Mascot"

            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun getNotificationBuilder(): NotificationCompat.Builder {
        val intent = Intent(this, MainActivity::class.java)
        val notificationPendingIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        return NotificationCompat.Builder(this@MainActivity, PRIMARY_CHANNEL_ID)
            .setContentTitle("You have been notified!")
            .setContentText("this is your notification text.!")
            .setSmallIcon(R.drawable.ic_android)
            .setContentIntent(notificationPendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
    }

    private fun updateNotification() {
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.mascot_1)
        val buider = getNotificationBuilder()
            .setStyle(NotificationCompat.BigPictureStyle()
                .bigPicture(bitmap)
                .setBigContentTitle("Notifiction Updated!!")
            )

        with(NotificationManagerCompat.from(this)){
            notify(NOTIFICATION_ID, buider.build())
        }

        setNotificationButtonState(isNotifyEnabled = false, isUpdateEnable = false, isCancelEnable = true)
    }

    private fun cancelNotification() {
        with(NotificationManagerCompat.from(this)) {
            cancel(NOTIFICATION_ID)
        }

        setNotificationButtonState(isNotifyEnabled = true, isUpdateEnable = false, isCancelEnable = false)
    }

    private fun setNotificationButtonState(isNotifyEnabled: Boolean, isUpdateEnable: Boolean, isCancelEnable: Boolean) {
        btn_notify.isEnabled = isNotifyEnabled
        btn_update.isEnabled = isUpdateEnable
        btn_cancel.isEnabled = isCancelEnable
    }
}
