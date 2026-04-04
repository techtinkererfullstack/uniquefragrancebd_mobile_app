package com.example.uniquefragrancebd.data.repository

import com.example.uniquefragrancebd.data.local.SessionManager
import com.example.uniquefragrancebd.data.remote.ApiService
import com.example.uniquefragrancebd.data.remote.dto.LoginRequestDto
import com.example.uniquefragrancebd.data.remote.dto.SignupRequestDto
import com.example.uniquefragrancebd.domain.model.User
import com.example.uniquefragrancebd.domain.repository.AuthRepository
import com.example.uniquefragrancebd.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) : AuthRepository {

    override fun login(email: String, password: String): Flow<Resource<User>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.login(LoginRequestDto(email, password))
            sessionManager.saveAuthToken(response.token)
            sessionManager.saveUser(response.userId, response.email, response.name)
            val user = User(response.userId, response.email, response.name, response.token)
            emit(Resource.Success(user))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Login failed"))
        }
    }

    override fun signup(name: String, email: String, password: String): Flow<Resource<User>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.signup(SignupRequestDto(name, email, password))
            sessionManager.saveAuthToken(response.token)
            sessionManager.saveUser(response.userId, response.email, response.name)
            val user = User(response.userId, response.email, response.name, response.token)
            emit(Resource.Success(user))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Signup failed"))
        }
    }

    override suspend fun logout() {
        sessionManager.clearSession()
    }

    override fun getCurrentUser(): User? {
        val token = sessionManager.getAuthToken() ?: return null
        val userId = sessionManager.getUserId() ?: ""
        val email = sessionManager.getUserEmail() ?: ""
        val name = sessionManager.getUserName() ?: ""
        return User(userId, email, name, token)
    }
}