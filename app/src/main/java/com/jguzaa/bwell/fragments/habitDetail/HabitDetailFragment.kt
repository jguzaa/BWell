package com.jguzaa.bwell.fragments.habitDetail

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.jguzaa.bwell.R
import com.jguzaa.bwell.data.HABIT_DATE
import com.jguzaa.bwell.data.Habit
import com.jguzaa.bwell.data.HabitsType
import com.jguzaa.bwell.data.ONE_DAY_MILLIS
import com.jguzaa.bwell.data.local.HabitDatabase
import com.jguzaa.bwell.data.local.HabitDatabaseDao
import com.jguzaa.bwell.databinding.FragmentHabitDetailBinding
import java.util.*

class HabitDetailFragment : Fragment() {

    companion object {
        private const val TAG = "HabitDetail"
    }

    private var _binding: FragmentHabitDetailBinding? = null
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
        _binding = FragmentHabitDetailBinding.inflate(inflater, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = HabitDatabase.getInstance(application).habitDatabaseDao
        val viewModelFactory = HabitDetailViewModelFactory(dataSource, application, habitId)
        viewModel = ViewModelProvider(this, viewModelFactory).get(HabitDetailViewModel::class.java)
        binding.lifecycleOwner = this
        binding.habitDetailViewModel = viewModel

        viewModel.habit.observe(viewLifecycleOwner, {
            habit = it

            //set up detail
            when(habit.type){
                HabitsType.DAILY_TRACKING -> binding.habitIcon.setImageResource(R.drawable.own)
                HabitsType.JOGGING -> binding.habitIcon.setImageResource(R.drawable.jogging)
                HabitsType.ALARM_CLOCK -> binding.habitIcon.setImageResource(R.drawable.clock)
            }

            val dayLeft = HABIT_DATE - habit.streak

            if(dayLeft >= 0){
                binding.dayLeft.text = resources.getQuantityString(
                    R.plurals.day_left,
                    dayLeft,
                    dayLeft
                )
            } else {
                binding.dayLeft.text = getString(R.string.finished)
                binding.finishBtn.isEnabled = false
                binding.snoozeBtn.isEnabled = false
            }

            val width = binding.container.width

            val widthByPercent = width * habit.finishPercentages / 100

            val params = binding.progressBar.layoutParams
            params.width = widthByPercent
            binding.progressBar.layoutParams = params

            binding.completePercentage.text = getString(R.string.progress_text, habit.finishPercentages)

            //check and set snooze button status
            binding.snoozeBtn.isEnabled = !(habit.isSnoozed || habit.todayFinished)

        })

        binding.backBtn.setOnClickListener {
            findNavController().navigate(HabitDetailFragmentDirections.actionHabitDetailFragmentToDashboardFragment())
        }

        binding.snoozeBtn.setOnClickListener {
            val today = Calendar.getInstance().timeInMillis

            if( today - habit.lastDayFinished > 2 * ONE_DAY_MILLIS){

                val builder = AlertDialog.Builder(requireContext())
                //set title for alert dialog
                builder.setTitle(R.string.ask_to_confirm_title)
                //set message for alert dialog
                builder.setMessage(R.string.confirm_snooze_text)
                builder.setIcon(android.R.drawable.ic_dialog_alert)

                //performing positive action
                builder.setPositiveButton("Yes"){ _, _ ->
                    Log.d(TAG, "reset streak")
                    viewModel.snoozeAndResetStreak()
                }
                //performing cancel action
                builder.setNeutralButton("Cancel"){ _, _ ->
                    Log.d(TAG, "snooze cancel")
                }

                // Create the AlertDialog
                val alertDialog: AlertDialog = builder.create()
                // Set other dialog properties
                alertDialog.setCancelable(false)
                alertDialog.show()

            } else viewModel.snooze()

        }

        binding.customizeBtn.setOnClickListener {
            findNavController().navigate(HabitDetailFragmentDirections.actionHabitDetailFragmentToHabitCustomizeFragment(habit.habitId))
        }

        return binding.root
    }

}



class HabitDetailViewModelFactory (
    private val dataSource: HabitDatabaseDao,
    private val application: Application,
    private val habitId: Long
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HabitDetailViewModel::class.java)) {

            return HabitDetailViewModel(dataSource, application, habitId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}