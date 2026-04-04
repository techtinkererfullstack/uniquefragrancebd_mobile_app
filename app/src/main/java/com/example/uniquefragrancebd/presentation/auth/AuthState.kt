package com.example.uniquefragrancebd.presentation.auth

import com.example.uniquefragrancebd.domain.model.User

data class AuthState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)