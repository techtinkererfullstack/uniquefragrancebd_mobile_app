package com.example.uniquefragrancebd.data.remote.dto

data class AuthResponseDto(
    val token: String,
    val userId: String,
    val email: String,
    val name: String
)