package com.vayalink.app.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.vayalink.app.databinding.ActivityRegisterBinding
import com.vayalink.app.ui.home.HomeActivity
import com.vayalink.app.util.Resource
import com.vayalink.app.viewmodel.AuthViewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: AuthViewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            viewModel.register(
                email = binding.etEmail.text.toString().trim(),
                password = binding.etPassword.text.toString(),
                confirmPassword = binding.etConfirmPassword.text.toString(),
                displayName = binding.etName.text.toString().trim()
            )
        }

        viewModel.registerResult.observe(this) { result ->
            when (result) {
                is Resource.Loading -> binding.progressBar.visibility = android.view.View.VISIBLE
                is Resource.Success -> {
                    binding.progressBar.visibility = android.view.View.GONE
                    Toast.makeText(this, "Welcome to VayaLink!", Toast.LENGTH_SHORT).show()
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
