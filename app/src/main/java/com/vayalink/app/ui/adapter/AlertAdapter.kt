package com.vayalink.app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vayalink.app.data.model.Alert
import com.vayalink.app.databinding.ItemAlertBinding

class AlertAdapter : RecyclerView.Adapter<AlertAdapter.AlertViewHolder>() {

    private val alerts = mutableListOf<Alert>()

    fun submitList(newAlerts: List<Alert>) {
        alerts.clear()
        alerts.addAll(newAlerts)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertViewHolder {
        val binding = ItemAlertBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlertViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlertViewHolder, position: Int) {
        holder.bind(alerts[position])
    }

    override fun getItemCount() = alerts.size

    inner class AlertViewHolder(private val binding: ItemAlertBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(alert: Alert) {
            binding.tvAlertType.text = alert.alertType.replace("_", " ").uppercase()
            binding.tvAlertDescription.text = alert.description
            binding.tvAlertTimestamp.text = alert.timestamp
        }
    }
}
