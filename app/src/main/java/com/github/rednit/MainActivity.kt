package com.github.rednit

import android.Manifest
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.github.rednit.databinding.ActivityMainBinding
import com.github.rednit.fragments.ChatFragment
import com.github.rednit.fragments.HistoryFragment
import com.github.rednit.fragments.SettingsFragment
import com.github.rednit.fragments.SwipeFragment
import com.github.rednit.likes.LikeFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.elevation = 0f
        val historyFragment = HistoryFragment()
        val likeFragment = LikeFragment()
        val swipeFragment = SwipeFragment()
        val chatFragment = ChatFragment()
        val settingsFragment = SettingsFragment()

        setFragment(swipeFragment)
        binding.bottomNavigation.selectedItemId = R.id.ic_swipe

        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.ic_history -> setFragment(historyFragment)
                R.id.ic_likes -> setFragment(likeFragment)
                R.id.ic_swipe -> setFragment(swipeFragment)
                R.id.ic_chat -> setFragment(chatFragment)
                R.id.ic_settings -> setFragment(settingsFragment)
            }
            true
        }

        requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.notifications_menu, menu)
        menuInflater.inflate(R.menu.account_menu, menu)
        return true
    }

    private fun setFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }
}