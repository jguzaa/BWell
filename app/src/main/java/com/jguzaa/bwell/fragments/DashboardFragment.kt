package com.jguzaa.bwell.fragments

import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.jguzaa.bwell.R
import com.jguzaa.bwell.databinding.FragmentDashboardBinding
import com.jguzaa.bwell.databinding.FragmentHomeBinding
import com.jguzaa.bwell.fragments.home.HomeFragment
import com.jguzaa.bwell.fragments.home.HomeViewModel

class DashboardFragment : Fragment() {

    companion object {
        private const val TAG = "DashboardFragment"
        private const val HOME = "HomeFragment"
        private const val ACCOUNT = "AccountFragment"
        private const val SETTING = "SettingFragment"
        private const val FRAGMENT_KEY = "CurrentFragment"
    }

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private var currentFragment = ""

    //setup fragments
    private val homeFragment = HomeFragment()
    private val settingFragment = SettingFragment()
    private val accountFragment = AccountFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //when the state is reset from view life cycle
        if(savedInstanceState != null){
            currentFragment = savedInstanceState.getString(FRAGMENT_KEY, "")
            setCurrentFragment(currentFragment)
        } else
            currentFragment = HOME
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //view binding
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        setCurrentFragment(currentFragment)

        //Set bottom bar onClickListener
        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.ic_home -> setCurrentFragment(HOME)
                R.id.ic_account -> setCurrentFragment(ACCOUNT)
                R.id.ic_setting -> setCurrentFragment(SETTING)
            }
            true
        }

        return binding.root
    }

    private fun setCurrentFragment(current: String) {

        currentFragment = current

        val fragment = when (current){
            HOME -> homeFragment
            ACCOUNT -> accountFragment
            SETTING -> settingFragment
            else -> homeFragment
        }

        childFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "onSaveInstanceState Called")
        outState.putString(FRAGMENT_KEY, currentFragment)
    }

}