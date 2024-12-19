package com.example.bangianslambook

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        // Delay for 3 seconds before redirecting to ActivityHome
        Handler(Looper.getMainLooper()).postDelayed({
            // After the delay, navigate to ActivityHome
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()  // Close the SplashActivity so the user can't go back to it
        }, 2500)  // 3000 milliseconds (3 seconds) delay
    }
}