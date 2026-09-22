package com.vayalink.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vayalink.app.data.model.IncidentReport
import com.vayalink.app.data.remote.RetrofitClient
import com.vayalink.app.data.repository.ReportRepository
import com.vayalink.app.util.Resource
import kotlinx.coroutines.launch

class ReportViewModel(
    private val repository: ReportRepository = ReportRepository(RetrofitClient.apiService)
) : ViewModel() {

    private val _submitResult = MutableLiveData<Resource<Unit>>()
    val submitResult: LiveData<Resource<Unit>> = _submitResult

    fun submitReport(report: IncidentReport) {
        _submitResult.value = Resource.Loading
        viewModelScope.launch {
            _submitResult.value = repository.submitReport(report)
        }
    }
}
