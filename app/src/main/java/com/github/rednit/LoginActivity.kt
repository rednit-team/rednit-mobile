package com.github.rednit

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.github.rednit.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.elevation = 0f

        val sharedPreferences = getSharedPreferences("rednit", Context.MODE_PRIVATE)

        TinderConnection.token = sharedPreferences.getString("token", "").toString()

        switchLogin(true)
        Thread {
            if (TinderConnection.login()) {
                runOnUiThread {
                    applicationContext.startActivity(
                        Intent(
                            applicationContext,
                            MainActivity::class.java
                        ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    )
                }
            } else {
                runOnUiThread {
                    switchLogin(false)
                }
            }
        }.start()

        binding.buttonLogin.setOnClickListener {
            val token = binding.inputPassword.text.toString()
            TinderConnection.token = token

            switchLogin(true)
            Thread {
                if (TinderConnection.login()) {
                    sharedPreferences.edit().putString("token", token).apply()
                    runOnUiThread {
                        applicationContext.startActivity(
                            Intent(
                                applicationContext,
                                MainActivity::class.java
                            ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        )
                    }
                } else {
                    runOnUiThread {
                        switchLogin(false)
                        Toast.makeText(
                            applicationContext,
                            "Invalid X-Auth-Token!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }.start()
        }

        binding.textGetToken.setOnClickListener {
            applicationContext.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://rednit.stoplight.io/docs/tinder-api/ZG9jOjIyMTk3ODgx-authentication")
                ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
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

