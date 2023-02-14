package com.ferpa.tarot

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.ferpa.tarot.presentation.MainActivity

@Suppress("DEPRECATION")
class Notifications :
    BroadcastReceiver() {

    companion object {
        const val NOTIFICATION_ID = 1
        const val MESSAGE_EXTRA = "message"
    }

    /**
     * Function to receive and process the broadcast
     *
     * @param context: Context
     * @param intent: Intent?
     */
    override fun onReceive(context: Context, intent: Intent?) {
        // Get the message from the intent or set a default message
        val message = intent?.getSerializableExtra(MESSAGE_EXTRA) as? String ?: context.getString(R.string.after_install)
        // Call the createNotification function to create and show the notification
        createNotification(context, message)
    }


    /**
     * Creates a notification with the provided context and message
     *
     * @param context Context of the application
     * @param message Message to display in the notification
     */
    private fun createNotification(context: Context, message: String) {
        // Create an intent for the MainActivity and set flags for the intent to clear the task and start a new task.
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        // Create a pending intent using the above intent, with the IMMUTABLE flag set.
        val flag = PendingIntent.FLAG_IMMUTABLE
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, flag)

        // Create a notification builder with the provided context, channel ID, and small icon
        val notification =
            NotificationCompat.Builder(context, MainActivity.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build()

        // Get the notification manager service and notify using the created notification and a unique ID
        val manager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, notification)
    }

}


