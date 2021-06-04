package com.jguzaa.bwell.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jguzaa.bwell.R
import com.jguzaa.bwell.databinding.FragmentAccountBinding
import com.jguzaa.bwell.databinding.FragmentHomeBinding

class AccountFragment : Fragment() {

    lateinit var binding: FragmentAccountBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //view binding
        binding = FragmentAccountBinding.inflate(layoutInflater, container, false)

        // Inflate the layout for this fragment
        return binding.root
    }

}