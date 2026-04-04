package com.example.uniquefragrancebd.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uniquefragrancebd.domain.model.User
import com.example.uniquefragrancebd.domain.repository.AuthRepository
import com.example.uniquefragrancebd.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _state.update { it.copy(error = "Please fill all fields") }
            return
        }
        repository.login(email, password).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.update { it.copy(user = result.data, isLoading = false, isSuccess = true) }
                }
                is Resource.Error -> {
                    _state.update { it.copy(error = result.message, isLoading = false) }
                }
                is Resource.Loading -> {
                    _state.update { it.copy(isLoading = true, error = null) }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun signup(name: String, email: String, password: String) {
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            _state.update { it.copy(error = "Please fill all fields") }
            return
        }
        repository.signup(name, email, password).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.update { it.copy(user = result.data, isLoading = false, isSuccess = true) }
                }
                is Resource.Error -> {
                    _state.update { it.copy(error = result.message, isLoading = false) }
                }
                is Resource.Loading -> {
                    _state.update { it.copy(isLoading = true, error = null) }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _state.update { AuthState() }
        }
    }

    fun getCurrentUser(): User? {
        return repository.getCurrentUser()
    }

    fun resetState() {
        _state.update { it.copy(error = null, isSuccess = false) }
    }
}