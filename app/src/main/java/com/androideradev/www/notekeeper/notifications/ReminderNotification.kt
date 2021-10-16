package com.androideradev.www.notekeeper.notifications

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import com.androideradev.www.notekeeper.NOTE_ID
import com.androideradev.www.notekeeper.NoteActivity
import com.androideradev.www.notekeeper.R


object ReminderNotification {

    private const val REMINDER_NOTIFICATION_ID = 3322
    private const val REMINDER_CHANNEL_ID = "REMINDER_CHANNEL_ID"

    fun notify(context: Context, titleText: String, noteText: String, noteId: Int) {

        val startNoteActivity = Intent(context, NoteActivity::class.java)
        startNoteActivity.putExtra(NOTE_ID, noteId)

        val pendingIntent =
            TaskStackBuilder.create(context).addNextIntentWithParentStack(startNoteActivity)
                .getPendingIntent(REMINDER_NOTIFICATION_ID, PendingIntent.FLAG_UPDATE_CURRENT)

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
            .setContentIntent(
                pendingIntent
            )
            .setAutoCancel(true)

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

    //BigTextStyle
    fun notify(
        context: Context,
        collapsedTitle: String,
        collapsedBodyText: String,
        largeIcon: Int,
        bigText: String,
        bigContentTitle: String,
        summaryText: String,
        noteId: Int
    ) {

        val startNoteActivity = Intent(context, NoteActivity::class.java)
        startNoteActivity.putExtra(NOTE_ID, noteId)

        val pendingIntent =
            TaskStackBuilder.create(context).addNextIntentWithParentStack(startNoteActivity)
                .getPendingIntent(REMINDER_NOTIFICATION_ID, PendingIntent.FLAG_UPDATE_CURRENT)


        // collapsedTitle = "Collapsed Title"
        // collapsedBodyText = "Collapsed Body Text"
        val builder = NotificationCompat.Builder(context, REMINDER_CHANNEL_ID)
            .setDefaults(Notification.DEFAULT_ALL)
            .setSmallIcon(R.drawable.ic_baseline_assignment_24)

            .setContentTitle(collapsedTitle)
            .setContentText(collapsedBodyText)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, largeIcon))

            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            .setContentIntent(
                pendingIntent
            )
            .setAutoCancel(true)

            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(bigText) // Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse tempor dolor et odio porta dictum. Ut ex urna, imperdiet sed.
                    .setBigContentTitle(bigContentTitle) // Big Content Title
                    .setSummaryText(summaryText) // Summary Text
            )

        notify(context, builder.build())

    }

    // Big Picture Style
    fun notify(
        context: Context,
        titleText: String,
        noteText: String,
        bigPicture: Int,
        noteId: Int
    ) {

        val startNoteActivity = Intent(context, NoteActivity::class.java)
        startNoteActivity.putExtra(NOTE_ID, noteId)

        val pendingIntent =
            TaskStackBuilder.create(context).addNextIntentWithParentStack(startNoteActivity)
                .getPendingIntent(REMINDER_NOTIFICATION_ID, PendingIntent.FLAG_UPDATE_CURRENT)


        val builder = NotificationCompat.Builder(context, REMINDER_CHANNEL_ID)
            .setDefaults(Notification.DEFAULT_ALL)
            .setSmallIcon(R.drawable.ic_baseline_assignment_24)
            .setContentTitle(context.getString(R.string.reminder_notification_title, titleText))
            .setContentText(noteText)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, bigPicture))
            .setContentIntent(
                pendingIntent
            )
            .setAutoCancel(true)
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(BitmapFactory.decodeResource(context.resources, bigPicture))
                    .bigLargeIcon(null)
            )

        notify(context, builder.build())

    }

    // Inbox Style
    fun notify(context: Context, noteId: Int) {

        val startNoteActivity = Intent(context, NoteActivity::class.java)
        startNoteActivity.putExtra(NOTE_ID, noteId)

        val pendingIntent =
            TaskStackBuilder.create(context).addNextIntentWithParentStack(startNoteActivity)
                .getPendingIntent(REMINDER_NOTIFICATION_ID, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(context, REMINDER_CHANNEL_ID)
            .setDefaults(Notification.DEFAULT_ALL)
            .setSmallIcon(R.drawable.ic_baseline_assignment_24)
            .setContentTitle("5 New Messages")
            .setContentText("Review your messages")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            .setContentIntent(
                pendingIntent
            )
            .setAutoCancel(true)
            .setStyle(NotificationCompat.InboxStyle()
                .addLine("Your Taxes Are Due")
                .addLine("Free Cake This Tuesday")
                .addLine("Your Order Has Shipped")
                .addLine("Your Pluralsight")
                .addLine("Hey~! How are You?")

            )
        notify(context, builder.build())

    }
}