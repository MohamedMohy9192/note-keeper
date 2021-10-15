package com.androideradev.www.notekeeper.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.androideradev.www.notekeeper.R


object ReminderNotification {

    private const val REMINDER_NOTIFICATION_ID = 3322
    private const val REMINDER_CHANNEL_ID = "REMINDER_CHANNEL_ID"

    fun notify(context: Context, titleText: String, noteText: String, noteId: Int) {


        val shareIntent = PendingIntent.getActivity(
            context,
            REMINDER_NOTIFICATION_ID,
            Intent.createChooser(
                Intent(Intent.ACTION_SEND)
                    .setType("text/plain")
                    .putExtra(
                        Intent.EXTRA_TEXT,
                        noteText
                    ),
                "Share Note Reminder"
            ),
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(context, REMINDER_CHANNEL_ID)
            .setDefaults(Notification.DEFAULT_ALL)
            .setSmallIcon(R.drawable.ic_baseline_assignment_24)
            .setContentTitle(context.getString(R.string.reminder_notification_title, titleText))
            .setContentText(noteText)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            // Add a share action
            .addAction(
                R.drawable.ic_baseline_share_24,
                "Share", shareIntent
            )

        notify(context, builder.build())

    }

    private fun notify(context: Context, notification: Notification) {
        val nm = context
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.notify(REMINDER_NOTIFICATION_ID, notification)
    }

    /**
     * Cancels any notifications of this type previously shown using
     * [.notify].
     */

    fun cancel(context: Context) {
        val nm = context
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.cancel(REMINDER_NOTIFICATION_ID)
    }

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val channel = NotificationChannel(
                REMINDER_CHANNEL_ID,
                "Note Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            notificationManager.createNotificationChannel(channel)
        }
    }
}