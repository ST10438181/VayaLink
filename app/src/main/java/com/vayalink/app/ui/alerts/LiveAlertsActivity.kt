package com.vayalink.app.ui.alerts

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.vayalink.app.databinding.ActivityLiveAlertsBinding
import com.vayalink.app.ui.adapter.AlertAdapter
import com.vayalink.app.util.Resource
import com.vayalink.app.viewmodel.AlertsViewModel

/**
 * FR17 / FR19: displays active strike, delay, traffic and safety alerts
 * pulled from the hosted REST API.
 */
class LiveAlertsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLiveAlertsBinding
    private lateinit var adapter: AlertAdapter
    private val viewModel: AlertsViewModel by lazy {
        ViewModelProvider(this)[AlertsViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLiveAlertsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = AlertAdapter()
        binding.rvAlerts.layoutManager = LinearLayoutManager(this)
        binding.rvAlerts.adapter = adapter

        binding.btnBack.setOnClickListener { finish() }
        binding.swipeRefresh.setOnRefreshListener { viewModel.loadAlerts() }

        viewModel.alerts.observe(this) { result ->
            when (result) {
                is Resource.Loading -> binding.progressBar.visibility = View.VISIBLE
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.swipeRefresh.isRefreshing = false
                    adapter.submitList(result.data)
                    binding.tvEmptyState.visibility = if (result.data.isEmpty()) View.VISIBLE else View.GONE
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.swipeRefresh.isRefreshing = false
                    Toast.makeText(this, result.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        viewModel.loadAlerts()
    }
}
