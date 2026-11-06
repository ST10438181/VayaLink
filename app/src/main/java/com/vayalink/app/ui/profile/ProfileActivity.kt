package com.vayalink.app.ui.profile

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.vayalink.app.data.repository.AuthRepository
import com.vayalink.app.databinding.ActivityProfileBinding
import com.vayalink.app.ui.auth.LoginActivity
import com.vayalink.app.util.Resource
import com.vayalink.app.util.SessionManager
import com.vayalink.app.util.ValidationUtils
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import android.app.Activity
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.storage.FirebaseStorage
import com.bumptech.glide.Glide



/**
 * FR6, FR7, FR9, FR10: lets the user update their profile, change their
 * password (via the Firebase Auth SDK), set their preferred language and
 * notification settings, and log out (FR4).
 */
class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private val authRepository = AuthRepository()
    private lateinit var sessionManager: SessionManager

    private val pickImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageUri = result.data?.data
            if (imageUri != null) {
                uploadImageToFirebase(imageUri)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)


        val user = FirebaseAuth.getInstance().currentUser
        binding.tvName.text = user?.displayName ?: user?.email ?: "Guest"
        binding.tvEmail.text = user?.email ?: ""
        binding.switchNotifications.isChecked = sessionManager.notificationsEnabled
        binding.switchDataSaver.isChecked = sessionManager.dataSaverMode
        binding.spinnerLanguage.setSelection(if (sessionManager.preferredLanguage == "ZU") 1 else 0)

        binding.btnBack.setOnClickListener { finish() }

        binding.switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            sessionManager.notificationsEnabled = isChecked
        }
        binding.switchDataSaver.setOnCheckedChangeListener { _, isChecked ->
            sessionManager.dataSaverMode = isChecked
        }
        binding.spinnerLanguage.setOnItemSelectedListenerCompat { position ->
            // Position 0 is English, Position 1 is isiZulu
            val newLangCode = if (position == 1) "zu" else "en"

            // 1. Save it to your session manager
            sessionManager.preferredLanguage = if (position == 1) "ZU" else "EN"

            // 2. Check the current language of the app
            val currentLang = resources.configuration.locales[0].language

            // 3. Only recreate the activity if the language actually changed
            if (currentLang != newLangCode) {
                val locale = java.util.Locale(newLangCode)
                java.util.Locale.setDefault(locale)
                val config = android.content.res.Configuration()
                config.setLocale(locale)

                baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)

                // This will refresh the screen with the new language
                recreate()
            }

        }

        binding.btnChangePassword.setOnClickListener {
            val newPassword = binding.etNewPassword.text.toString()
            if (!ValidationUtils.isValidPassword(newPassword)) {
                Toast.makeText(this, "Password must be at least 8 characters.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            lifecycleScope.launch {
                when (val result = authRepository.updatePassword(newPassword)) {
                    is Resource.Success -> Toast.makeText(this@ProfileActivity, "Password updated.", Toast.LENGTH_SHORT).show()
                    is Resource.Error -> Toast.makeText(this@ProfileActivity, result.message, Toast.LENGTH_LONG).show()
                    Resource.Loading -> {}
                }
            }
        }

        binding.btnLogout.setOnClickListener {
            authRepository.logout()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.btnUploadImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            pickImage.launch(intent)
        }
    }
    private fun uploadImageToFirebase(uri: Uri) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val storageRef = FirebaseStorage.getInstance().reference.child("profile_pics/$userId.jpg")

        Toast.makeText(this, "Uploading...", Toast.LENGTH_SHORT).show()

        storageRef.putFile(uri)
            .addOnSuccessListener {
                Toast.makeText(this,"Upload successful!", Toast.LENGTH_SHORT).show()
                // Get the download URL and load it into the ImageView
                storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    Glide.with(this)
                        .load(downloadUri)
                        .into(binding.ivProfileImage)
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Upload failed: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

}

/** Small helper to keep the Spinner listener call above one line and readable. */
private fun android.widget.Spinner.setOnItemSelectedListenerCompat(onSelected: (Int) -> Unit) {
    this.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
            onSelected(position)
        }
        override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
    }
}


