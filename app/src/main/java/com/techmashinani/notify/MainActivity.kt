package com.techmashinani.notify

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val ACTION_UPDATE_NOTIFICATION = "com.techmashinani.notify.ACTION_UPDATE_NOTIFICATION"
        const val ACTION_DELETE_NOTIFICATION = "com.techmashinani.notify.ACTION_DELETE_NOTIFICATION"
        const val PRIMARY_CHANNEL_ID = "primary_notification_channel"
        const val NOTIFICATION_ID = 0
    }

    private val mReceiver: NotificationReceiver by lazy { NotificationReceiver() }
    private val mDeleteReceiver: DeleteReceiver by lazy { DeleteReceiver() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setup()
        registerReceiver(mReceiver, IntentFilter(ACTION_UPDATE_NOTIFICATION))
        registerReceiver(mDeleteReceiver, IntentFilter(ACTION_DELETE_NOTIFICATION))
    }

    private fun setup() {
        createNotificationChannel()
        setNotificationButtonState(isNotifyEnabled = true, isUpdateEnable = false, isCancelEnable = false)
        btn_notify.setOnClickListener { sendNotification() }
        btn_cancel.setOnClickListener { cancelNotification() }
        btn_update.setOnClickListener { updateNotification() }
    }

    private fun createNotificationChannel() {
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            // create channel
            val channel = NotificationChannel(PRIMARY_CHANNEL_ID, "Mascot Notification", NotificationManager.IMPORTANCE_HIGH).apply {
                enableLights(true)
                lightColor = Color.RED
                enableVibration(true)
                description = "Notification from Mascot"
            }

            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification() {
        val updateIntent = Intent(ACTION_UPDATE_NOTIFICATION)
        val updatePendingIntent = PendingIntent.getBroadcast(this, NOTIFICATION_ID, updateIntent, PendingIntent.FLAG_ONE_SHOT)

        with(NotificationManagerCompat.from(this)) {
            notify(NOTIFICATION_ID, getNotificationBuilder()
                .addAction(R.drawable.ic_action_history, "Update Notification", updatePendingIntent)
                .build())
        }
        setNotificationButtonState(isNotifyEnabled = false, isUpdateEnable = true, isCancelEnable = true)
    }



    private fun getNotificationBuilder(): NotificationCompat.Builder {
        val intent = Intent(this, MainActivity::class.java)
        val notificationPendingIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val deleteIntent = Intent(ACTION_DELETE_NOTIFICATION)
        val deletePendingIntent = PendingIntent.getBroadcast(this, NOTIFICATION_ID, deleteIntent, PendingIntent.FLAG_ONE_SHOT)

        return NotificationCompat.Builder(this@MainActivity, PRIMARY_CHANNEL_ID).apply {
            setContentTitle("You have been notified!")
            setContentText("this is your notification text.!")
            setSmallIcon(R.drawable.ic_android)
            setContentIntent(notificationPendingIntent)
            setAutoCancel(true)
            priority = NotificationCompat.PRIORITY_HIGH
            setDefaults(NotificationCompat.DEFAULT_ALL)
            setDeleteIntent(deletePendingIntent)
        }
    }

    private fun updateNotification() {
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.mascot_1)
        val builder = getNotificationBuilder()
            .setStyle(NotificationCompat.BigPictureStyle()
                .bigPicture(bitmap)
                .setBigContentTitle("Notifiction Updated!!")
            )

        with(NotificationManagerCompat.from(this)){
            notify(NOTIFICATION_ID, builder.build())
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

    // broadcast receiver for listening to reply
    inner class NotificationReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            updateNotification()
        }
    }

    inner class DeleteReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            setNotificationButtonState(isNotifyEnabled = true, isUpdateEnable = false, isCancelEnable = false)
        }
    }

    override fun onDestroy() {
        unregisterReceiver(mReceiver)
        unregisterReceiver(mDeleteReceiver)
        super.onDestroy()
    }
}
