package com.jguzaa.bwell.fragments.setting

import android.app.Application
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jguzaa.bwell.LoginActivity
import com.jguzaa.bwell.R
import com.jguzaa.bwell.data.local.HabitDatabase
import com.jguzaa.bwell.data.local.HabitDatabaseDao
import com.jguzaa.bwell.databinding.FragmentSettingBinding
import com.jguzaa.bwell.util.FAIL_CODE
import com.jguzaa.bwell.util.LOADING_CODE
import com.jguzaa.bwell.util.SUCCESS_CODE
import com.jguzaa.bwell.util.StatusCallback

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
            builder.apply {
                //set title for alert dialog
                setTitle(R.string.ask_to_confirm_title)
                //set message for alert dialog
                setMessage(R.string.confirm_delete_text)
                setIcon(android.R.drawable.ic_dialog_alert)
                //performing positive action
                setPositiveButton("Yes"){ _, _ ->
                    viewModel.resetAll()
                    viewModel.signOut()
                    startActivity(Intent(requireContext(), LoginActivity::class.java))
                    activity?.finish()
                }
                //performing cancel action
                setNeutralButton("Cancel"){ _, _ -> }
            }

            // Create the AlertDialog
            val alertDialog: AlertDialog = builder.create()
            // Set other dialog properties
            alertDialog.apply {
                setCancelable(false)
                show()
            }

        }

        binding.backupTab.setOnClickListener {

            viewModel.backupToFirebase(object: StatusCallback{
                override fun onCallback(status: Int) {

                    when (status){
                        SUCCESS_CODE -> Toast.makeText(requireContext(), getString(R.string.backup_success), Toast.LENGTH_SHORT).show()
                        FAIL_CODE -> Toast.makeText(requireContext(), getString(R.string.backup_fail), Toast.LENGTH_SHORT).show()
                        else -> Toast.makeText(requireContext(), getString(R.string.error), Toast.LENGTH_SHORT).show()
                    }

                }
            })

        }

        binding.restoreTab.setOnClickListener {

            val builder = AlertDialog.Builder(requireContext())
            builder.apply {
                //set title for alert dialog
                setTitle(R.string.ask_to_confirm_title)
                //set message for alert dialog
                setMessage(R.string.restore_confirm)
                setIcon(android.R.drawable.ic_dialog_alert)
                //performing positive action
                setPositiveButton("Yes"){ _, _ ->

                    viewModel.restoreFromFirebase(object: StatusCallback{
                        override fun onCallback(status: Int) {

                            when (status){
                                SUCCESS_CODE -> Toast.makeText(requireContext(), getString(R.string.restore_success), Toast.LENGTH_SHORT).show()
                                FAIL_CODE -> Toast.makeText(requireContext(), getString(R.string.restore_fail), Toast.LENGTH_SHORT).show()
                                else -> Toast.makeText(requireContext(), getString(R.string.error), Toast.LENGTH_SHORT).show()
                            }

                        }
                    })

                }
                //performing cancel action
                setNeutralButton("Cancel"){ _, _ -> }
            }

            // Create the AlertDialog
            val alertDialog: AlertDialog = builder.create()
            // Set other dialog properties
            alertDialog.apply {
                setCancelable(false)
                show()
            }

        }

        viewModel.isLoading.observe(viewLifecycleOwner,{
            if(it)
                binding.loading.visibility = View.VISIBLE
            else
                binding.loading.visibility = View.GONE
        })

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