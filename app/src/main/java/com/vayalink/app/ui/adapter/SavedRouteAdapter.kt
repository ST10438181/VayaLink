package com.vayalink.app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vayalink.app.data.local.SavedRouteEntity
import com.vayalink.app.databinding.ItemSavedRouteBinding

class SavedRouteAdapter(
    private val onDeleteClick: (SavedRouteEntity) -> Unit
) : RecyclerView.Adapter<SavedRouteAdapter.SavedRouteViewHolder>() {

    private val routes = mutableListOf<SavedRouteEntity>()

    fun submitList(newRoutes: List<SavedRouteEntity>) {
        routes.clear()
        routes.addAll(newRoutes)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedRouteViewHolder {
        val binding = ItemSavedRouteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SavedRouteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SavedRouteViewHolder, position: Int) {
        holder.bind(routes[position])
    }

    override fun getItemCount() = routes.size

    inner class SavedRouteViewHolder(private val binding: ItemSavedRouteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(route: SavedRouteEntity) {
            binding.tvRouteName.text = route.routeName
            binding.tvOriginDestination.text = "${route.origin} → ${route.destination}"
            binding.tvFare.text = "R %.2f · %d min".format(route.baseFare, route.estimatedTravelTime)
            binding.btnDelete.setOnClickListener { onDeleteClick(route) }
        }
    }
}
