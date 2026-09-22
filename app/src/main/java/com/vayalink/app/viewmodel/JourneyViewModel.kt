package com.vayalink.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vayalink.app.data.local.SavedRouteEntity
import com.vayalink.app.data.model.Route
import com.vayalink.app.data.remote.RetrofitClient
import com.vayalink.app.data.repository.RouteRepository
import com.vayalink.app.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class JourneyViewModel(private val repository: RouteRepository) : ViewModel() {

    private val _routes = MutableLiveData<Resource<List<Route>>>()
    val routes: LiveData<Resource<List<Route>>> = _routes

    fun searchRoutes(origin: String, destination: String) {
        _routes.value = Resource.Loading
        viewModelScope.launch {
            _routes.value = repository.searchRoutes(origin, destination)
        }
    }

    fun loadAllActiveRoutes() {
        _routes.value = Resource.Loading
        viewModelScope.launch {
            _routes.value = repository.getActiveRoutes()
        }
    }

    fun saveRoute(userId: String, route: Route) {
        viewModelScope.launch { repository.saveRouteOffline(userId, route) }
    }

    fun observeSavedRoutes(userId: String): Flow<List<SavedRouteEntity>> =
        repository.observeSavedRoutes(userId)

    fun removeSavedRoute(route: SavedRouteEntity) {
        viewModelScope.launch { repository.removeSavedRoute(route) }
    }

    class Factory(private val repository: RouteRepository) : androidx.lifecycle.ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            JourneyViewModel(repository) as T
    }

    companion object {
        fun defaultRepository(context: android.content.Context): RouteRepository =
            RouteRepository(
                apiService = RetrofitClient.apiService,
                savedRouteDao = com.vayalink.app.data.local.AppDatabase.getInstance(context).savedRouteDao()
            )
    }
}
