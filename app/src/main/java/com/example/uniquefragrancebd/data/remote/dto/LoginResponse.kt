package com.example.uniquefragrancebd.data.remote.dto

data class LoginResponse(
    val id: String,
    val email: String,
    val name: String,
    val token: String
)