package com.vayalink.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vayalink.app.data.model.Alert
import com.vayalink.app.data.remote.RetrofitClient
import com.vayalink.app.data.repository.AlertRepository
import com.vayalink.app.util.Resource
import kotlinx.coroutines.launch

class AlertsViewModel(
    private val repository: AlertRepository = AlertRepository(RetrofitClient.apiService)
) : ViewModel() {

    private val _alerts = MutableLiveData<Resource<List<Alert>>>()
    val alerts: LiveData<Resource<List<Alert>>> = _alerts

    fun loadAlerts() {
        _alerts.value = Resource.Loading
        viewModelScope.launch {
            _alerts.value = repository.getAlerts()
        }
    }
}
