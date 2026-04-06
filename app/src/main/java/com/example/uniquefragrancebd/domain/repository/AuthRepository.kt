package com.example.uniquefragrancebd.domain.repository

import com.example.uniquefragrancebd.domain.model.User
import com.example.uniquefragrancebd.util.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun login(email: String, password: String): Flow<Resource<User>>
    fun signup(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        phone: String? = null,
        address: String? = null
    ): Flow<Resource<User>>
    suspend fun logout()
    fun getCurrentUser(): User?
}