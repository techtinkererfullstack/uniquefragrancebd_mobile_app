package com.example.uniquefragrancebd.data.remote

import com.example.uniquefragrancebd.data.local.SessionManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val sessionManager: SessionManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        
        // Add Supabase API Key
        // Replace "YOUR_SUPABASE_ANON_KEY" with your actual key from Supabase dashboard
        request.addHeader("apikey", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InF4bGNlY21wcG13eHdieGl2ZGZ4Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzQ3NTY3MTUsImV4cCI6MjA5MDMzMjcxNX0.0_qlXUreJF4DM8KDH3qdL4F6ZV6Zh_PMJs_m1RGsL8Q")
        
        sessionManager.getAuthToken()?.let { token ->
            request.addHeader("Authorization", "Bearer $token")
        }
        
        return chain.proceed(request.build())
    }
}