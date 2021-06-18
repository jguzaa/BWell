package com.jguzaa.bwell.fragments.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jguzaa.bwell.R
import com.jguzaa.bwell.data.local.HabitDatabase
import com.jguzaa.bwell.databinding.FragmentHomeBinding
import com.jguzaa.bwell.fragments.DashboardFragment
import com.jguzaa.bwell.fragments.DashboardFragmentDirections

class HomeFragment : Fragment() {

    companion object {
        private const val TAG = "HomeFragment"
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //view binding
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = HabitDatabase.getInstance(application).habitDatabaseDao
        val viewModelFactory = HomeViewModelFactory(dataSource, application)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
        binding.lifecycleOwner = this
        binding.homeViewModel = viewModel

        //when fab clicked, direct to create habit activity
        binding.fabButton.setOnClickListener {
            findNavController().navigate(DashboardFragmentDirections.actionDashboardFragmentToCreateHabitFragment())
        }

        val manager = LinearLayoutManager(activity)
        binding.habitList.layoutManager = manager

        val adapter = HabitAdapter(HabitListener { habitId ->
            Log.d(TAG, "list tabbed = $habitId")
            viewModel.onHabitClicked(habitId)
        })
        binding.habitList.adapter = adapter

        viewModel.habits.observe(viewLifecycleOwner, {
            it?.let{
                adapter.submitList(it)
            }
        })

        //Navigate to habitDetail when liveData changed
        viewModel.navigateToHabitDetail.observe(viewLifecycleOwner, {
                habitId -> habitId?.let{
                    findNavController().navigate(DashboardFragmentDirections.actionDashboardFragmentToHabitDetailFragment(habitId))
                    viewModel.resetNavigate()
                }
        })

        // Inflate the layout for this fragment
        return binding.root

    }

}