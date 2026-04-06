package com.example.uniquefragrancebd.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AuthResponseDto(
    @SerializedName("access_token")
    val token: String?,
    val user: UserDto?
)

data class UserDto(
    val id: String,
    val email: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    val phone: String?,
    @SerializedName("is_admin")
    val isAdmin: Boolean,
    val address: String?
)