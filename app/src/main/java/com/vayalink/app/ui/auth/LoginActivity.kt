package com.vayalink.app.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.vayalink.app.R
import com.vayalink.app.databinding.ActivityLoginBinding
import com.vayalink.app.ui.home.HomeActivity
import com.vayalink.app.util.Resource
import com.vayalink.app.viewmodel.AuthViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: AuthViewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }

    // --- New Firebase/Google variables ---
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001
    // -------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sessionManager = com.vayalink.app.util.SessionManager(this) // Adjust import if needed
        val savedLang = sessionManager.preferredLanguage ?: "EN"

        val locale = java.util.Locale(savedLang.lowercase())
        java.util.Locale.setDefault(locale)
        val config = android.content.res.Configuration()
        config.setLocale(locale)
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)



        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // 1. Normal Email/Password Login
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString()
            viewModel.login(email, password)
        }

        // 2. Google Sign In Button Click
        binding.btnGoogleSignIn.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        // 3. Navigate to Register
        binding.tvCreateAccount.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        // 4. Observe ViewModel for normal login
        viewModel.loginResult.observe(this) { result ->
            when (result) {
                is Resource.Loading -> binding.progressBar.visibility = android.view.View.VISIBLE
                is Resource.Success -> {
                    binding.progressBar.visibility = android.view.View.GONE
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = android.view.View.GONE
                    Toast.makeText(this, result.message, Toast.LENGTH_LONG).show()
                }
            }
        }

    }

    // --- New Functions for Google Sign In Result ---

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d("LoginActivity", "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed
                Log.w("LoginActivity", "Google sign in failed", e)
                Toast.makeText(this, "Google Sign-In failed. Check your SHA-1.", Toast.LENGTH_LONG).show()
            }
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("FCM_TOKEN", "My Device Token: ${task.result}")
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        binding.progressBar.visibility = android.view.View.VISIBLE // Show loading
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                binding.progressBar.visibility = android.view.View.GONE // Hide loading
                if (task.isSuccessful) {
                    // Sign in success! Navigate to Home
                    Toast.makeText(this, "Google Sign-In Successful!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                } else {
                    // If sign in fails
                    Toast.makeText(this, "Firebase Authentication Failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}