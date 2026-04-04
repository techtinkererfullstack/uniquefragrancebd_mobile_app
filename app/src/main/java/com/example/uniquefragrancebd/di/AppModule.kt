package com.example.uniquefragrancebd.di

import android.content.Context
import androidx.room.Room
import com.example.uniquefragrancebd.data.local.ProductDatabase
import com.example.uniquefragrancebd.data.local.SessionManager
import com.example.uniquefragrancebd.data.local.dao.CartDao
import com.example.uniquefragrancebd.data.local.dao.OrderDao
import com.example.uniquefragrancebd.data.local.dao.ProductDao
import com.example.uniquefragrancebd.data.local.dao.WishlistDao
import com.example.uniquefragrancebd.data.remote.ApiService
import com.example.uniquefragrancebd.data.remote.AuthInterceptor
import com.example.uniquefragrancebd.data.repository.AuthRepositoryImpl
import com.example.uniquefragrancebd.data.repository.CartRepositoryImpl
import com.example.uniquefragrancebd.data.repository.OrderRepositoryImpl
import com.example.uniquefragrancebd.data.repository.ProductRepositoryImpl
import com.example.uniquefragrancebd.data.repository.WishlistRepositoryImpl
import com.example.uniquefragrancebd.domain.repository.AuthRepository
import com.example.uniquefragrancebd.domain.repository.CartRepository
import com.example.uniquefragrancebd.domain.repository.OrderRepository
import com.example.uniquefragrancebd.domain.repository.ProductRepository
import com.example.uniquefragrancebd.domain.repository.WishlistRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(okHttpClient: OkHttpClient): ApiService {
        return Retrofit.Builder()
            .baseUrl("https://api.uniquefragrancebd.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideProductDatabase(@ApplicationContext context: Context): ProductDatabase {
        return Room.databaseBuilder(
            context,
            ProductDatabase::class.java,
            "product_db"
        ).build()
    }

    @Provides
    fun provideProductDao(database: ProductDatabase): ProductDao {
        return database.productDao
    }

    @Provides
    fun provideCartDao(database: ProductDatabase): CartDao {
        return database.cartDao
    }

    @Provides
    fun provideOrderDao(database: ProductDatabase): OrderDao {
        return database.orderDao
    }

    @Provides
    fun provideWishlistDao(database: ProductDatabase): WishlistDao {
        return database.wishlistDao
    }

    @Provides
    @Singleton
    fun provideProductRepository(
        apiService: ApiService,
        productDao: ProductDao
    ): ProductRepository {
        return ProductRepositoryImpl(apiService, productDao)
    }

    @Provides
    @Singleton
    fun provideCartRepository(
        cartDao: CartDao
    ): CartRepository {
        return CartRepositoryImpl(cartDao)
    }

    @Provides
    @Singleton
    fun provideOrderRepository(
        orderDao: OrderDao
    ): OrderRepository {
        return OrderRepositoryImpl(orderDao)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        apiService: ApiService,
        sessionManager: SessionManager
    ): AuthRepository {
        return AuthRepositoryImpl(apiService, sessionManager)
    }

    @Provides
    @Singleton
    fun provideWishlistRepository(
        wishlistDao: WishlistDao
    ): WishlistRepository {
        return WishlistRepositoryImpl(wishlistDao)
    }
}