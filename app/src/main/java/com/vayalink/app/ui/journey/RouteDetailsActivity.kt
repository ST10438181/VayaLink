package com.vayalink.app.ui.journey

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.vayalink.app.data.model.Route
import com.vayalink.app.databinding.ActivityRouteDetailsBinding
import com.vayalink.app.viewmodel.JourneyViewModel

class RouteDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRouteDetailsBinding

    private val viewModel: JourneyViewModel by lazy {
        val repo = JourneyViewModel.defaultRepository(applicationContext)
        ViewModelProvider(this, JourneyViewModel.Factory(repo))[JourneyViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRouteDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val routeId = intent.getStringExtra(EXTRA_ROUTE_ID) ?: ""
        val routeName = intent.getStringExtra(EXTRA_ROUTE_NAME) ?: ""
        val origin = intent.getStringExtra(EXTRA_ORIGIN) ?: ""
        val destination = intent.getStringExtra(EXTRA_DESTINATION) ?: ""
        val baseFare = intent.getDoubleExtra(EXTRA_BASE_FARE, 0.0)
        val travelTime = intent.getIntExtra(EXTRA_TRAVEL_TIME, 0)

        binding.tvRouteName.text = routeName
        binding.tvOriginDestination.text = "$origin → $destination"
        binding.tvFare.text = "R %.2f".format(baseFare)
        binding.tvTravelTime.text = "$travelTime min"

        binding.btnBack.setOnClickListener { finish() }

        binding.btnSaveRoute.setOnClickListener {
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            if (uid == null) {
                Toast.makeText(this, "Please log in to save routes.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val route = Route(
                routeId = routeId,
                routeName = routeName,
                origin = origin,
                destination = destination,
                baseFare = baseFare,
                farePerKm = 0.0,
                estimatedTravelTime = travelTime,
                distanceKm = 0.0
            )
            viewModel.saveRoute(uid, route)
            Toast.makeText(this, "Route saved for offline viewing.", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val EXTRA_ROUTE_ID = "extra_route_id"
        const val EXTRA_ROUTE_NAME = "extra_route_name"
        const val EXTRA_ORIGIN = "extra_origin"
        const val EXTRA_DESTINATION = "extra_destination"
        const val EXTRA_BASE_FARE = "extra_base_fare"
        const val EXTRA_TRAVEL_TIME = "extra_travel_time"
    }
}
