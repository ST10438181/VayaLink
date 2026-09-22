package com.vayalink.app.ui.savedroutes

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.vayalink.app.databinding.ActivitySavedRoutesBinding
import com.vayalink.app.ui.adapter.SavedRouteAdapter
import com.vayalink.app.viewmodel.JourneyViewModel

/**
 * FR8, FR20-22: shows routes cached locally in Room so they remain visible
 * offline (NFR8).
 */
class SavedRoutesActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySavedRoutesBinding
    private lateinit var adapter: SavedRouteAdapter

    private val viewModel: JourneyViewModel by lazy {
        val repo = JourneyViewModel.defaultRepository(applicationContext)
        ViewModelProvider(this, JourneyViewModel.Factory(repo))[JourneyViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedRoutesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = SavedRouteAdapter { route ->
            viewModel.removeSavedRoute(route)
            Toast.makeText(this, "Route removed.", Toast.LENGTH_SHORT).show()
        }
        binding.rvSavedRoutes.layoutManager = LinearLayoutManager(this)
        binding.rvSavedRoutes.adapter = adapter

        binding.btnBack.setOnClickListener { finish() }

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid == null) {
            binding.tvEmptyState.visibility = View.VISIBLE
            binding.tvEmptyState.text = "Log in to view your saved routes."
        } else {
            viewModel.observeSavedRoutes(uid).asLiveData().observe(this, Observer { routes ->
                adapter.submitList(routes)
                binding.tvEmptyState.visibility = if (routes.isEmpty()) View.VISIBLE else View.GONE
            })
        }
    }
}
