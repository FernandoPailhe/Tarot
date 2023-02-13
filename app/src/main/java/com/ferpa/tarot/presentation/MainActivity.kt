package com.ferpa.tarot.presentation

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.preferencesDataStore
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.ferpa.tarot.Notifications
import com.ferpa.tarot.Notifications.Companion.MESSAGE_EXTRA
import com.ferpa.tarot.Notifications.Companion.NOTIFICATION_ID
import com.ferpa.tarot.R
import com.ferpa.tarot.data.repository.TarotRepositoryImpl
import com.ferpa.tarot.data.repository.TarotRepositoryImpl.Companion.PREFERENCES_NAME
import com.ferpa.tarot.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val Context.dataStore by preferencesDataStore(name = PREFERENCES_NAME)

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "notification_channel"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val toolbar = binding.toolbar
        setContentView(binding.root)
        setSupportActionBar(toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun onStop() {
        createChannel()
        val scope = CoroutineScope(Dispatchers.Default)
        val context = this@MainActivity.applicationContext
        scope.launch {
            val lastGameDate = getLastGameDate(context)
            if (lastGameDate != null) {
                scheduleNotification(
                    context.getString(R.string.after_result),
                    lastGameDate + TimeUnit.DAYS.toMillis(1)
                )
            } else {
                scheduleNotification(
                    context.getString(R.string.after_install),
                    Calendar.getInstance().timeInMillis + TimeUnit.DAYS.toMillis(1)
                )
            }
        }
        super.onStop()
    }

    private suspend fun getLastGameDate(context: Context): Long? {
        return context.dataStore.data.first()[TarotRepositoryImpl.LAST_GAME_DATE]
    }

    /**
     * Schedules a notification to be shown to the user.
     * @param message: a string value representing the message to be displayed in the notification.
     * @param trigger: a long value representing the trigger time for the notification to be shown, in milliseconds.
     */
    private fun scheduleNotification(message: String, trigger: Long) {
        val intent = Intent(applicationContext, Notifications::class.java)
        intent.putExtra(MESSAGE_EXTRA, message)
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            trigger,
            pendingIntent
        )
    }

    /**
     * Create Notification Channel
     */
    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                getString(R.string.app_name),
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = getString(R.string.after_result)
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel((channel))
        }
    }
}