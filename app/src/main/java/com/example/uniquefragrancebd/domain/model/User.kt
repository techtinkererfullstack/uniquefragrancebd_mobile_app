package com.example.uniquefragrancebd.domain.model

data class User(
    val id: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val phone: String?,
    val address: String?,
    val token: String? = null
) {
    val fullName: String get() = "$firstName $lastName"
}