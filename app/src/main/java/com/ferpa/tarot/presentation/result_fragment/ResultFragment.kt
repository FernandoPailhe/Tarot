package com.ferpa.tarot.presentation.result_fragment

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ferpa.tarot.Notifications
import com.ferpa.tarot.R
import com.ferpa.tarot.databinding.FragmentResultBinding
import com.ferpa.tarot.domain.model.Card
import com.ferpa.tarot.presentation.game_fragment.GameViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class ResultFragment : Fragment(R.layout.fragment_result) {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GameViewModel by activityViewModels()
    private lateinit var resultAdapter: ResultAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeIsFirstTarotReading()
        setResultRecyclerView()
        subscribeResult()

    }

    /**
     * This function subscribes to a `LiveData` object, `isFirstTarotReading`, from the view model and observes for changes in its value.
     *
     * When the value of `isFirstTarotReading` changes to `true`, the function schedules a notification to be triggered. The notification's
     * message is retrieved from a string resource with the key `R.string.after_result`, and the trigger time is set to one day from the current
     * time. The context required for scheduling the notification is obtained from the view's root.
     *
     * @see viewModel.isFirstTarotReading
     * @see scheduleNotification
     * @see getString
     */
    private fun subscribeIsFirstTarotReading(){
        viewModel.isFirstTarotReading.observe(viewLifecycleOwner, Observer { isFirstTarotReading ->
            if (isFirstTarotReading) {
                scheduleNotification(
                    getString(R.string.after_result),
                    Calendar.getInstance().timeInMillis + TimeUnit.DAYS.toMillis(1),
                    binding.root.context
                )
            }
        })
    }

    private fun subscribeResult() {
        viewModel.selectedCards.observe(viewLifecycleOwner, Observer { selectedCards ->
            setResultAdapter(selectedCards)
        })
    }

    /**
     * Set initial configuration for Tarot Menu Recycler View
     */
    private fun setResultRecyclerView() {
        binding.rvResult.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
        }
    }

    /**
     * Set Result Adapter
     */
    private fun setResultAdapter(result: List<Card>) {
        resultAdapter = ResultAdapter()
        resultAdapter.submitList(result)
        binding.rvResult.adapter = resultAdapter
    }

    /**
     * Schedules a notification to be shown to the user.
     *
     * @param message: a string value representing the message to be displayed in the notification.
     * @param trigger: a long value representing the trigger time for the notification to be shown, in milliseconds.
     */
    private fun scheduleNotification(message: String, trigger: Long, context: Context) {
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

}