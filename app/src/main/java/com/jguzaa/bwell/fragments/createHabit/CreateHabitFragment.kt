package com.jguzaa.bwell.fragments.createHabit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.jguzaa.bwell.data.Habit
import com.jguzaa.bwell.data.local.HabitDatabase
import com.jguzaa.bwell.databinding.FragmentCreateHabitBinding
import com.jguzaa.bwell.fragments.home.HomeViewModel
import com.jguzaa.bwell.fragments.home.HomeViewModelFactory

class CreateHabitFragment : Fragment() {

    companion object {
        private const val TAG = "CreateHabitFragment"
    }

    private var _binding: FragmentCreateHabitBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //view binding
        _binding = FragmentCreateHabitBinding.inflate(inflater, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = HabitDatabase.getInstance(application).habitDatabaseDao
        val viewModelFactory = CreateHabitVewModelFactory(dataSource, application)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(CreateHabitViewModel::class.java)
        binding.lifecycleOwner = this
        binding.createHabitViewModel = viewModel

        binding.button.setOnClickListener {
            val newHabit = Habit()
            viewModel.addHabit(newHabit)

        }

        binding.button2.setOnClickListener {
            viewModel.onClear()
        }

        return binding.root
    }

}