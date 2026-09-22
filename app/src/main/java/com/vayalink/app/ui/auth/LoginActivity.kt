package com.vayalink.app.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.vayalink.app.databinding.ActivityLoginBinding
import com.vayalink.app.ui.home.HomeActivity
import com.vayalink.app.util.Resource
import com.vayalink.app.viewmodel.AuthViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: AuthViewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString()
            viewModel.login(email, password)
        }

        binding.tvCreateAccount.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

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
}
