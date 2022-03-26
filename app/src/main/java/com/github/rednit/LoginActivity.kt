package com.github.rednit

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.github.rednit.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.elevation = 0f

        val sharedPreferences = getSharedPreferences("rednit", Context.MODE_PRIVATE)

        TinderConnection.token = sharedPreferences.getString("token", "").toString()

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
            }
        }.start()

        binding.buttonLogin.setOnClickListener {
            val token = binding.inputPassword.text.toString()
            TinderConnection.token = token

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
                        Toast.makeText(
                            applicationContext,
                            "Invalid X-Auth-Token!",
                            Toast.LENGTH_LONG
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


}

