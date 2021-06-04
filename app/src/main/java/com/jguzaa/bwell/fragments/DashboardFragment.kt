package com.jguzaa.bwell.fragments

import android.os.Bundle
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

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    //setup fragments
    private val homeFragment = HomeFragment()
    private val settingFragment = SettingFragment()
    private val accountFragment = AccountFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //view binding
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        setCurrentFragment(homeFragment)

        //Set bottom bar onClickListener
        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.ic_home -> setCurrentFragment(homeFragment)
                R.id.ic_account -> setCurrentFragment(accountFragment)
                R.id.ic_setting -> setCurrentFragment(settingFragment)
            }
            true
        }

        return binding.root
    }

    private fun setCurrentFragment(fragment: Fragment) {

        childFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}