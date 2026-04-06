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
            val token = response.token ?: ""
            val userDto = response.user
            
            if (userDto != null) {
                sessionManager.saveAuthToken(token)
                sessionManager.saveUser(
                    userId = userDto.id,
                    email = userDto.email,
                    firstName = userDto.firstName,
                    lastName = userDto.lastName,
                    phone = userDto.phone,
                    address = userDto.address
                )
                
                val user = User(
                    id = userDto.id,
                    email = userDto.email,
                    firstName = userDto.firstName,
                    lastName = userDto.lastName,
                    phone = userDto.phone,
                    address = userDto.address,
                    token = token
                )
                emit(Resource.Success(user))
            } else {
                emit(Resource.Error("User data not found"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Login failed"))
        }
    }

    override fun signup(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        phone: String?,
        address: String?
    ): Flow<Resource<User>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.signup(
                SignupRequestDto(
                    firstName = firstName,
                    lastName = lastName,
                    email = email,
                    password = password,
                    phone = phone,
                    address = address
                )
            )
            val token = response.token ?: ""
            val userDto = response.user
            
            if (userDto != null) {
                sessionManager.saveAuthToken(token)
                sessionManager.saveUser(
                    userId = userDto.id,
                    email = userDto.email,
                    firstName = userDto.firstName,
                    lastName = userDto.lastName,
                    phone = userDto.phone,
                    address = userDto.address
                )
                
                val user = User(
                    id = userDto.id,
                    email = userDto.email,
                    firstName = userDto.firstName,
                    lastName = userDto.lastName,
                    phone = userDto.phone,
                    address = userDto.address,
                    token = token
                )
                emit(Resource.Success(user))
            } else {
                emit(Resource.Error("User data not found"))
            }
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
        val firstName = sessionManager.getFirstName() ?: ""
        val lastName = sessionManager.getLastName() ?: ""
        val phone = sessionManager.getPhone()
        val address = sessionManager.getAddress()
        
        return User(userId, email, firstName, lastName, phone, address, token)
    }
}