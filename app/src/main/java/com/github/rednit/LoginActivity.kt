package com.github.rednit

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.github.rednit.databinding.ActivityLoginBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.elevation = 0f

        val sharedPreferences = getSharedPreferences("rednit", Context.MODE_PRIVATE)

        val connection = TinderConnection.connection
        connection.token = sharedPreferences.getString("token", "").toString()

        switchLogin(true)

        lifecycleScope.launch {
            if (withContext(Dispatchers.IO) { connection.login() }) {
                applicationContext.startActivity(
                    Intent(applicationContext, MainActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                )
            } else {
                switchLogin(false)
            }
        }

        binding.buttonLogin.setOnClickListener {
            val token = binding.inputPassword.text.toString()
            TinderConnection.connection.token = token
            switchLogin(true)

            lifecycleScope.launch {
                if (TinderConnection.connection.login()) {
                    sharedPreferences.edit().putString("token", token).apply()
                    applicationContext.startActivity(
                        Intent(applicationContext, MainActivity::class.java)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    )
                } else {
                    switchLogin(false)
                    Toast.makeText(
                        applicationContext,
                        "Invalid X-Auth-Token!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        binding.textGetToken.setOnClickListener {
            applicationContext.startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.wiki_token)))
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        }
    }


    private fun switchLogin(active: Boolean) {
        if (active) {
            binding.progressBar.isVisible = true
            binding.buttonLogin.isEnabled = false
            binding.layoutLogin.alpha = 0.5f
        } else {
            binding.progressBar.isVisible = false
            binding.buttonLogin.isEnabled = true
            binding.layoutLogin.alpha = 1.0f
        }
    }

}

