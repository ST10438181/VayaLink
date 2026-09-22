package com.vayalink.app.ui.journey

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.vayalink.app.data.model.Route
import com.vayalink.app.databinding.ActivityJourneyPlannerBinding
import com.vayalink.app.ui.adapter.RouteAdapter
import com.vayalink.app.util.Resource
import com.vayalink.app.viewmodel.JourneyViewModel

/**
 * FR11, FR12, FR13, FR14: search routes by origin/destination via the
 * hosted REST API and display fare + travel-time estimates.
 */
class JourneyPlannerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJourneyPlannerBinding
    private lateinit var adapter: RouteAdapter

    private val viewModel: JourneyViewModel by lazy {
        val repo = JourneyViewModel.defaultRepository(applicationContext)
        ViewModelProvider(this, JourneyViewModel.Factory(repo))[JourneyViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJourneyPlannerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = RouteAdapter { route -> openRouteDetails(route) }
        binding.rvRoutes.layoutManager = LinearLayoutManager(this)
        binding.rvRoutes.adapter = adapter

        binding.btnFindRoute.setOnClickListener {
            val origin = binding.etOrigin.text.toString().trim()
            val destination = binding.etDestination.text.toString().trim()
            if (origin.isEmpty() || destination.isEmpty()) {
                Toast.makeText(this, "Please enter both an origin and a destination.", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.searchRoutes(origin, destination)
            }
        }

        binding.btnBack.setOnClickListener { finish() }

        viewModel.routes.observe(this) { result ->
            when (result) {
                is Resource.Loading -> binding.progressBar.visibility = View.VISIBLE
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    adapter.submitList(result.data)
                    binding.tvEmptyState.visibility = if (result.data.isEmpty()) View.VISIBLE else View.GONE
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, result.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        viewModel.loadAllActiveRoutes()
    }

    private fun openRouteDetails(route: Route) {
        val intent = Intent(this, RouteDetailsActivity::class.java).apply {
            putExtra(RouteDetailsActivity.EXTRA_ROUTE_ID, route.routeId)
            putExtra(RouteDetailsActivity.EXTRA_ROUTE_NAME, route.routeName)
            putExtra(RouteDetailsActivity.EXTRA_ORIGIN, route.origin)
            putExtra(RouteDetailsActivity.EXTRA_DESTINATION, route.destination)
            putExtra(RouteDetailsActivity.EXTRA_BASE_FARE, route.baseFare)
            putExtra(RouteDetailsActivity.EXTRA_TRAVEL_TIME, route.estimatedTravelTime)
        }
        startActivity(intent)
    }
}
