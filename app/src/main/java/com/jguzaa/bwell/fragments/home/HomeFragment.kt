package com.jguzaa.bwell.fragments.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.jguzaa.bwell.R
import com.jguzaa.bwell.databinding.ActivityMainBinding
import com.jguzaa.bwell.databinding.FragmentHomeBinding
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jguzaa.bwell.data.local.HabitDatabase

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
            findNavController().navigate(R.id.action_dashboardFragment_to_createHabitFragment)
        }

        val manager = LinearLayoutManager(activity)
        binding.habitList.layoutManager = manager

        val adapter = HabitAdapter(HabitListener { habitId ->
            Log.d(TAG, "list tabbed = $habitId")
        })
        binding.habitList.adapter = adapter

        viewModel.habits.observe(viewLifecycleOwner, Observer {
            it?.let{
                adapter.submitList(it)
            }
        })

        // Inflate the layout for this fragment
        return binding.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}