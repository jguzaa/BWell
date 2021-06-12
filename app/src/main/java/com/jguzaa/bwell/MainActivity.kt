package com.jguzaa.bwell

import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment

import com.jguzaa.bwell.databinding.ActivityMainBinding
import com.jguzaa.bwell.fragments.AccountFragment
import com.jguzaa.bwell.fragments.DashboardFragment
import com.jguzaa.bwell.fragments.home.HomeFragment
import com.jguzaa.bwell.fragments.SettingFragment

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    companion object {
        //fun newInstance() = DashboardFragment()
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        //view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        //create channel
        createChannel(
            getString(R.string.habit_notification_channel_id),
            getString(R.string.habit_notification_channel_name)
        )

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

            val notificationManager = this.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }

    }

}