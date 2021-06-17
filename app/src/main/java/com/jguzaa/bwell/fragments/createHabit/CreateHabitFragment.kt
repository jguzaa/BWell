package com.jguzaa.bwell.fragments.createHabit

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.jguzaa.bwell.data.local.HabitDatabase
import com.jguzaa.bwell.databinding.FragmentCreateHabitBinding
import java.util.*

class CreateHabitFragment : Fragment(),TimePickerDialog.OnTimeSetListener {

    companion object {
        private const val TAG = "CreateHabitFragment"
    }

    private var _binding: FragmentCreateHabitBinding? = null
    private val binding get() = _binding!!

    private var hour = 0
    private var minute = 0

    private lateinit var viewModel: CreateHabitViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //view binding
        _binding = FragmentCreateHabitBinding.inflate(inflater, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = HabitDatabase.getInstance(application).habitDatabaseDao
        val viewModelFactory = CreateHabitViewModelFactory(dataSource, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(CreateHabitViewModel::class.java)
        binding.lifecycleOwner = this
        binding.createHabitViewModel = viewModel

        binding.timePickerBtn.setOnClickListener {
            getCurrentTime()
            TimePickerDialog(context, this, hour, minute, true).show()
        }

        binding.habitName.doOnTextChanged { text, _, _, _ ->
            viewModel.habit.name = text.toString()
        }

        binding.addBtn.setOnClickListener {
            viewModel.createHabit()
            findNavController().navigate(CreateHabitFragmentDirections.actionCreateHabitFragmentToDashboardFragment())
        }

        //when back btn clicked
        binding.backBtn.setOnClickListener {
            findNavController().navigate(CreateHabitFragmentDirections.actionCreateHabitFragmentToDashboardFragment())
        }

        return binding.root
    }

    private fun getCurrentTime() {
        val cal = Calendar.getInstance()
        hour = cal.get(Calendar.HOUR_OF_DAY)
        minute = cal.get(Calendar.MINUTE)

    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        viewModel.updateCurrentTime(hourOfDay, minute)
        binding.timeSet.text = "$hourOfDay : $minute"
    }

}