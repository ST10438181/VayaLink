package com.vayalink.app.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vayalink.app.databinding.ActivityHomeBinding
import com.vayalink.app.ui.alerts.LiveAlertsActivity
import com.vayalink.app.ui.journey.JourneyPlannerActivity
import com.vayalink.app.ui.profile.ProfileActivity
import com.vayalink.app.ui.report.ReportIncidentActivity
import com.vayalink.app.ui.savedroutes.SavedRoutesActivity

/**
 * Home screen with large action buttons for each core feature, matching
 * the "home screen" mock-up in the Planning & Design document.
 */
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPlanJourney.setOnClickListener {
            startActivity(Intent(this, JourneyPlannerActivity::class.java))
        }
        binding.btnLiveAlerts.setOnClickListener {
            startActivity(Intent(this, LiveAlertsActivity::class.java))
        }
        binding.btnSavedRoutes.setOnClickListener {
            startActivity(Intent(this, SavedRoutesActivity::class.java))
        }
        binding.btnReportIncident.setOnClickListener {
            startActivity(Intent(this, ReportIncidentActivity::class.java))
        }
        binding.btnProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }
}
