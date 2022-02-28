package com.github.rednit

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.rednit.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.elevation = 0f

        binding.buttonLogin.setOnClickListener {
            applicationContext.startActivity(
                Intent(
                    applicationContext,
                    MainActivity::class.java
                ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            )
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

