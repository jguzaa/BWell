package com.jguzaa.bwell

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.jguzaa.bwell.databinding.ActivityMainBinding
import com.jguzaa.bwell.databinding.ActivitySplashScreenBinding

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //view binding
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser

        //create channel
        createChannel(
            getString(R.string.habit_notification_channel_id),
            getString(R.string.habit_notification_channel_name)
        )

        binding.logo.alpha = 0f
        binding.logo.animate().setDuration(1500).alpha(1f).withEndAction(){

            val i = if(user != null)
                Intent(this, MainActivity::class.java)
            else
                Intent(this, LoginActivity::class.java)

            startActivity(i)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }

    }

    private fun createChannel(channelId: String, channelName: String) {

        Log.d("SplashScreenActivity", "Channel created")

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