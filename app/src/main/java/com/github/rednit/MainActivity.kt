package com.github.rednit

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.github.rednit.databinding.ActivityMainBinding
import com.github.rednit.fragments.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val historyFragment = HistoryFragment()
        val likeFragment = LikeFragment()
        val swipeFragment = SwipeFragment()
        val chatFragment = ChatFragment()
        val settingsFragment = SettingsFragment()

        setFragment(swipeFragment)
        binding.bottomNavigation.selectedItemId = R.id.ic_swipe

        binding.bottomNavigation.setOnItemSelectedListener() {
            when (it.itemId) {
                R.id.ic_history -> setFragment(historyFragment)
                R.id.ic_likes -> setFragment(likeFragment)
                R.id.ic_swipe -> setFragment(swipeFragment)
                R.id.ic_chat -> setFragment(chatFragment)
                R.id.ic_settings -> setFragment(settingsFragment)
            }
            true
        }
    }

    private fun setFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }
}