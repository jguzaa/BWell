package com.jguzaa.bwell.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.jguzaa.bwell.LoginActivity
import com.jguzaa.bwell.R
import com.jguzaa.bwell.databinding.FragmentAccountBinding
import com.jguzaa.bwell.databinding.FragmentHomeBinding

class AccountFragment : Fragment() {

    lateinit var binding: FragmentAccountBinding
    lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //view binding
        binding = FragmentAccountBinding.inflate(layoutInflater, container, false)

        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser

        binding.name.text = currentUser!!.displayName
        binding.email.text = currentUser.email
        Glide.with(this).load(currentUser.photoUrl).into(binding.accountImg)

        binding.signOutButton.setOnClickListener {
            mAuth.signOut()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            activity?.finish()
        }

        // Inflate the layout for this fragment
        return binding.root
    }

}