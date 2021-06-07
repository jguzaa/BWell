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
        //fun newInstance() = DashboardFragment()
        private const val TAG = "DashboardFragment"
    }

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    //setup fragments
    private val homeFragment = HomeFragment()
    private val settingFragment = SettingFragment()
    private val accountFragment = AccountFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //view binding
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        setCurrentFragment(homeFragment)

        //create channel
        createChannel(
            getString(R.string.habit_notification_channel_id),
            getString(R.string.habit_notification_channel_name)
        )

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

    private fun createChannel(channelId: String, channelName: String) {

        Log.d(TAG, "Channel created")

        //START create a channel
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Habit"
            notificationChannel.setShowBadge(false)

            val notificationManager = requireActivity().getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }

    }

}