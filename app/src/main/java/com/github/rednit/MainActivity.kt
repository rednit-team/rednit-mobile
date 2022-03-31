package com.github.rednit

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.github.rednit.databinding.ActivityMainBinding
import com.github.rednit.fragments.ChatFragment
import com.github.rednit.fragments.HistoryFragment
import com.github.rednit.fragments.SettingsFragment
import com.github.rednit.fragments.SwipeFragment
import com.github.rednit.likes.LikeFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.CompletionException

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

        lifecycleScope.launch {
            val teaserCount =
                withContext(Dispatchers.IO) { TinderConnection.connection.teaserCount() }
            if (teaserCount > 0) {
                val badge = binding.bottomNavigation.getOrCreateBadge(R.id.ic_likes)
                badge.number = teaserCount
                badge.verticalOffset = 15
                badge.horizontalOffset = 15
                badge.backgroundColor = getColor(R.color.secondary)
                badge.badgeTextColor = getColor(R.color.badgeText)
            }
        }

        Thread.setDefaultUncaughtExceptionHandler { _, throwable ->
            if (throwable is CompletionException) {
                Toast.makeText(
                    applicationContext,
                    "Your X-Auth-Token expired!", Toast.LENGTH_LONG
                ).show()

                applicationContext.startActivity(
                    Intent(applicationContext, LoginActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                )
            }
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
            replace(R.id.container, fragment)
            commit()
        }
}