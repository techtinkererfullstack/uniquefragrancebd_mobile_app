package com.example.uniquefragrancebd.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SignupRequestDto(
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    val email: String,
    val password: String,
    val phone: String? = null,
    val address: String? = null
)