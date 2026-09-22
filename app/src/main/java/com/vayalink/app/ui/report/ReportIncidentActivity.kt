package com.vayalink.app.ui.report

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.vayalink.app.data.model.IncidentReport
import com.vayalink.app.databinding.ActivityReportIncidentBinding
import com.vayalink.app.util.Resource
import com.vayalink.app.viewmodel.ReportViewModel

/**
 * FR23 / FR24: lets a user report driver behaviour or a safety/traffic
 * incident. Submits via POST /reports on the hosted REST API.
 */
class ReportIncidentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReportIncidentBinding
    private var selectedType = "traffic"

    private val viewModel: ReportViewModel by lazy {
        ViewModelProvider(this)[ReportViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportIncidentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        binding.chipTraffic.setOnClickListener { selectedType = "traffic" }
        binding.chipSafety.setOnClickListener { selectedType = "safety_concern" }
        binding.chipStrike.setOnClickListener { selectedType = "strike" }
        binding.chipRoadClosure.setOnClickListener { selectedType = "road_closure" }

        binding.btnSubmitReport.setOnClickListener {
            val description = binding.etDescription.text.toString().trim()
            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: "guest"

            val report = IncidentReport(
                userId = uid,
                reportType = selectedType,
                description = description
            )
            viewModel.submitReport(report)
        }

        viewModel.submitResult.observe(this) { result ->
            when (result) {
                is Resource.Loading -> binding.progressBar.visibility = View.VISIBLE
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "Thanks - your report has been submitted.", Toast.LENGTH_LONG).show()
                    finish()
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, result.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
