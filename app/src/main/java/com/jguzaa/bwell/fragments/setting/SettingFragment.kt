package com.jguzaa.bwell.fragments.setting

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.jguzaa.bwell.LoginActivity
import com.jguzaa.bwell.R
import com.jguzaa.bwell.data.Habit
import com.jguzaa.bwell.data.local.HabitDatabase
import com.jguzaa.bwell.data.local.HabitDatabaseDao
import com.jguzaa.bwell.databinding.FragmentCreateHabitBinding
import com.jguzaa.bwell.databinding.FragmentHabitDetailBinding
import com.jguzaa.bwell.databinding.FragmentSettingBinding
import com.jguzaa.bwell.fragments.HabitCustomizeFragment
import com.jguzaa.bwell.fragments.HabitCustomizeFragmentDirections
import com.jguzaa.bwell.fragments.createHabit.CreateHabitViewModel
import com.jguzaa.bwell.fragments.createHabit.CreateHabitViewModelFactory
import com.jguzaa.bwell.fragments.habitDetail.HabitDetailViewModel

class SettingFragment : Fragment() {

    companion object {
        private const val TAG = "SettingFragment"
    }

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SettingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //view binding
        _binding = FragmentSettingBinding.inflate(inflater, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = HabitDatabase.getInstance(application).habitDatabaseDao
        val viewModelFactory = SettingViewModelFactory(dataSource, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(SettingViewModel::class.java)
        binding.lifecycleOwner = this
        binding.settingViewModel = viewModel

        binding.resetTab.setOnClickListener {

            val builder = AlertDialog.Builder(requireContext())
            //set title for alert dialog
            builder.setTitle(R.string.ask_to_confirm_title)
            //set message for alert dialog
            builder.setMessage(R.string.confirm_delete_text)
            builder.setIcon(android.R.drawable.ic_dialog_alert)

            //performing positive action
            builder.setPositiveButton("Yes"){ _, _ ->
                viewModel.resetAll()
                startActivity(Intent(requireContext(), LoginActivity::class.java))
                activity?.finish()
            }
            //performing cancel action
            builder.setNeutralButton("Cancel"){ _, _ -> }

            // Create the AlertDialog
            val alertDialog: AlertDialog = builder.create()
            // Set other dialog properties
            alertDialog.setCancelable(false)
            alertDialog.show()
        }

        return binding.root
    }

}

class SettingViewModelFactory (
    private val dataSource: HabitDatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            return SettingViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}