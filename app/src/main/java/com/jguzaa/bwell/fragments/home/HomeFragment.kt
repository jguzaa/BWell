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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.jguzaa.bwell.data.local.HabitDatabase

class HomeFragment : Fragment() {

    companion object {
        private const val TAG = "HomeFragment"
    }

    private val viewModel : HomeViewModel by viewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView


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

        }

        // Inflate the layout for this fragment
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.habitList
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}