package com.vayalink.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vayalink.app.data.model.User
import com.vayalink.app.data.repository.AuthRepository
import com.vayalink.app.util.Resource
import com.vayalink.app.util.ValidationUtils
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository = AuthRepository()) : ViewModel() {

    private val _registerResult = MutableLiveData<Resource<User>>()
    val registerResult: LiveData<Resource<User>> = _registerResult

    private val _loginResult = MutableLiveData<Resource<String>>()
    val loginResult: LiveData<Resource<String>> = _loginResult

    val isLoggedIn: Boolean get() = repository.currentUser != null

    fun register(email: String, password: String, confirmPassword: String, displayName: String) {
        if (!ValidationUtils.isValidEmail(email)) {
            _registerResult.value = Resource.Error("Please enter a valid email address.")
            return
        }
        if (!ValidationUtils.isValidPassword(password)) {
            _registerResult.value = Resource.Error("Password must be at least 8 characters.")
            return
        }
        if (password != confirmPassword) {
            _registerResult.value = Resource.Error("Passwords do not match.")
            return
        }

        _registerResult.value = Resource.Loading
        viewModelScope.launch {
            _registerResult.value = repository.register(email, password, displayName)
        }
    }

    fun login(email: String, password: String) {
        if (!ValidationUtils.isValidEmail(email)) {
            _loginResult.value = Resource.Error("Please enter a valid email address.")
            return
        }
        _loginResult.value = Resource.Loading
        viewModelScope.launch {
            _loginResult.value = repository.login(email, password)
        }
    }

    fun logout() = repository.logout()
}
