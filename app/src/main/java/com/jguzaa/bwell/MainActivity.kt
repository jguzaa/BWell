package com.jguzaa.bwell

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.jguzaa.bwell.databinding.ActivityMainBinding
import com.jguzaa.bwell.fragments.AccountFragment
import com.jguzaa.bwell.fragments.HomeFragment
import com.jguzaa.bwell.fragments.SettingFragment

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    //setup fragments
    private val homeFragment = HomeFragment()
    private val settingFragment = SettingFragment()
    private val accountFragment = AccountFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }
}