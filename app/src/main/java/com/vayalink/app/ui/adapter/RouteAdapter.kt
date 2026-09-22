package com.vayalink.app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vayalink.app.data.model.Route
import com.vayalink.app.databinding.ItemRouteBinding
import com.vayalink.app.util.FareCalculator

class RouteAdapter(
    private val onRouteClick: (Route) -> Unit
) : RecyclerView.Adapter<RouteAdapter.RouteViewHolder>() {

    private val routes = mutableListOf<Route>()

    fun submitList(newRoutes: List<Route>) {
        routes.clear()
        routes.addAll(newRoutes)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteViewHolder {
        val binding = ItemRouteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RouteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RouteViewHolder, position: Int) {
        holder.bind(routes[position])
    }

    override fun getItemCount() = routes.size

    inner class RouteViewHolder(private val binding: ItemRouteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(route: Route) {
            binding.tvRouteName.text = route.routeName
            binding.tvOriginDestination.text = "${route.origin} → ${route.destination}"
            binding.tvFare.text = "R %.2f".format(FareCalculator.estimateFare(route))
            binding.tvTravelTime.text = "${route.estimatedTravelTime} min"
            binding.root.setOnClickListener { onRouteClick(route) }
        }
    }
}
