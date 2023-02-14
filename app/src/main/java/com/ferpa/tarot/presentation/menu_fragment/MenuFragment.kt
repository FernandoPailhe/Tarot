package com.ferpa.tarot.presentation.menu_fragment

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ferpa.tarot.Notifications
import com.ferpa.tarot.R
import com.ferpa.tarot.databinding.FragmentMenuBinding
import com.ferpa.tarot.domain.model.TarotMenuItem
import com.ferpa.tarot.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MenuFragment : Fragment(R.layout.fragment_menu) {

    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MenuViewModel by viewModels()
    private lateinit var tarotMenuAdapter: TarotMenuAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeLastTarotReading()

        setTarotMenuInterface()

    }

    private fun subscribeLastTarotReading() {
        viewModel.lastTarotReadingDate.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it == null) {
                scheduleNotification(
                    getString(R.string.after_install),
                    Calendar.getInstance().timeInMillis + TimeUnit.DAYS.toMillis(1),
                    binding.root.context
                )
            }
        })
    }

    /**
     * Set Tarot Menu Interface with default menu
     */
    private fun setTarotMenuInterface() {
        setUpTarotMenuRecyclerView()
        setTarotMenuAdapter(
            listOf(
                TarotMenuItem(
                    title = getString(R.string.menu_title_1),
                    description = getString(R.string.menu_description_1),
                    numberOfCards = 1
                ),
                TarotMenuItem(
                    title = getString(R.string.menu_title_2),
                    description = getString(R.string.menu_description_2),
                    numberOfCards = 3
                ),
                TarotMenuItem(
                    title = getString(R.string.menu_title_3),
                    description = getString(R.string.menu_description_3),
                    numberOfCards = 5
                )
            )
        )
    }

    /**
     * Set initial configuration for Tarot Menu Recycler View
     */
    private fun setUpTarotMenuRecyclerView() {
        binding.rvMenuList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
        }
    }

    /**
     * Set Tarot Menu Adapter
     */
    private fun setTarotMenuAdapter(menu: List<TarotMenuItem>) {
        tarotMenuAdapter = TarotMenuAdapter(
            onItemClicked = { tarotMenuItem ->
                if (tarotMenuItem.numberOfCards == 5) {
                    val action = MenuFragmentDirections.actionMenuFragmentToGameFragment()
                    this.findNavController().navigate(action)
                }
            }
        )
        tarotMenuAdapter.submitList(menu)
        binding.rvMenuList.adapter = tarotMenuAdapter
    }

    /**
     * Schedules a notification to be shown to the user.
     * @param message: a string value representing the message to be displayed in the notification.
     * @param trigger: a long value representing the trigger time for the notification to be shown, in milliseconds.
     */
    private fun scheduleNotification(message: String, trigger: Long, context: Context) {
        createChannel()
        val intent = Intent(context, Notifications::class.java)
        intent.putExtra(Notifications.MESSAGE_EXTRA, message)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            Notifications.NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
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
                MainActivity.NOTIFICATION_CHANNEL_ID,
                getString(R.string.app_name),
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = getString(R.string.after_result)
            }

            val notificationManager: NotificationManager =
                activity?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel((channel))
        }
    }
}