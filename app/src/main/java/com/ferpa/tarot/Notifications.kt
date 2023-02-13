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

    override fun onReceive(context: Context, p1: Intent?) {
        val message = p1?.getSerializableExtra(MESSAGE_EXTRA) as? String ?: context.getString(R.string.after_install)
        createNotification(context, message)
    }

    private fun createNotification(context: Context, message: String) {

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val flag = PendingIntent.FLAG_IMMUTABLE
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, flag)


        val notification =
            NotificationCompat.Builder(context, MainActivity.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build()

        val manager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        manager.notify(NOTIFICATION_ID, notification)
    }

}


