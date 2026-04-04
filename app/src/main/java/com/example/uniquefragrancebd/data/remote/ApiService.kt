package com.example.uniquefragrancebd.data.remote

import com.example.uniquefragrancebd.data.remote.dto.AuthResponseDto
import com.example.uniquefragrancebd.data.remote.dto.LoginRequestDto
import com.example.uniquefragrancebd.data.remote.dto.ProductDto
import com.example.uniquefragrancebd.data.remote.dto.SignupRequestDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("products")
    suspend fun getProducts(): List<ProductDto>

    @POST("login")
    suspend fun login(@Body request: LoginRequestDto): AuthResponseDto

    @POST("signup")
    suspend fun signup(@Body request: SignupRequestDto): AuthResponseDto
}