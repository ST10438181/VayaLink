package com.vayalink.app.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.vayalink.app.databinding.ActivitySplashBinding
import com.vayalink.app.ui.auth.LoginActivity
import com.vayalink.app.ui.home.HomeActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            val destination = if (FirebaseAuth.getInstance().currentUser != null) {
                Intent(this, HomeActivity::class.java)
            } else {
                Intent(this, LoginActivity::class.java)
            }
            startActivity(destination)
            finish()
        }, 1200)
    }
}
