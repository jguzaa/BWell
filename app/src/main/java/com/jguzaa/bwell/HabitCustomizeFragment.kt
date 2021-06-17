package com.jguzaa.bwell

import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.jguzaa.bwell.data.Habit
import com.jguzaa.bwell.data.local.HabitDatabase
import com.jguzaa.bwell.databinding.FragmentHabitCustomizeBinding
import com.jguzaa.bwell.fragments.habitDetail.HabitDetailFragmentArgs
import com.jguzaa.bwell.fragments.habitDetail.HabitDetailViewModel
import com.jguzaa.bwell.fragments.habitDetail.HabitDetailViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class HabitCustomizeFragment : Fragment(), OnTimeSetListener {

    companion object {
        private const val TAG = "HabitCustomize"
    }

    private var hour = 0
    private var minute = 0

    private var _binding: FragmentHabitCustomizeBinding? = null
    private val binding get() = _binding!!
    private lateinit var habit: Habit

    private lateinit var viewModel: HabitDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //get habitId from bundle
        val habitId = HabitDetailFragmentArgs.fromBundle(requireArguments()).habitId
        Log.d(TAG, "habitID = $habitId")

        //view binding
        _binding = FragmentHabitCustomizeBinding.inflate(inflater, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = HabitDatabase.getInstance(application).habitDatabaseDao
        val viewModelFactory = HabitDetailViewModelFactory(dataSource, application, habitId)
        viewModel = ViewModelProvider(this, viewModelFactory).get(HabitDetailViewModel::class.java)
        binding.lifecycleOwner = this
        binding.habitDetailViewModel = viewModel

        viewModel.habit.observe(viewLifecycleOwner, {
            habit = it
            val time = SimpleDateFormat("HH:mm").format(habit.notificationTime).toString()
            binding.notificationTimeCustomize.text = getString(R.string.notification_time, time)
            binding.habitTitle.text = getString(R.string.customize_title, habit.name)
        })

        binding.backBtn.setOnClickListener {
            findNavController().navigate(HabitCustomizeFragmentDirections.actionHabitCustomizeFragmentToHabitDetailFragment(habit.habitId))
        }

        binding.timeChangeTab.setOnClickListener {
            getCurrentTime()
            TimePickerDialog(context, this, hour, minute, true).show()
        }

        binding.deleteTab.setOnClickListener {

            val builder = AlertDialog.Builder(requireContext())
            //set title for alert dialog
            builder.setTitle(R.string.ask_to_confirm_title)
            //set message for alert dialog
            builder.setMessage(R.string.confirm_delete_text)
            builder.setIcon(android.R.drawable.ic_dialog_alert)

            //performing positive action
            builder.setPositiveButton("Yes"){ _, _ ->
                Log.d(TAG, "habit deleted")
                viewModel.deleteHabit()
                findNavController().navigate(HabitCustomizeFragmentDirections.actionHabitCustomizeFragmentToDashboardFragment())
            }
            //performing cancel action
            builder.setNeutralButton("Cancel"){ _, _ ->
                Log.d(TAG, "cancel delete")
            }

            // Create the AlertDialog
            val alertDialog: AlertDialog = builder.create()
            // Set other dialog properties
            alertDialog.setCancelable(false)
            alertDialog.show()
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun getCurrentTime() {
        val cal = Calendar.getInstance()
        hour = cal.get(Calendar.HOUR_OF_DAY)
        minute = cal.get(Calendar.MINUTE)

    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        viewModel.updateNotificationTime(hourOfDay, minute)
    }

}