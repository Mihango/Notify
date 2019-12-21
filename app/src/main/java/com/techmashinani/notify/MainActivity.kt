package com.techmashinani.notify

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


class MainActivity : AppCompatActivity() {

    companion object {
        const val CHANNEL_ID: String = "sample hello"
        const val ACTION_SNOOZE = "com.techmashinani.notify.ACTION_SNOOZE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannel()


        with(NotificationManagerCompat.from(this)) {
            notify(200, createNotification().build())
        }
    }

    private fun createNotification(): NotificationCompat.Builder {

        // create a pending intent for tapping the notification
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        // add action to notification
        val snoozeIntent = Intent(this, MyBroadCastReceiver::class.java).apply {
            action = ACTION_SNOOZE
            putExtra("ID", 0)
        }
        val snoozePendingIntent = PendingIntent.getActivity(this, 0, snoozeIntent, 0)

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Greeting")
            .setContentText("Hello, Mum.")
            .setSmallIcon(R.drawable.ic_baseline_message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .addAction(R.drawable.ic_snooze, getString(R.string.snooze), snoozePendingIntent)
    }

    // create channel - needed by android 8.0 to show a notification
    private fun createNotificationChannel() {
        // create notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.sample_channle)
            val description = getString(R.string.channel_desc)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                this.description = description
            }

            // register channel with the system
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendIntent(message: String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, message)
        }

        if (sendIntent.resolveActivity(packageManager) != null) {
            startActivity(sendIntent)
        }
    }
}
